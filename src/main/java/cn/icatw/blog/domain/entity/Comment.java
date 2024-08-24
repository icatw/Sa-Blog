package cn.icatw.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (Comment)实体类
 *
 * @author icatw
 * @since 2024-03-22 20:39:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_comment")
@Builder
public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = 488220694879656395L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论用户Id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 评论文章id
     */
    @TableField(value = "article_id")
    private Integer articleId;

    /**
     * 评论说说id
     */
    @TableField(value = "talk_id")
    private Integer talkId;

    /**
     * 评论内容
     */
    @TableField(value = "comment_content")
    private String commentContent;

    /**
     * 回复用户id
     */
    @TableField(value = "reply_user_id")
    private Integer replyUserId;

    /**
     * 父评论id
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 评论类型 1.文章 2.友链 3.说说
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 是否删除  0否 1是
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 是否审核
     */
    @TableField(value = "is_review")
    private Integer isReview;

    /**
     * 评论时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

