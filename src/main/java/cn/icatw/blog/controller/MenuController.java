package cn.icatw.blog.controller;

import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.LabelOptionDTO;
import cn.icatw.blog.domain.dto.MenuDTO;
import cn.icatw.blog.domain.dto.UserMenuDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.MenuService;
import cn.icatw.blog.domain.vo.MenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Menu)表控制层
 *
 * @author icatw
 * @since 2024-03-22 09:11:44
 */
@RestController
@Tag(name = "菜单模块", description = "菜单模块")
public class MenuController {

    /**
     * 构造方法依赖注入
     */
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    /**
     * 用户菜单列表
     *
     * @return {@link Result}<{@link List}<{@link UserMenuDTO}>>
     */
    @GetMapping("/admin/user/menus")
    @Operation(summary = "用户菜单列表", description = "获取当前登录用户菜单列表")
    public Result<List<UserMenuDTO>> listUserMenus() {
        return Result.ok(menuService.listUserMenus());
    }

    /**
     * 查询菜单列表
     *
     * @param conditionVO 条件
     * @return {@link Result<MenuDTO>} 菜单列表
     */
    @Operation(summary = "查看菜单列表")
    @GetMapping("/admin/menus")
    public Result<List<MenuDTO>> listMenus(ConditionParams conditionVO) {
        return Result.ok(menuService.listMenus(conditionVO));
    }
    /**
     * 新增或修改菜单
     *
     * @param menuVO 菜单
     * @return {@link Result<>}
     */
    @Operation(summary = "新增或修改菜单")
    @PostMapping("/admin/menus")
    public Result<?> saveOrUpdateMenu(@Valid @RequestBody MenuVO menuVO) {
        menuService.saveOrUpdateMenu(menuVO);
        return Result.ok();
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return {@link Result<>}
     */
    @Operation(summary = "删除菜单")
    @DeleteMapping("/admin/menus/{menuId}")
    public Result<?> deleteMenu(@PathVariable("menuId") Integer menuId){
        menuService.deleteMenu(menuId);
        return Result.ok();
    }

    /**
     * 查看角色菜单选项
     *
     * @return {@link Result< LabelOptionDTO >} 查看角色菜单选项
     */
    @Operation(summary = "查看角色菜单选项")
    @GetMapping("/admin/role/menus")
    public Result<List<LabelOptionDTO>> listMenuOptions() {
        return Result.ok(menuService.listMenuOptions());
    }


}

