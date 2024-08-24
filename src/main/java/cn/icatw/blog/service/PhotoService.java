package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.Photo;
import cn.icatw.blog.domain.dto.PhotoBackDTO;
import cn.icatw.blog.domain.dto.PhotoDTO;
import cn.icatw.blog.domain.params.PhotoConditionParams;
import cn.icatw.blog.domain.vo.DeleteVO;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.PhotoInfoVO;
import cn.icatw.blog.domain.vo.PhotoVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 照片(Photo)表服务接口
 *
 * @author icatw
 * @since 2024-04-07 10:21:58
 */
public interface PhotoService extends IService<Photo> {
    /**
     * 相册id获取照片列表
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link PhotoBackDTO}>
     */
    PageResult<PhotoBackDTO> listPhotos(PhotoConditionParams condition);

    /**
     * 更新照片
     *
     * @param photoInfoVO 照片信息vo
     */
    void updatePhoto(PhotoInfoVO photoInfoVO);

    /**
     * 保存照片
     *
     * @param photoVO 照片vo
     */
    void savePhotos(PhotoVO photoVO);

    /**
     * 更新相册
     *
     * @param photoVO 照片vo
     */
    void updatePhotosAlbum(PhotoVO photoVO);

    /**
     * 更新照片删除状态
     *
     * @param deleteVO 删除vo
     */
    void updatePhotoDelete(DeleteVO deleteVO);

    /**
     * 删除照片
     *
     * @param photoIdList 照片id列表
     */
    void deletePhotos(List<Integer> photoIdList);

    /**
     * 按相册id列出照片
     *
     * @param albumId 相册id
     * @return {@link PhotoDTO}
     */
    PhotoDTO listPhotosByAlbumId(Integer albumId);
}

