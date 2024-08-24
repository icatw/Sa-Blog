package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.PhotoAlbum;
import cn.icatw.blog.domain.dto.PhotoAlbumBackDTO;
import cn.icatw.blog.domain.dto.PhotoAlbumDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.PhotoAlbumVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 相册(PhotoAlbum)表服务接口
 *
 * @author icatw
 * @since 2024-04-07 10:21:59
 */
public interface PhotoAlbumService extends IService<PhotoAlbum> {
    /**
     * 保存或更新相册
     *
     * @param photoAlbumVO 相册vo
     */
    void saveOrUpdatePhotoAlbum(PhotoAlbumVO photoAlbumVO);

    /**
     * 查看后台相册列表
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link PhotoAlbumBackDTO}>
     */
    PageResult<PhotoAlbumBackDTO> listPhotoAlbumBacks(ConditionParams condition);

    /**
     * 获取后台相册列表信息
     *
     * @return {@link List}<{@link PhotoAlbumDTO}>
     */
    List<PhotoAlbumDTO> listPhotoAlbumBackInfos();

    /**
     * 根据id获取后台相册信息
     *
     * @param albumId 相册id
     * @return {@link PhotoAlbumBackDTO}
     */
    PhotoAlbumBackDTO getPhotoAlbumBackById(Integer albumId);

    /**
     * 按id删除相册
     *
     * @param albumId 相册id
     */
    void deletePhotoAlbumById(Integer albumId);

    /**
     * 相册列表
     *
     * @return {@link List}<{@link PhotoAlbumDTO}>
     */
    List<PhotoAlbumDTO> listPhotoAlbums();
}

