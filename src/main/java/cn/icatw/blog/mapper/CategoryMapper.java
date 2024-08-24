package cn.icatw.blog.mapper;

import cn.icatw.blog.domain.dto.CategoryBackDTO;
import cn.icatw.blog.domain.dto.CategoryDTO;
import cn.icatw.blog.domain.dto.CategoryOptionDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Category)表数据库访问层
 *
 * @author icatw
 * @since 2024-03-22 20:39:50
 */
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 列表类别dto
     *
     * @return {@link List}<{@link CategoryDTO}>
     */
    List<CategoryDTO> listCategoryDTO();

    /**
     * 列出类别（搜索用）
     *
     * @param params params
     * @return {@link List}<{@link CategoryOptionDTO}>
     */
    List<CategoryOptionDTO> listCategoriesBySearch(ConditionParams params);

    /**
     * 后台分类列表
     *
     * @param limitCurrent limit
     * @param size         size
     * @param condition    条件
     * @return {@link List}<{@link CategoryBackDTO}>
     */
    List<CategoryBackDTO> listCategoryBackDTO(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionParams condition);
}

