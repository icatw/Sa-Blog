package cn.icatw.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (Article)实体类
 *
 * @author icatw
 * @since 2024-03-22 20:36:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_article")
@Builder
@Schema(description = "文章")
public class Article implements Serializable {
    @Serial
    private static final long serialVersionUID = -41789945590214591L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作者
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 文章分类
     */
    @TableField(value = "category_id")
    private Integer categoryId;

    /**
     * 文章缩略图
     */
    @TableField(value = "article_cover")
    private String articleCover;

    /**
     * 标题
     */
    @TableField(value = "article_title")
    private String articleTitle;

    /**
     * 内容
     */
    @TableField(value = "article_content")
    private String articleContent;

    /**
     * 文章类型 1原创 2转载 3翻译
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 原文链接
     */
    @TableField(value = "original_url")
    private String originalUrl;

    /**
     * 是否置顶 0否 1是
     */
    @TableField(value = "is_top")
    private Integer isTop;

    /**
     * 是否删除  0否 1是
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 状态值 1公开 2私密 3评论可见
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

