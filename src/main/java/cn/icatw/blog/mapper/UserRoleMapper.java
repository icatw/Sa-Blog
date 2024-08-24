package cn.icatw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (UserRole)表数据库访问层
 *
 * @author icatw
 * @since 2024-03-21 11:22:22
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
    /**
     * 批量插入
     *
     * @param userRoleList 用户角色列表
     */
    void insertBatch(@Param("userRoleList") List<UserRole> userRoleList);
}

