package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.Role;
import cn.icatw.blog.domain.dto.RoleDTO;
import cn.icatw.blog.domain.dto.UserRoleDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.RoleVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Role)表服务接口
 *
 * @author icatw
 * @date 2024/04/02
 * @since 2024-03-21 11:22:22
 */
public interface RoleService extends IService<Role> {
    /**
     * 按用户信息id列出角色
     *
     * @param userInfoId 用户信息id
     * @return {@link List}<{@link String}>
     */
    List<String> listRolesByUserInfoId(String userInfoId);

    /**
     * 列出用户角色
     *
     * @return {@link List}<{@link UserRoleDTO}>
     */
    List<UserRoleDTO> listUserRoles();

    /**
     * 列出角色
     *
     * @param conditionVO 条件vo
     * @return {@link PageResult}<{@link RoleDTO}>
     */
    PageResult<RoleDTO> listRoles(ConditionParams conditionVO);

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色vo
     */
    void saveOrUpdateRole(RoleVO roleVO);

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     */
    void deleteRoles(List<Integer> roleIdList);
}

