package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.Menu;
import cn.icatw.blog.domain.dto.LabelOptionDTO;
import cn.icatw.blog.domain.dto.MenuDTO;
import cn.icatw.blog.domain.dto.UserMenuDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.MenuVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Menu)表服务接口
 *
 * @author icatw
 * @since 2024-03-22 09:11:44
 */
public interface MenuService extends IService<Menu> {
    /**
     * 列出用户菜单
     *
     * @return {@link List}<{@link UserMenuDTO}>
     */
    List<UserMenuDTO> listUserMenus();

    /**
     * 菜单列表list
     *
     * @param conditionParams 条件参数
     * @return {@link List}<{@link MenuDTO}>
     */
    List<MenuDTO> listMenus(ConditionParams conditionParams);

    /**
     * 保存或更新菜单
     *
     * @param menuVO 菜单vo
     */
    void saveOrUpdateMenu(MenuVO menuVO);

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    void deleteMenu(Integer menuId);

    /**
     * 查看角色菜单选项
     *
     * @return {@link List}<{@link LabelOptionDTO}>
     */
    List<LabelOptionDTO> listMenuOptions();
}

