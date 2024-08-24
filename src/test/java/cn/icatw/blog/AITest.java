package cn.icatw.blog;

import cn.icatw.blog.domain.params.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/8/24
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Ai测试")
@Slf4j
public class AITest {
    @Autowired
    private RestTemplate restTemplate;

    // POST http://localhost:8080/ai/send
    // Content-Type: application/json
    //
    // {
    //     "userId": "",
    //         "message": ""
    // }
    @Test
    public void aiTest() {
        log.info("AI测试");
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUserId("");
        chatMessage.setMessage("你好");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:8080/ai/send",
                chatMessage,
                String.class
        );

        HttpHeaders headers = response.getHeaders();
        String contentType = headers.getContentType().toString();
        log.info("Content-Type: {}", contentType);

        String body = response.getBody();
        log.info("Body: {}", body);

        // 确保使用正确的字符集解析字符串
        String decodedBody = new String(body.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        log.info("Decoded Body: {}", decodedBody);
    }
}

