package cn.icatw.blog.mapper;

import cn.icatw.blog.domain.dto.ResourceRoleDTO;
import cn.icatw.blog.domain.dto.RoleDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Role)表数据库访问层
 *
 * @author icatw
 * @since 2024-03-21 11:22:22
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 按用户信息id列出角色
     *
     * @param userInfoId 用户信息id
     * @return {@link List}<{@link String}>
     */
    List<String> listRolesByUserInfoId(@Param("userInfoId") String userInfoId);

    /**
     * 角色列表
     *
     * @param current 当前页
     * @param size    大小
     * @param params  params
     * @return {@link List}<{@link RoleDTO}>
     */
    List<RoleDTO> listRoles(@Param("current") Long current, @Param("size") Long size, @Param("params") ConditionParams params);

    /**
     * 列出资源角色
     *
     * @return {@link List}<{@link ResourceRoleDTO}>
     */
    List<ResourceRoleDTO> listResourceRoles();
}

