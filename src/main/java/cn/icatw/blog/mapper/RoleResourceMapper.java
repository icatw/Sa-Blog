package cn.icatw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.RoleResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (RoleResource)表数据库访问层
 *
 * @author icatw
 * @since 2024-03-21 11:22:24
 */
public interface RoleResourceMapper extends BaseMapper<RoleResource> {
    /**
     * 批量插入
     *
     * @param roleResourceList 角色资源列表
     */
    void insertBatch(@Param("list") List<RoleResource> roleResourceList);
}

