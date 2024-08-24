package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.Category;
import cn.icatw.blog.domain.dto.CategoryBackDTO;
import cn.icatw.blog.domain.dto.CategoryDTO;
import cn.icatw.blog.domain.dto.CategoryOptionDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.CategoryVO;
import cn.icatw.blog.domain.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Category)表服务接口
 *
 * @author icatw
 * @since 2024-03-22 20:39:51
 */
public interface CategoryService extends IService<Category> {
    /**
     * 分类列表（搜索用）
     *
     * @param params params
     * @return {@link List}<{@link CategoryOptionDTO}>
     */
    List<CategoryOptionDTO> listCategoriesBySearch(ConditionParams params);

    /**
     * 后台分类列表
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link CategoryBackDTO}>
     */
    PageResult<CategoryBackDTO> listBackCategories(ConditionParams condition);

    /**
     * 保存或更新类别
     *
     * @param categoryVO vo类
     */
    void saveOrUpdateCategory(CategoryVO categoryVO);

    /**
     * 删除类别
     *
     * @param categoryIdList 类别id列表
     */
    void deleteCategory(List<Integer> categoryIdList);

    /**
     * 分类列表
     *
     * @return {@link PageResult}<{@link CategoryDTO}>
     */
    PageResult<CategoryDTO> listCategories();
}

