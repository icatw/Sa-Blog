package cn.icatw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Menu)表数据库访问层
 *
 * @author icatw
 * @since 2024-03-22 09:11:44
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 按用户id列出菜单
     *
     * @param userId 用户Id
     * @return {@link List}<{@link Menu}>
     */
    List<Menu> listMenusByUserId(@Param("userId") String userId);
}

