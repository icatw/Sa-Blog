package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.*;
import cn.icatw.blog.domain.vo.*;
import cn.icatw.blog.enums.FilePathEnum;
import cn.icatw.blog.domain.params.ArticleConditionParams;
import cn.icatw.blog.service.ArticleService;
import cn.icatw.blog.strategy.context.ArticleImportStrategyContext;
import cn.icatw.blog.strategy.context.UploadStrategyContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static cn.icatw.blog.constant.OptTypeConst.*;

/**
 * (Article)表控制层
 *
 * @author icatw
 * @since 2024-03-22 20:36:28
 */
@RestController
@Tag(name = "文章模块", description = "文章模块")
public class ArticleController {

    /**
     * 构造方法依赖注入
     */
    private final ArticleService articleService;
    private final UploadStrategyContext uploadStrategyContext;
    private final ArticleImportStrategyContext articleImportStrategyContext;

    public ArticleController(ArticleService articleService, UploadStrategyContext uploadStrategyContext, ArticleImportStrategyContext articleImportStrategyContext) {
        this.articleService = articleService;
        this.uploadStrategyContext = uploadStrategyContext;
        this.articleImportStrategyContext = articleImportStrategyContext;
    }

    /**
     * 查看后台文章
     *
     * @param conditionVO 条件
     * @return {@link Result< ArticleBackDTO >} 后台文章列表
     */
    @Operation(summary = "查看后台文章")
    @GetMapping("/admin/articles")
    public Result<PageResult<ArticleBackDTO>> listArticleBacks(ArticleConditionParams conditionVO) {
        return Result.ok(articleService.listArticleBacks(conditionVO));
    }

    /**
     * 添加或修改文章
     *
     * @param articleVO 文章信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @Operation(summary = "添加或修改文章", description = "添加或修改文章")
    @PostMapping("/admin/articles")
    public Result<?> saveOrUpdateArticle(@Valid @RequestBody ArticleVO articleVO) {
        articleService.saveOrUpdateArticle(articleVO);
        return Result.ok();
    }

    /**
     * 根据id查看后台文章
     *
     * @param articleId 文章id
     * @return {@link Result<ArticleVO>} 后台文章
     */
    @Operation(summary = "根据id查看后台文章", description = "根据id查看后台文章")
    @GetMapping("/admin/articles/{articleId}")
    public Result<ArticleVO> getArticleBackById(@PathVariable("articleId") Integer articleId) {
        return Result.ok(articleService.getArticleBackById(articleId));
    }

    /**
     * 删除文章
     *
     * @param articleIdList 文章id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @Operation(summary = "物理删除文章", description = "物理删除文章")
    @DeleteMapping("/admin/articles")
    public Result<?> deleteArticles(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticles(articleIdList);
        return Result.ok();
    }

    /**
     * 恢复或删除文章
     *
     * @param deleteVO 逻辑删除信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @Operation(summary = "恢复或删除文章", description = "根据id恢复或删除文章")
    @PutMapping("/admin/articles")
    public Result<?> updateArticleDelete(@Valid @RequestBody DeleteVO deleteVO) {
        articleService.updateArticleDelete(deleteVO);
        return Result.ok();
    }

    /**
     * 查看文章归档
     *
     * @return {@link Result<ArchiveDTO>} 文章归档列表
     */
    @Operation(summary = "查看文章归档")
    @GetMapping("/articles/archives")
    public Result<PageResult<ArchiveDTO>> listArchives() {
        return Result.ok(articleService.listArchives());
    }

    /**
     * 查看首页文章
     *
     * @return {@link Result< ArticleHomeDTO >} 首页文章列表
     */
    @Operation(summary = "查看首页文章")
    @GetMapping("/articles")
    public Result<List<ArticleHomeDTO>> listArticles() {
        return Result.ok(articleService.listArticles());
    }

    /**
     * 修改文章置顶状态
     *
     * @param articleTopVO 文章置顶信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @Operation(summary = "修改文章置顶")
    @PutMapping("/admin/articles/top")
    public Result<?> updateArticleTop(@Valid @RequestBody ArticleTopVO articleTopVO) {
        articleService.updateArticleTop(articleTopVO);
        return Result.ok();
    }

    /**
     * 上传文章图片
     *
     * @param file 文件
     * @return {@link Result<String>} 文章图片地址
     */
    @Operation(summary = "上传文章图片")
    @PostMapping("/admin/articles/images")
    public Result<String> saveArticleImages(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.ARTICLE.getPath()));
    }

    /**
     * 根据id查看文章
     *
     * @param articleId 文章id
     * @return {@link Result<  ArticleDTO  >} 文章信息
     */
    @Operation(summary = "根据id查看文章")
    @GetMapping("/articles/{articleId}")
    public Result<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId) {
        return Result.ok(articleService.getArticleById(articleId));
    }

    /**
     * 根据条件查询文章
     *
     * @param condition 条件
     * @return {@link Result< ArticlePreviewListDTO >} 文章列表
     */
    @Operation(summary = "根据条件查询文章")
    @GetMapping("/articles/condition")
    public Result<ArticlePreviewListDTO> listArticlesByCondition(ArticleConditionParams condition) {
        return Result.ok(articleService.listArticlesByCondition(condition));
    }

    /**
     * 搜索文章
     *
     * @param condition 条件
     * @return {@link Result< ArticleSearchDTO >} 文章列表
     */
    @Operation(summary = "搜索文章")
    @GetMapping("/articles/search")
    public Result<List<ArticleSearchDTO>> listArticlesBySearch(ArticleConditionParams condition) {
        return Result.ok(articleService.listArticlesBySearch(condition));
    }

    /**
     * 点赞文章
     *
     * @param articleId 文章id
     * @return {@link Result<>}
     */
    @Operation(summary = "点赞文章")
    @PostMapping("/articles/{articleId}/like")
    public Result<?> saveArticleLike(@PathVariable("articleId") Integer articleId) {
        articleService.saveArticleLike(articleId);
        return Result.ok();
    }

    /**
     * 导出文章
     *
     * @param articleExportVO 文章导出VO
     * @return {@link Result}<{@link List}<{@link String}>>
     */
    @Operation(summary = "导出文章", description = "导出文章")
    @PostMapping("/admin/articles/export")
    public Result<List<String>> exportArticles(@RequestBody ArticleExportVO articleExportVO) {
        return Result.ok(articleService.exportArticles(articleExportVO));
    }

    /**
     * 导入文章
     *
     * @param file 文件
     * @param type 文章类型
     * @return {@link Result<>}
     */
    @Operation(summary = "导入文章", description = "导入文章")
    @PostMapping("/admin/articles/import")
    public Result<?> importArticles(MultipartFile file, @RequestParam(required = false) String type) {
        articleImportStrategyContext.importArticles(file, type);
        return Result.ok();
    }
    @GetMapping(value = "/admin/articles/reptile")
    @Operation(summary = "文章爬虫")
    @OptLog( optType = REPTILE)
    public Result<?> reptile( String url) {
        return articleService.reptile(url);
    }

}

