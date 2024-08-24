package cn.icatw.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.icatw.blog.domain.entity.Menu;
import cn.icatw.blog.domain.entity.RoleMenu;
import cn.icatw.blog.domain.dto.LabelOptionDTO;
import cn.icatw.blog.domain.dto.MenuDTO;
import cn.icatw.blog.domain.dto.UserMenuDTO;
import cn.icatw.blog.mapper.MenuMapper;
import cn.icatw.blog.mapper.RoleMenuMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.MenuService;
import cn.icatw.blog.utils.AssertUtil;
import cn.icatw.blog.utils.BeanCopyUtil;
import cn.icatw.blog.utils.UserUtil;
import cn.icatw.blog.domain.vo.MenuVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.icatw.blog.constant.CommonConst.COMPONENT;
import static cn.icatw.blog.constant.CommonConst.TRUE;

/**
 * (Menu)表服务实现类
 *
 * @author icatw
 * @date 2024/04/02
 * @since 2024-03-22 09:11:44
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Resource
    private RoleMenuMapper roleMenuMapper;

    /**
     * 列出用户菜单
     *
     * @return {@link List}<{@link UserMenuDTO}>
     */
    @Override
    public List<UserMenuDTO> listUserMenus() {
        // 查询用户菜单信息
        List<Menu> menuList = baseMapper.listMenusByUserId(String.valueOf(UserUtil.getCurrentUser().getUserId()));
        // 获取目录列表
        List<Menu> catalogList = filterCatalogs(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = groupMenusByParentId(menuList);
        // 转换前端菜单格式
        return convertToUserMenuList(catalogList, childrenMap);
    }

    /**
     * 后台菜单列表
     *
     * @param params 条件参数
     * @return {@link List}<{@link MenuDTO}>
     */
    @Override
    public List<MenuDTO> listMenus(ConditionParams params) {
        //查询全部菜单
        List<Menu> menuList = baseMapper.selectList(new LambdaQueryWrapper<Menu>()
                .like(StrUtil.isNotBlank(params.getKeywords()), Menu::getName, params.getKeywords()));
        //获取一级目录列表
        List<Menu> catalogList = filterCatalogs(menuList);
        //获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = groupMenusByParentId(menuList);
        //封装目录菜单数据
        List<MenuDTO> menuDTOList = new ArrayList<>(catalogList.stream().map(item -> {
            MenuDTO menuDTO = BeanUtil.copyProperties(item, MenuDTO.class);
            //    获取目录下的菜单排序
            List<MenuDTO> menuDTOS = BeanCopyUtil.copyListToList(childrenMap.get(item.getId()), MenuDTO.class);
            List<MenuDTO> list = menuDTOS.stream()
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .toList();
            menuDTO.setChildren(list);
            // 移除已处理的父菜单 ID
            childrenMap.remove(item.getId());
            return menuDTO;
        }).sorted(Comparator.comparing(MenuDTO::getOrderNum).thenComparing(MenuDTO::getCreateTime)).toList());
        //若还有菜单未取出则拼接
        if (CollUtil.isNotEmpty(childrenMap)) {
            List<Menu> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<MenuDTO> childrenDTOList = childrenList.stream()
                    .map(item -> BeanUtil.copyProperties(item, MenuDTO.class))
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum).thenComparing(MenuDTO::getCreateTime)).toList();
            menuDTOList.addAll(childrenDTOList);
        }
        return menuDTOList;
    }

    /**
     * 保存或更新菜单
     *
     * @param menuVO 菜单vo
     */
    @Override
    public void saveOrUpdateMenu(MenuVO menuVO) {
        Menu menu = BeanCopyUtil.copyProperties(menuVO, Menu.class);
        this.saveOrUpdate(menu);
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    @Override
    public void deleteMenu(Integer menuId) {
        // 查询是否有角色关联菜单
        Long count = roleMenuMapper.selectCount(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getMenuId, menuId));
        AssertUtil.isTrue(count == 0, "该菜单下有角色关联，无法删除");
        // 查询子菜单
        List<Integer> menuIdList = baseMapper.selectList(new LambdaQueryWrapper<Menu>()
                        .select(Menu::getId)
                        .eq(Menu::getParentId, menuId))
                .stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
        menuIdList.add(menuId);
        baseMapper.deleteBatchIds(menuIdList);
    }

    /**
     * 查询角色菜单选项
     *
     * @return {@link List}<{@link LabelOptionDTO}>
     */
    @Override
    public List<LabelOptionDTO> listMenuOptions() {
        // 查询菜单数据
        List<Menu> menuList = baseMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getName, Menu::getParentId, Menu::getOrderNum));
        // 获取目录列表
        List<Menu> catalogList = filterCatalogs(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = groupMenusByParentId(menuList);
        // 组装目录菜单数据
        return catalogList.stream().map(item -> {
            // 获取目录下的菜单排序
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Menu> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> LabelOptionDTO.builder()
                                .id(menu.getId())
                                .label(menu.getName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder()
                    .id(item.getId())
                    .label(item.getName())
                    .children(list)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 过滤一级目录
     *
     * @param menuList 菜单列表
     * @return {@link List}<{@link Menu}>
     */
    private List<Menu> filterCatalogs(List<Menu> menuList) {
        return menuList.stream()
                .filter(menu -> menu.getParentId() == null)
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());
    }

    /**
     * 按父id分组菜单
     *
     * @param menuList 菜单列表
     * @return {@link Map}<{@link Integer}, {@link List}<{@link Menu}>>
     */
    private Map<Integer, List<Menu>> groupMenusByParentId(List<Menu> menuList) {
        return menuList.stream()
                .filter(menu -> menu.getParentId() != null)
                .collect(Collectors.groupingBy(Menu::getParentId));
    }

    /**
     * 转换为用户菜单列表
     *
     * @param catalogList 一级目录列表
     * @param childrenMap 子菜单
     * @return {@link List}<{@link UserMenuDTO}>
     */
    private List<UserMenuDTO> convertToUserMenuList(List<Menu> catalogList, Map<Integer, List<Menu>> childrenMap) {
        return catalogList.stream()
                .map(catalog -> {
                    UserMenuDTO userMenuDTO = new UserMenuDTO();
                    List<UserMenuDTO> list = new ArrayList<>();
                    //目录下的子菜单
                    List<Menu> children = childrenMap.get(catalog.getId());
                    //多级菜单
                    if (CollUtil.isNotEmpty(children)) {
                        BeanUtil.copyProperties(catalog, userMenuDTO);
                        list = children.stream().sorted(Comparator.comparing(Menu::getOrderNum))
                                .map(menu -> {
                                    UserMenuDTO dto = BeanUtil.copyProperties(menu, UserMenuDTO.class);
                                    dto.setHidden(menu.getIsHidden() == 1);
                                    return dto;
                                }).toList();
                    } else {
                        //一级菜单，构建个空目录
                        userMenuDTO.setPath(catalog.getPath());
                        userMenuDTO.setComponent(COMPONENT);
                        list.add(UserMenuDTO.builder()
                                .path("")
                                .name(catalog.getName())
                                .icon(catalog.getIcon())
                                .component(catalog.getComponent())
                                .build());
                    }
                    userMenuDTO.setHidden(catalog.getIsHidden() == TRUE);
                    userMenuDTO.setChildren(list);
                    return userMenuDTO;
                })
                .collect(Collectors.toList());
    }


}

