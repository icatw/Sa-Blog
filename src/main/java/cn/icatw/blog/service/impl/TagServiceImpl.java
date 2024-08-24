package cn.icatw.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.icatw.blog.domain.entity.ArticleTag;
import cn.icatw.blog.domain.entity.Tag;
import cn.icatw.blog.domain.dto.TagBackDTO;
import cn.icatw.blog.domain.dto.TagDTO;
import cn.icatw.blog.mapper.ArticleTagMapper;
import cn.icatw.blog.mapper.TagMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.TagService;
import cn.icatw.blog.utils.AssertUtil;
import cn.icatw.blog.utils.PageUtil;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.TagVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * (Tag)表服务实现类
 *
 * @author icatw
 * @date 2024/03/28
 * @since 2024-03-22 20:54:59
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Resource
    ArticleTagMapper articleTagMapper;

    /**
     * 搜索标签
     *
     * @param params params
     * @return {@link List}<{@link TagDTO}>
     */
    @Override
    public List<TagDTO> listTagsBySearch(ConditionParams params) {
        return baseMapper.listTagsBySearch(params);
    }

    /**
     * 后台标签列表DTO
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link TagBackDTO}>
     */
    @Override
    public PageResult<TagBackDTO> listTagBackDTO(ConditionParams condition) {
        // 查询标签数量
        long count = baseMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Tag::getTagName, condition.getKeywords()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询标签列表
        List<TagBackDTO> tagList = baseMapper.listTagBackDTO(PageUtil.getLimitCurrent(), PageUtil.getSize(), condition);
        return new PageResult<>(tagList, (int) count);
    }

    /**
     * 标签列表
     *
     * @return {@link PageResult}<{@link TagDTO}>
     */
    @Override
    public PageResult<TagDTO> listTags() {
        // 查询标签列表
        List<Tag> tagList = baseMapper.selectList(null);
        // 转换DTO
        List<TagDTO> tagDTOList = BeanUtil.copyToList(tagList, TagDTO.class);
        // 查询标签数量
        long count = baseMapper.selectCount(null);
        return new PageResult<>(tagDTOList, (int) count);
    }

    /**
     * 保存或更新标签
     *
     * @param tagVO 标签vo
     */
    @Override
    public void saveOrUpdateTag(TagVO tagVO) {
        // 查询标签名是否存在
        Tag existTag = baseMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getTagName, tagVO.getTagName()));
        AssertUtil.isTrue(Objects.isNull(existTag) || !existTag.getId().equals(tagVO.getId()), "标签名已存在");
        Tag tag = BeanUtil.copyProperties(tagVO, Tag.class);
        this.saveOrUpdate(tag);
    }

    /**
     * 删除标签
     *
     * @param tagIdList 标签id列表
     */
    @Override
    public void deleteTag(List<Integer> tagIdList) {
        // 查询标签下是否有文章
        long count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, tagIdList));
        AssertUtil.isTrue(count == 0, "删除失败，该标签下存在文章");
        baseMapper.deleteBatchIds(tagIdList);
    }
}

