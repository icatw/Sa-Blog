package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 密码
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "密码")
public class PasswordVO {

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    @Schema(name = "oldPassword", description = "旧密码")
    private String oldPassword;

    /**
     * 新密码
     */
    @Size(min = 6, message = "新密码不能少于6位")
    @NotBlank(message = "新密码不能为空")
    @Schema(name = "newPassword", description = "新密码")
    private String newPassword;
}
