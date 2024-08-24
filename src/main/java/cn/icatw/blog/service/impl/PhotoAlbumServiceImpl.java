package cn.icatw.blog.service.impl;

import cn.icatw.blog.domain.entity.Photo;
import cn.icatw.blog.domain.entity.PhotoAlbum;
import cn.icatw.blog.domain.dto.PhotoAlbumBackDTO;
import cn.icatw.blog.domain.dto.PhotoAlbumDTO;
import cn.icatw.blog.mapper.PhotoAlbumMapper;
import cn.icatw.blog.mapper.PhotoMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.PhotoAlbumService;
import cn.icatw.blog.utils.AssertUtil;
import cn.icatw.blog.utils.BeanCopyUtil;
import cn.icatw.blog.utils.PageUtil;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.PhotoAlbumVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static cn.icatw.blog.constant.CommonConst.FALSE;
import static cn.icatw.blog.constant.CommonConst.TRUE;
import static cn.icatw.blog.enums.ArticleStatusEnum.PUBLIC;

/**
 * 相册(PhotoAlbum)表服务实现类
 *
 * @author icatw
 * @since 2024-04-07 10:21:59
 */
@Service("photoAlbumService")
public class PhotoAlbumServiceImpl extends ServiceImpl<PhotoAlbumMapper, PhotoAlbum> implements PhotoAlbumService {
    @Resource
    private PhotoMapper photoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdatePhotoAlbum(PhotoAlbumVO photoAlbumVO) {
        // 查询相册名是否存在
        PhotoAlbum album = baseMapper.selectOne(new LambdaQueryWrapper<PhotoAlbum>()
                .select(PhotoAlbum::getId)
                .eq(PhotoAlbum::getAlbumName, photoAlbumVO.getAlbumName()));
        AssertUtil.isTrue(Objects.isNull(album) || !album.getId().equals(photoAlbumVO.getId()), "相册名已存在");
        PhotoAlbum photoAlbum = BeanCopyUtil.copyProperties(photoAlbumVO, PhotoAlbum.class);
        this.saveOrUpdate(photoAlbum);
    }


    @Override
    public PageResult<PhotoAlbumBackDTO> listPhotoAlbumBacks(ConditionParams condition) {
        // 查询相册数量
        long count = baseMapper.selectCount(new LambdaQueryWrapper<PhotoAlbum>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), PhotoAlbum::getAlbumName, condition.getKeywords())
                .eq(PhotoAlbum::getIsDelete, FALSE));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询相册信息
        List<PhotoAlbumBackDTO> photoAlbumBackList = baseMapper.listPhotoAlbumBacks(PageUtil.getLimitCurrent(), PageUtil.getSize(), condition);
        return new PageResult<>(photoAlbumBackList, (int) count);
    }

    @Override
    public List<PhotoAlbumDTO> listPhotoAlbumBackInfos() {
        List<PhotoAlbum> photoAlbumList = baseMapper.selectList(new LambdaQueryWrapper<PhotoAlbum>()
                .eq(PhotoAlbum::getIsDelete, FALSE));
        return BeanCopyUtil.copyListToList(photoAlbumList, PhotoAlbumDTO.class);
    }

    @Override
    public PhotoAlbumBackDTO getPhotoAlbumBackById(Integer albumId) {
        // 查询相册信息
        PhotoAlbum photoAlbum = baseMapper.selectById(albumId);
        // 查询照片数量
        long photoCount = photoMapper.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId)
                .eq(Photo::getIsDelete, FALSE));
        PhotoAlbumBackDTO album = BeanCopyUtil.copyProperties(photoAlbum, PhotoAlbumBackDTO.class);
        album.setPhotoCount((int) photoCount);
        return album;
    }

    @Override
    public void deletePhotoAlbumById(Integer albumId) {
        // 查询照片数量
        long count = photoMapper.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId));
        if (count > 0) {
            // 若相册下存在照片则逻辑删除相册和照片
            baseMapper.updateById(PhotoAlbum.builder()
                    .id(albumId)
                    .isDelete(TRUE)
                    .build());
            photoMapper.update(new Photo(), new LambdaUpdateWrapper<Photo>()
                    .set(Photo::getIsDelete, TRUE)
                    .eq(Photo::getAlbumId, albumId));
        } else {
            // 若相册下不存在照片则直接删除
            baseMapper.deleteById(albumId);
        }
    }

    @Override
    public List<PhotoAlbumDTO> listPhotoAlbums() {
        // 查询相册列表
        List<PhotoAlbum> photoAlbumList = baseMapper.selectList(new LambdaQueryWrapper<PhotoAlbum>()
                .eq(PhotoAlbum::getStatus, PUBLIC.getStatus())
                .eq(PhotoAlbum::getIsDelete, FALSE)
                .orderByDesc(PhotoAlbum::getId));
        return BeanCopyUtil.copyToList(photoAlbumList, PhotoAlbumDTO.class);
    }
}

