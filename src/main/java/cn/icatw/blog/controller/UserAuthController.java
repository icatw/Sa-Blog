package cn.icatw.blog.controller;

import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.UserBackDTO;
import cn.icatw.blog.domain.params.UserConditionParams;
import cn.icatw.blog.service.UserService;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.PasswordVO;
import cn.icatw.blog.domain.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * (UserAuth)表控制层
 *
 * @author icatw
 * @since 2024-03-21 10:17:20
 */
@RestController
@Tag(name = "用户账号认证模块", description = "用户账号认证模块")
public class UserAuthController {

    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return {@link Result<UserBackDTO>} 用户列表
     */
    @Operation(summary = "查询后台用户列表", description = "查询后台用户列表")
    @GetMapping("/admin/users")
    public Result<PageResult<UserBackDTO>> listUsers(UserConditionParams condition) {
        return Result.ok(userService.listUserBackDTO(condition));
    }

    /**
     * 发送邮箱验证码
     *
     * @param username 用户名
     * @return {@link Result<>}
     */
    @Operation(summary = "发送邮箱验证码")
    @GetMapping("/users/code")
    public Result<?> sendCode(String username) {
        userService.sendCode(username);
        return Result.ok();
    }

    /**
     * 修改密码
     *
     * @param user 用户信息
     * @return {@link Result<>}
     */
    @Operation(summary = "修改密码")
    @PutMapping("/users/password")
    public Result<?> updatePassword(@Valid @RequestBody UserVO user) {
        userService.updatePassword(user);
        return Result.ok();
    }

    /**
     * 修改管理员密码
     *
     * @param passwordVO 密码信息
     * @return {@link Result<>}
     */
    @Operation(summary = "修改管理员密码")
    @PutMapping("/admin/users/password")
    public Result<?> updateAdminPassword(@Valid @RequestBody PasswordVO passwordVO) {
        userService.updateAdminPassword(passwordVO);
        return Result.ok();
    }
}

