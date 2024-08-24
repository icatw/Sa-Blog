package cn.icatw.blog.mapper;

import cn.icatw.blog.domain.dto.*;
import cn.icatw.blog.domain.entity.Comment;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.CommentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * (Comment)表数据库访问层
 *
 * @author icatw
 * @since 2024-03-22 20:39:50
 */
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 评论数量
     *
     * @param condition 条件
     * @return {@link Integer}
     */
    Integer countCommentDTO(@Param("condition") ConditionParams condition);

    /**
     * 后台评论列表
     *
     * @param current   当前页
     * @param size      总页
     * @param condition 条件
     * @return {@link List}<{@link CommentBackDTO}>
     */
    List<CommentBackDTO> listCommentBackDTO(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionParams condition);

    /**
     * 按talk id列出评论计数
     *
     * @param talkIdList 说说id列表
     * @return {@link List}<{@link CommentCountDTO}>
     */
    List<CommentCountDTO> listCommentCountByTalkIds(List<Integer> talkIdList);

    /**
     * 评论列表
     *
     * @param current   现在
     * @param size      大小
     * @param commentVO 评论vo
     * @return {@link List}<{@link CommentDTO}>
     */
    List<CommentDTO> listComments(@Param("current") Long current, @Param("size") Long size, @Param("commentVO") CommentVO commentVO);

    /**
     * 查询评论底下的回复
     *
     * @param commentIdList 评论id列表
     * @return {@link List}<{@link ReplyDTO}>
     */
    List<ReplyDTO> listReplies(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 根据评论id查询回复总量
     *
     * @param commentIdList 评论id列表
     * @return {@link Collection}<{@link Object}>
     */
    Collection<ReplyCountDTO> listReplyCountByCommentId(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 查看当条评论下的回复
     *
     * @param current   现在
     * @param size      大小
     * @param commentId 评论id
     * @return {@link List}<{@link ReplyDTO}>
     */
    List<ReplyDTO> listRepliesByCommentId(@Param("current") Long current, @Param("size") Long size, @Param("commentId") Integer commentId);
}

