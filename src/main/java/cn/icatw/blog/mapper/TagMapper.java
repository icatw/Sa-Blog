package cn.icatw.blog.mapper;

import cn.icatw.blog.domain.dto.TagBackDTO;
import cn.icatw.blog.domain.dto.TagDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Tag)表数据库访问层
 *
 * @author icatw
 * @since 2024-03-22 20:54:59
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 标签Dto列表
     *
     * @return {@link List}<{@link TagDTO}>
     */
    List<TagDTO> listTagDto();

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
     * @param current   当前页
     * @param size      大小
     * @param condition 条件
     * @return {@link List}<{@link TagBackDTO}>
     */
    List<TagBackDTO> listTagBackDTO(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionParams condition);

    /**
     * 保存文章标记
     *
     * @param articleId 文章id
     * @param tagsId    标签id
     */
    void saveArticleTag(@Param("articleId") Integer articleId, @Param("tagsId") List<Integer> tagsId);
}

