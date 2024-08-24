package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.CategoryBackDTO;
import cn.icatw.blog.domain.dto.CategoryDTO;
import cn.icatw.blog.domain.dto.CategoryOptionDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.CategoryService;
import cn.icatw.blog.domain.vo.CategoryVO;
import cn.icatw.blog.domain.vo.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.icatw.blog.constant.OptTypeConst.REMOVE;
import static cn.icatw.blog.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * (Category)表控制层
 *
 * @author icatw
 * @since 2024-03-22 20:39:51
 */
@RestController
@Tag(name = "分类模块", description = "分类模块")
public class CategoryController {

    /**
     * 构造方法依赖注入
     */
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 查看分类列表
     *
     * @return {@link Result < CategoryDTO >} 分类列表
     */
    @Operation(summary = "查看分类列表")
    @GetMapping("/categories")
    public Result<PageResult<CategoryDTO>> listCategories() {
        return Result.ok(categoryService.listCategories());
    }

    /**
     * 分类列表（搜索用）
     *
     * @param params params
     * @return {@link Result}<{@link List}<{@link CategoryOptionDTO}>>
     */
    @GetMapping("/admin/categories/search")
    @Operation(summary = "分类列表（搜索用）")
    public Result<List<CategoryOptionDTO>> listCategoriesBySearch(ConditionParams params) {
        return Result.ok(this.categoryService.listCategoriesBySearch(params));
    }

    /**
     * 查看后台分类列表
     *
     * @param condition 条件
     * @return {@link Result<CategoryBackDTO>} 后台分类列表
     */
    @Operation(summary = "查看后台分类列表")
    @GetMapping("/admin/categories")
    public Result<PageResult<CategoryBackDTO>> listBackCategories(ConditionParams condition) {
        return Result.ok(categoryService.listBackCategories(condition));
    }


    /**
     * 添加或修改分类
     *
     * @param categoryVO 分类信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @Operation(summary = "添加或修改分类")
    @PostMapping("/admin/categories")
    public Result<?> saveOrUpdateCategory(@Valid @RequestBody CategoryVO categoryVO) {
        categoryService.saveOrUpdateCategory(categoryVO);
        return Result.ok();
    }

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @Operation(summary = "删除分类")
    @DeleteMapping("/admin/categories")
    public Result<?> deleteCategories(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategory(categoryIdList);
        return Result.ok();
    }
}

