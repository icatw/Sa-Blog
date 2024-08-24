package cn.icatw.blog.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 文章
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVO {

    /**
     * 文章id
     */
    @Schema(name = "id", description = "文章id")
    private Integer id;

    /**
     * 标题
     */
    @NotBlank(message = "文章标题不能为空")
    @Schema(name = "articleTitle", description = "文章标题")
    private String articleTitle;

    /**
     * 内容
     */
    @NotBlank(message = "文章内容不能为空")
    @Schema(name = "articleContent", description = "文章内容")
    private String articleContent;

    /**
     * 文章封面
     */
    @Schema(name = "articleCover", description = "文章封面")
    private String articleCover;

    /**
     * 文章分类
     */
    @Schema(name = "categoryName", description = "文章分类")
    private String categoryName;

    /**
     * 文章标签
     */
    @Schema(name = "tagNameList", description = "文章标签")
    private List<String> tagNameList;

    /**
     * 文章类型
     */
    @Schema(name = "type", description = "文章类型")
    private Integer type;

    /**
     * 原文链接
     */
    @Schema(name = "originalUrl", description = "原文链接")
    private String originalUrl;

    /**
     * 是否置顶
     */
    @Schema(name = "isTop", description = "是否置顶")
    private Integer isTop;

    /**
     * 文章状态 1.公开 2.私密 3.评论可见
     */
    @Schema(name = "status", description = "文章状态 1.公开 2.私密 3.评论可见")
    private Integer status;

}
