package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 标签VO
 *
 * @author 王顺
 * @date 2024/03/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagVO {

    /**
     * id
     */
    @Schema(name = "id", description = "标签id")
    private Integer id;

    /**
     * 标签名
     */
    @NotBlank(message = "标签名不能为空")
    @Schema(name = "categoryName", description = "标签名")
    private String tagName;

}
