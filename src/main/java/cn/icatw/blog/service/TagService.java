package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.Tag;
import cn.icatw.blog.domain.dto.TagBackDTO;
import cn.icatw.blog.domain.dto.TagDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.TagVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Tag)表服务接口
 *
 * @author icatw
 * @since 2024-03-22 20:54:59
 */
public interface TagService extends IService<Tag> {
    /**
     * 标签列表（搜索用）
     *
     * @param params params
     * @return {@link List}<{@link TagDTO}>
     */
    List<TagDTO> listTagsBySearch(ConditionParams params);

    /**
     * 后台标签列表
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link TagBackDTO}>
     */
    PageResult<TagBackDTO> listTagBackDTO(ConditionParams condition);

    /**
     * 标签list
     *
     * @return {@link PageResult}<{@link TagDTO}>
     */
    PageResult<TagDTO> listTags();

    /**
     * 保存或更新标记
     *
     * @param tagVO 标记vo
     */
    void saveOrUpdateTag(TagVO tagVO);

    /**
     * 删除标记
     *
     * @param tagIdList 标记id列表
     */
    void deleteTag(List<Integer> tagIdList);
}

