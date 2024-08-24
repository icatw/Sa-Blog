package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 资源
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "资源")
public class ResourceVO {

    /**
     * 资源id
     */
    @Schema(name = "id", description = "资源id")
    private Integer id;

    /**
     * 资源名
     */
    @NotBlank(message = "资源名不能为空")
    @Schema(name = "resourceName", description = "资源名")
    private String resourceName;

    /**
     * 路径
     */
    @Schema(name = "url", description = "资源路径")
    private String url;

    /**
     * 请求方式
     */
    @Schema(name = "url", description = "资源路径")
    private String requestMethod;

    /**
     * 父资源id
     */
    @Schema(name = "parentId", description = "父资源id")
    private Integer parentId;

    /**
     * 是否匿名访问
     */
    @Schema(name = "isAnonymous", description = "是否匿名访问")
    private Integer isAnonymous;

}
