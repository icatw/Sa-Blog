package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 说说
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "说说对象")
public class TalkVO {

    /**
     * 说说id
     */
    @Schema(name = "id", description = "说说id")
    private Integer id;

    /**
     * 说说内容
     */
    @Schema(name = "content", description = "说说内容")
    @NotBlank(message = "说说内容不能为空")
    private String content;

    /**
     * 图片
     */
    @Schema(name = "images", description = "说说图片")
    private String images;

    /**
     * 是否置顶
     */
    @Schema(name = "isTop", description = "置顶状态")
    @NotNull(message = "置顶状态不能为空")
    private Integer isTop;

    /**
     * 说说状态 1.公开 2.私密
     */
    @Schema(name = "status", description = "说说状态")
    @NotNull(message = "说说状态不能为空")
    private Integer status;

}
