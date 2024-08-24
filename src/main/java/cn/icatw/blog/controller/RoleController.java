package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.RoleDTO;
import cn.icatw.blog.domain.dto.UserRoleDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.RoleService;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.icatw.blog.constant.OptTypeConst.REMOVE;
import static cn.icatw.blog.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * (Role)表控制层
 *
 * @author icatw
 * @since 2024-03-21 11:31:55
 */
@RestController
@Tag(name = "角色模块", description = "角色模块")
public class RoleController {

    /**
     * 构造方法依赖注入
     */
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 查询用户角色选项
     *
     * @return {@link Result<UserRoleDTO>} 用户角色选项
     */
    @Operation(summary = "查询用户角色选项")
    @GetMapping("/admin/users/role")
    public Result<List<UserRoleDTO>> listUserRoles() {
        return Result.ok(roleService.listUserRoles());
    }

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件
     * @return {@link Result<RoleDTO>} 角色列表
     */
    @Operation(summary = "查询角色列表")
    @GetMapping("/admin/roles")
    public Result<PageResult<RoleDTO>> listRoles(ConditionParams conditionVO) {
        return Result.ok(roleService.listRoles(conditionVO));
    }

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @Operation(summary = "保存或更新角色")
    @PostMapping("/admin/role")
    public Result<?> saveOrUpdateRole(@RequestBody @Valid RoleVO roleVO) {
        roleService.saveOrUpdateRole(roleVO);
        return Result.ok();
    }

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @Operation(summary = "删除角色")
    @DeleteMapping("/admin/roles")
    public Result<?> deleteRoles(@RequestBody List<Integer> roleIdList) {
        roleService.deleteRoles(roleIdList);
        return Result.ok();
    }

}

