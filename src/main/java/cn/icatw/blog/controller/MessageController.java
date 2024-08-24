package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.Limit;
import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.MessageBackDTO;
import cn.icatw.blog.domain.dto.MessageDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.MessageService;
import cn.icatw.blog.domain.vo.MessageVO;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.ReviewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.icatw.blog.constant.OptTypeConst.REMOVE;
import static cn.icatw.blog.constant.OptTypeConst.UPDATE;

/**
 * (Message)表控制层
 *
 * @author icatw
 * @since 2024-03-22 20:39:51
 */
@RestController
@Tag(name = "留言模块", description = "留言模块")
public class MessageController {

    /**
     * 构造方法依赖注入
     */
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 查看后台留言列表
     *
     * @param condition 条件
     * @return {@link Result<MessageBackDTO>} 留言列表
     */
    @Operation(summary = "查看后台留言列表", description = "查看后台留言列表")
    @GetMapping("/admin/messages")
    public Result<PageResult<MessageBackDTO>> listMessageBackDTO(ConditionParams condition) {
        return Result.ok(messageService.listMessageBackDTO(condition));
    }

    /**
     * 审核留言
     *
     * @param reviewVO 审核信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @Operation(summary = "审核留言", description = "审核留言")
    @PutMapping("/admin/messages/review")
    public Result<?> updateMessagesReview(@Valid @RequestBody ReviewVO reviewVO) {
        messageService.updateMessagesReview(reviewVO);
        return Result.ok();
    }

    /**
     * 删除留言
     *
     * @param messageIdList 留言id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @Operation(summary = "删除留言", description = "删除留言")
    @DeleteMapping("/admin/messages")
    public Result<?> deleteMessages(@RequestBody List<Integer> messageIdList) {
        messageService.removeByIds(messageIdList);
        return Result.ok();
    }

    /**
     * 添加留言
     *
     * @param messageVO 留言信息
     * @return {@link Result <>}
     */
    @Limit(key = "addMessage", period = 60, count = 5, name = "添加留言接口", prefix = "limit")
    @Operation(summary = "添加留言", description = "添加留言")
    @PostMapping("/messages")
    public Result<?> saveMessage(@Valid @RequestBody MessageVO messageVO) {
        messageService.saveMessage(messageVO);
        return Result.ok();
    }

    /**
     * 查看留言列表
     *
     * @return {@link Result<MessageDTO>} 留言列表
     */
    @Operation(summary = "前台查看留言列表", description = "查看留言列表")
    @GetMapping("/messages")
    public Result<List<MessageDTO>> listMessages() {
        return Result.ok(messageService.listMessages());
    }
}

