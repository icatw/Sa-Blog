package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户角色vo
 *
 * @author xiaojie
 * @date 2021/08/03
 * @since 2020-05-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户权限")
public class UserRoleVO {
    /**
     * 用户id
     */
    @NotNull(message = "id不能为空")
    @Schema(name = "userId", description = "用户id")
    private Integer userId;

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Schema(name = "nickname", description = "昵称")
    private String nickname;

    /**
     * 用户角色
     */
    @NotNull(message = "用户角色不能为空")
    @Schema(name = "roleList", description = "角色id集合")
    private List<Integer> roleIdList;

}
