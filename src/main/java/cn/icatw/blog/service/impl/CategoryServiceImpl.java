package cn.icatw.blog.service.impl;

import cn.icatw.blog.domain.entity.Article;
import cn.icatw.blog.domain.entity.Category;
import cn.icatw.blog.domain.dto.CategoryBackDTO;
import cn.icatw.blog.domain.dto.CategoryDTO;
import cn.icatw.blog.domain.dto.CategoryOptionDTO;
import cn.icatw.blog.mapper.ArticleMapper;
import cn.icatw.blog.mapper.CategoryMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.CategoryService;
import cn.icatw.blog.utils.AssertUtil;
import cn.icatw.blog.utils.PageUtil;
import cn.icatw.blog.domain.vo.CategoryVO;
import cn.icatw.blog.domain.vo.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static cn.icatw.blog.constant.CommonConst.FALSE;

/**
 * (Category)表服务实现类
 *
 * @author icatw
 * @since 2024-03-22 20:39:51
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    ArticleMapper articleMapper;

    /**
     * 分类列表
     *
     * @param params params
     * @return {@link List}<{@link CategoryOptionDTO}>
     */
    @Override
    public List<CategoryOptionDTO> listCategoriesBySearch(ConditionParams params) {
        return baseMapper.listCategoriesBySearch(params);
    }

    /**
     * 后台分类列表
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link CategoryBackDTO}>
     */
    @Override
    public PageResult<CategoryBackDTO> listBackCategories(ConditionParams condition) {
        // 查询分类数量
        long count = baseMapper.selectCount(new LambdaQueryWrapper<Category>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Category::getCategoryName, condition.getKeywords()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询分类列表
        List<CategoryBackDTO> categoryList = baseMapper.listCategoryBackDTO(PageUtil.getLimitCurrent(), PageUtil.getSize(), condition);
        return new PageResult<>(categoryList, (int) count);
    }

    /**
     * 保存或更新类别
     *
     * @param categoryVO vo类
     */
    @Override
    public void saveOrUpdateCategory(CategoryVO categoryVO) {
        Category existCategory = baseMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, categoryVO.getCategoryName()));
        AssertUtil.isTrue(Objects.isNull(existCategory) || !existCategory.getId().equals(categoryVO.getId()), "分类名已存在");
        Category newCategory = Category.builder()
                .id(categoryVO.getId())
                .categoryName(categoryVO.getCategoryName())
                .build();
        this.saveOrUpdate(newCategory);
    }

    /**
     * 删除类别
     *
     * @param categoryIdList 类别id列表
     */
    @Override
    public void deleteCategory(List<Integer> categoryIdList) {
        //检查分类下是否存在文章
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdList)
                .eq(Article::getIsDelete, FALSE));
        AssertUtil.isFalse(count > 0, "该分类下存在文章，无法删除");
        baseMapper.deleteBatchIds(categoryIdList);
    }

    @Override
    public PageResult<CategoryDTO> listCategories() {
        return new PageResult<>(baseMapper.listCategoryDTO(), Math.toIntExact(baseMapper.selectCount(null)));
    }
}

