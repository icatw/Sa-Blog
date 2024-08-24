package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.PhotoBackDTO;
import cn.icatw.blog.domain.dto.PhotoDTO;
import cn.icatw.blog.domain.params.PhotoConditionParams;
import cn.icatw.blog.service.PhotoService;
import cn.icatw.blog.domain.vo.DeleteVO;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.PhotoInfoVO;
import cn.icatw.blog.domain.vo.PhotoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.icatw.blog.constant.OptTypeConst.*;

/**
 * 照片(Photo)表控制层
 *
 * @author icatw
 * @since 2024-04-07 10:21:58
 */
@RestController
@Tag(name = "照片模块", description = "照片模块")
public class PhotoController {

    /**
     * 构造方法依赖注入
     */
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    /**
     * 获取后台照片列表
     *
     * @param condition 条件
     * @return {@link Result<PhotoBackDTO>} 照片列表
     */
    @Operation(summary = "根据相册id获取照片列表")
    @GetMapping("/admin/photos")
    public Result<PageResult<PhotoBackDTO>> listPhotos(PhotoConditionParams condition) {
        return Result.ok(photoService.listPhotos(condition));
    }

    /**
     * 更新照片信息
     *
     * @param photoInfoVO 照片信息
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @Operation(summary = "更新照片信息")
    @PutMapping("/admin/photos")
    public Result<?> updatePhoto(@Valid @RequestBody PhotoInfoVO photoInfoVO) {
        photoService.updatePhoto(photoInfoVO);
        return Result.ok();
    }

    /**
     * 保存照片
     *
     * @param photoVO 照片
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE)
    @Operation(summary = "保存照片")
    @PostMapping("/admin/photos")
    public Result<?> savePhotos(@Valid @RequestBody PhotoVO photoVO) {
        photoService.savePhotos(photoVO);
        return Result.ok();
    }

    /**
     * 移动照片相册
     *
     * @param photoVO 照片信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @Operation(summary = "移动照片相册")
    @PutMapping("/admin/photos/album")
    public Result<?> updatePhotosAlbum(@Valid @RequestBody PhotoVO photoVO) {
        photoService.updatePhotosAlbum(photoVO);
        return Result.ok();
    }

    /**
     * 更新照片删除状态
     *
     * @param deleteVO 删除信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @Operation(summary = "更新照片删除状态")
    @PutMapping("/admin/photos/delete")
    public Result<?> updatePhotoDelete(@Valid @RequestBody DeleteVO deleteVO) {
        photoService.updatePhotoDelete(deleteVO);
        return Result.ok();
    }

    /**
     * 删除照片
     *
     * @param photoIdList 照片id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @Operation(summary = "删除照片")
    @DeleteMapping("/admin/photos")
    public Result<?> deletePhotos(@RequestBody List<Integer> photoIdList) {
        photoService.deletePhotos(photoIdList);
        return Result.ok();
    }

    /**
     * 根据相册id查看照片列表
     *
     * @param albumId 相册id
     * @return {@link Result<PhotoDTO>} 照片列表
     */
    @Operation(summary = "根据相册id查看照片列表")
    @GetMapping("/albums/{albumId}/photos")
    public Result<PhotoDTO> listPhotosByAlbumId(@PathVariable("albumId") Integer albumId) {
        return Result.ok(photoService.listPhotosByAlbumId(albumId));
    }
}

