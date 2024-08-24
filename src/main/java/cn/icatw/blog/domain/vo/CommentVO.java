package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 评论
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "评论")
public class CommentVO {

    /**
     * 回复用户id
     */
    @Schema(name = "replyUserId", description = "回复用户id")
    private Integer replyUserId;

    /**
     * 评论文章id
     */
    @Schema(name = "articleId", description = "文章id")
    private Integer articleId;

    /**
     * 评论说说id
     */
    @Schema(name = "talkId", description = "说说id")
    private Integer talkId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Schema(name = "commentContent", description = "评论内容")
    private String commentContent;

    /**
     * 父评论id
     */
    @Schema(name = "parentId", description = "评论父id")
    private Integer parentId;

    /**
     * 类型
     */
    @NotNull(message = "评论类型不能为空")
    @Schema(name = "type", description = "评论类型")
    private Integer type;

}
