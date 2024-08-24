package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 照片
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "照片信息")
public class PhotoInfoVO {

    /**
     * 照片id
     */
    @NotNull(message = "照片id不能为空")
    @Schema(name = "id", description = "照片id")
    private Integer id;

    /**
     * 照片名
     */
    @NotBlank(message = "照片名不能为空")
    @Schema(name = "photoName", description = "照片名")
    private String photoName;

    /**
     * 照片描述
     */
    @Schema(name = "photoDesc", description = "照片描述")
    private String photoDesc;

}
