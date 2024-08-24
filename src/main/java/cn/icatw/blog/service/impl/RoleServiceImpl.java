package cn.icatw.blog.service.impl;

import cn.icatw.blog.constant.CommonConst;
import cn.icatw.blog.domain.entity.Role;
import cn.icatw.blog.domain.entity.RoleMenu;
import cn.icatw.blog.domain.entity.RoleResource;
import cn.icatw.blog.domain.entity.UserRole;
import cn.icatw.blog.domain.dto.RoleDTO;
import cn.icatw.blog.domain.dto.UserRoleDTO;
import cn.icatw.blog.mapper.RoleMapper;
import cn.icatw.blog.mapper.RoleMenuMapper;
import cn.icatw.blog.mapper.RoleResourceMapper;
import cn.icatw.blog.mapper.UserRoleMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.RoleService;
import cn.icatw.blog.strategy.MySourceSafilterAuthStrategy;
import cn.icatw.blog.utils.AssertUtil;
import cn.icatw.blog.utils.BeanCopyUtil;
import cn.icatw.blog.utils.PageUtil;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.RoleVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * (Role)表服务实现类
 *
 * @author icatw
 * @date 2024/04/02
 * @since 2024-03-21 11:22:22
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    RoleMenuMapper roleMenuMapper;
    @Resource
    RoleResourceMapper roleResourceMapper;
    @Resource
    UserRoleMapper userRoleMapper;
    @Resource
    MySourceSafilterAuthStrategy mySourceSafilterAuthStrategy;

    /**
     * 按用户信息id列出角色
     *
     * @param userInfoId 用户信息id
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> listRolesByUserInfoId(String userInfoId) {
        return baseMapper.listRolesByUserInfoId(userInfoId);
    }

    /**
     * 列出用户角色
     *
     * @return {@link List}<{@link UserRoleDTO}>
     */
    @Override
    public List<UserRoleDTO> listUserRoles() {
        // 查询角色列表
        List<Role> roleList = baseMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName));
        return BeanCopyUtil.copyListToList(roleList, UserRoleDTO.class);
    }

    /**
     * 列出角色
     *
     * @param conditionVO 条件vo
     * @return {@link PageResult}<{@link RoleDTO}>
     */
    @Override
    public PageResult<RoleDTO> listRoles(ConditionParams conditionVO) {
        // 查询角色列表
        List<RoleDTO> roleDTOList = baseMapper.listRoles(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionVO);
        // 查询总量
        long count = baseMapper.selectCount(new LambdaQueryWrapper<Role>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Role::getRoleName, conditionVO.getKeywords()));
        return new PageResult<>(roleDTOList, (int) count);
    }

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色vo
     */
    @Override
    public void saveOrUpdateRole(RoleVO roleVO) {
        // 判断角色名重复
        Role existRole = baseMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getRoleName, roleVO.getRoleName()));
        AssertUtil.isFalse(Objects.nonNull(existRole) && !existRole.getId().equals(roleVO.getId()), "角色名已存在");
        // 保存或更新角色信息
        Role role = Role.builder()
                .id(roleVO.getId())
                .roleName(roleVO.getRoleName())
                .roleLabel(roleVO.getRoleLabel())
                .isDisable(CommonConst.FALSE)
                .build();
        this.saveOrUpdate(role);
        // 更新角色资源关系
        if (CollectionUtils.isNotEmpty(roleVO.getResourceIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleResourceMapper.delete(new LambdaQueryWrapper<RoleResource>()
                        .eq(RoleResource::getRoleId, roleVO.getId()));
            }
            roleVO.getResourceIdList()
                    .forEach(item -> {
                        RoleResource roleResource = RoleResource.builder()
                                .roleId(role.getId())
                                .resourceId(item)
                                .build();
                        roleResourceMapper.insert(roleResource);
                    });
            // 重新加载角色资源信息
            mySourceSafilterAuthStrategy.clearDataSource();
        }
        // 更新角色菜单关系
        if (CollectionUtils.isNotEmpty(roleVO.getMenuIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleVO.getId()));
            }
            roleVO.getMenuIdList()
                    .forEach(menuId -> {
                        RoleMenu roleMenu = RoleMenu.builder()
                                .roleId(role.getId())
                                .menuId(menuId)
                                .build();
                        roleMenuMapper.insert(roleMenu);
                    });
        }
    }

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     */
    @Override
    public void deleteRoles(List<Integer> roleIdList) {
        // 判断角色下是否有用户
        long count = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getRoleId, roleIdList));
        AssertUtil.isTrue(count == 0, "该角色下存在用户，禁止删除！");
        baseMapper.deleteBatchIds(roleIdList);
    }
}

