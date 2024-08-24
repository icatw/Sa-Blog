package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 分类VO
 *
 * @author 王顺
 * @date 2024/03/27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO {

    /**
     * id
     */
    @Schema(name = "id", description = "分类id", type = "Integer")
    private Integer id;

    /**
     * 分类名
     */
    @NotBlank(message = "分类名不能为空")
    @Schema(name = "categoryName", description = "分类名")
    private String categoryName;

}
