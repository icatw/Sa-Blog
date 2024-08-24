package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色
 *
 * @author 王顺
 * @date 2024/04/02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "角色")
public class RoleVO {

    /**
     * id
     */
    @Schema(name = "id", description = "用户id")
    private Integer id;

    /**
     * 标签名
     */
    @NotBlank(message = "角色名不能为空")
    @Schema(name = "roleName", description = "角色名")
    private String roleName;

    /**
     * 标签名
     */
    @NotBlank(message = "权限标签不能为空")
    @Schema(name = "categoryName", description = "标签名")
    private String roleLabel;

    /**
     * 资源列表
     */
    @Schema(name = "resourceIdList", description = "资源列表")
    private List<Integer> resourceIdList;

    /**
     * 菜单列表
     */
    @Schema(name = "menuIdList", description = "菜单列表")
    private List<Integer> menuIdList;

}
