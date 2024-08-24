package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.constant.OptTypeConst;
import cn.icatw.blog.domain.dto.CommentBackDTO;
import cn.icatw.blog.domain.dto.CommentDTO;
import cn.icatw.blog.domain.dto.ReplyDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.CommentService;
import cn.icatw.blog.domain.vo.CommentVO;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.ReviewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.icatw.blog.constant.OptTypeConst.UPDATE;

/**
 * (Comment)表控制层
 *
 * @author icatw
 * @since 2024-03-22 20:39:50
 */
@RestController
@Tag(name = "评论模块", description = "评论模块")
public class CommentController {

    /**
     * 构造方法依赖注入
     */
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 查询后台评论
     *
     * @param condition 条件
     * @return {@link Result<CommentBackDTO>} 后台评论
     */
    @Operation(summary = "查询后台评论", description = "查询后台评论")
    @GetMapping("/admin/comments")
    public Result<PageResult<CommentBackDTO>> listCommentBackDTO(ConditionParams condition) {
        return Result.ok(commentService.listCommentBackDTO(condition));
    }

    /**
     * 删除评论
     *
     * @param commentIdList 评论id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @Operation(summary = "删除评论", description = "根据主键id删除评论")
    @DeleteMapping("/admin/comments")
    public Result<?> deleteComments(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return Result.ok();
    }
    /**
     * 审核评论
     *
     * @param reviewVO 审核信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @Operation(summary = "审核评论",description = "审核评论")
    @PutMapping("/admin/comments/review")
    public Result<?> updateCommentsReview(@Valid @RequestBody ReviewVO reviewVO) {
        commentService.updateCommentsReview(reviewVO);
        return Result.ok();
    }
    @Operation(summary = "查询当前页面的评论")
    @GetMapping("/comments")
    public Result<PageResult<CommentDTO>> listComments(CommentVO commentVO) {
        return Result.ok(commentService.listComments(commentVO));
    }


    /**
     * 添加评论
     *
     * @param commentVO 评论信息
     * @return {@link Result<>}
     */
    @Operation(summary = "添加评论")
    @PostMapping("/comments")
    public Result<?> saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return Result.ok();
    }

    /**
     * 查询评论下的回复
     *
     * @param commentId 评论id
     * @return {@link Result<ReplyDTO>} 回复列表
     */
    @Operation(summary = "查询评论下的回复")
    @GetMapping("/comments/{commentId}/replies")
    public Result<List<ReplyDTO>> listRepliesByCommentId(@PathVariable("commentId") Integer commentId) {
        return Result.ok(commentService.listRepliesByCommentId(commentId));
    }

    /**
     * 评论点赞
     *
     * @param commentId 评论id
     * @return {@link Result<>}
     */
    @Operation(summary = "评论点赞")
    @PostMapping("/comments/{commentId}/like")
    public Result<?> saveCommentLike(@PathVariable("commentId") Integer commentId) {
        commentService.saveCommentLike(commentId);
        return Result.ok();
    }
}

