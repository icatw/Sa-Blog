package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.Comment;
import cn.icatw.blog.domain.dto.CommentBackDTO;
import cn.icatw.blog.domain.dto.CommentDTO;
import cn.icatw.blog.domain.dto.ReplyDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.CommentVO;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.ReviewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Comment)表服务接口
 *
 * @author icatw
 * @since 2024-03-22 20:39:50
 */
public interface CommentService extends IService<Comment> {
    /**
     * 后台评论列表
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link CommentBackDTO}>
     */
    PageResult<CommentBackDTO> listCommentBackDTO(ConditionParams condition);

    /**
     * 更新评论审核状态
     *
     * @param reviewVO 审查vo
     */
    void updateCommentsReview(ReviewVO reviewVO);

    /**
     * 查询当前页面评论
     *
     * @param commentVO 评论vo
     * @return {@link PageResult}<{@link CommentDTO}>
     */
    PageResult<CommentDTO> listComments(CommentVO commentVO);

    /**
     * 添加评论
     *
     * @param commentVO 评论vo
     */
    void saveComment(CommentVO commentVO);

    /**
     * 查询评论下的回复
     *
     * @param commentId 评论id
     * @return {@link List}<{@link ReplyDTO}>
     */
    List<ReplyDTO> listRepliesByCommentId(Integer commentId);

    /**
     * 评论点赞
     *
     * @param commentId 评论id
     */
    void saveCommentLike(Integer commentId);

}

