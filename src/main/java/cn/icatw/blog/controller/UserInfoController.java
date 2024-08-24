package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.UserOnlineDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.*;
import cn.icatw.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.icatw.blog.constant.OptTypeConst.UPDATE;

/**
 * (UserInfo)表控制层
 *
 * @author icatw
 * @since 2024-03-21 11:31:54
 */
@RestController
@Tag(name = "用户信息模块", description = "用户信息模块")
public class UserInfoController {

    /**
     * 构造方法依赖注入
     */
    private final UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查看在线用户
     *
     * @param conditionVO 条件
     * @return {@link Result<UserOnlineDTO>} 在线用户列表
     */
    @Operation(summary = "查看在线用户", description = "查看在线用户")
    @GetMapping("/admin/users/online")
    public Result<PageResult<UserOnlineDTO>> listOnlineUsers(ConditionParams conditionVO) {
        return Result.ok(userService.listOnlineUsers(conditionVO));
    }

    /**
     * 下线用户
     *
     * @param userInfoId 用户信息
     * @return {@link Result<>}
     */
    @Operation(summary = "下线用户", description = "下线用户")
    @DeleteMapping("/admin/users/{userId}/online")
    public Result<?> removeOnlineUser(@PathVariable("userId") Integer userId) {
        userService.removeOnlineUser(userId);
        return Result.ok();
    }

    /**
     * 更新用户信息
     *
     * @param userInfoVO 用户信息
     * @return {@link Result <>}
     */
    @Operation(summary = "更新用户信息")
    @PutMapping("/users/info")
    public Result<?> updateUserInfo(@Valid @RequestBody UserInfoVO userInfoVO) {
        userService.updateUserInfo(userInfoVO);
        return Result.ok();
    }

    /**
     * 更新用户头像
     *
     * @param file 文件
     * @return {@link Result<String>} 头像地址
     */
    @Operation(summary = "更新用户头像")
    @PostMapping("/users/avatar")
    public Result<String> updateUserAvatar(MultipartFile file) {
        return Result.ok(userService.updateUserAvatar(file));
    }

    /**
     * 绑定用户邮箱
     *
     * @param emailVO 邮箱信息
     * @return {@link Result<>}
     */
    @Operation(summary = "绑定用户邮箱")
    @PostMapping("/users/email")
    public Result<?> saveUserEmail(@Valid @RequestBody EmailVO emailVO) {
        userService.saveUserEmail(emailVO);
        return Result.ok();
    }

    /**
     * 修改用户角色
     *
     * @param userRoleVO 用户角色信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @Operation(summary = "后台修改用户角色和昵称")
    @PutMapping("/admin/users/role")
    public Result<?> updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO) {
        userService.updateUserRole(userRoleVO);
        return Result.ok();
    }

    /**
     * 修改用户禁用状态
     *
     * @param userDisableVO 用户禁用信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @Operation(summary = "修改用户禁用状态")
    @PutMapping("/admin/users/disable")
    public Result<?> updateUserDisable(@Valid @RequestBody UserDisableVO userDisableVO) {
        userService.updateUserDisable(userDisableVO);
        return Result.ok();
    }
}

