package cn.icatw.blog.mapper;

import cn.icatw.blog.domain.dto.PhotoAlbumBackDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.PhotoAlbum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 相册(PhotoAlbum)表数据库访问层
 *
 * @author icatw
 * @since 2024-04-07 10:21:58
 */
public interface PhotoAlbumMapper extends BaseMapper<PhotoAlbum> {
    /**
     * 查看后台相册列表
     *
     * @param current   当前页码
     * @param size      大小
     * @param condition 条件
     * @return {@link List}<{@link PhotoAlbumBackDTO}>
     */
    List<PhotoAlbumBackDTO> listPhotoAlbumBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionParams condition);
}

