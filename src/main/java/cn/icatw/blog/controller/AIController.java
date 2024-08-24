package cn.icatw.blog.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.icatw.blog.domain.params.ChatMessage;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.fastjson.JSONObject;
import io.reactivex.Flowable;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote AI控制器
 * @since 2024/8/24
 */
@RestController
@RequestMapping(value = "/ai")
public class AIController {
    @Value("${ai.tongyi.appKey}")
    private String appKey;
    @Resource
    private Generation generation;
    // 用于存放用户的会话信息
    private final ConcurrentHashMap<String, List<Message>> conversationHistory = new ConcurrentHashMap<>();

    // @SaIgnore
    @PostMapping(value = "/send", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> aiTalk(@RequestBody ChatMessage chatMessage, HttpServletResponse response)
            throws NoApiKeyException, InputRequiredException {

        // String userId = chatMessage.getUserId();
        String userId = StpUtil.getLoginIdAsString();

        List<Message> history = conversationHistory.computeIfAbsent(userId, k -> new ArrayList<>());

        // 创建一个Message对象，表示用户发送的消息
        Message userMessage = createMessage(Role.USER, chatMessage.getMessage());
        history.add(userMessage);

        QwenParam qwenParam = QwenParam.builder()
                .model(Generation.Models.QWEN_PLUS)
                .messages(history) // 使用整个会话历史作为输入
                .topP(0.8)
                .resultFormat(QwenParam.ResultFormat.MESSAGE)
                .enableSearch(true)
                .apiKey(appKey)
                .incrementalOutput(true)
                .build();

        Flowable<GenerationResult> result = generation.streamCall(qwenParam);

        // 创建一个StringBuilder来累积所有的响应内容
        StringBuilder accumulatedContent = new StringBuilder();

        return Flux.from(result)
                .concatMap(m -> {
                    // GenerationResult对象中输出流(GenerationOutput)的choices是一个列表，存放着生成的数据。
                    String content = m.getOutput().getChoices().get(0).getMessage().getContent();
                    // 累积响应内容
                    accumulatedContent.append(content);

                    // 生成一个唯一序列号
                    long snowflakeNextId = IdUtil.getSnowflakeNextId();

                    // 返回流式数据
                    return Mono.just(ServerSentEvent.<String>builder()
                            .event("message")
                            .data("data: " + snowflakeNextId + " " + content)
                            .build());
                })
                .doOnComplete(() -> {
                    // 当流完成时，将完整的响应添加到会话历史中
                    if (!accumulatedContent.isEmpty()) {
                        Message sysMsg = createMessage(Role.SYSTEM, accumulatedContent.toString());
                        history.add(sysMsg);
                    }
                })
                .publishOn(Schedulers.immediate())
                .doOnError(e -> {
                    Map<String, Object> errorMap = new HashMap<>();
                    errorMap.put("code", "400");
                    errorMap.put("message", "出现了异常，请稍后重试");
                    try {
                        response.getOutputStream().print(JSONObject.toJSONString(errorMap));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    private static Message createMessage(Role role, String content) {
        return Message.builder().role(role.getValue()).content(content).build();
    }
}
