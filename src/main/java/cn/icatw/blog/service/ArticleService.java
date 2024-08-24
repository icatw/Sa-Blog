package cn.icatw.blog.service;

import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.*;
import cn.icatw.blog.domain.entity.Article;
import cn.icatw.blog.domain.params.ArticleConditionParams;
import cn.icatw.blog.domain.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Article)表服务接口
 *
 * @author icatw
 * @since 2024-03-22 20:36:28
 */
public interface ArticleService extends IService<Article> {
    /**
     * 后台文章列表
     *
     * @param params params
     * @return {@link PageResult}<{@link ArticleBackDTO}>
     */
    List<ArticleSearchDTO> listArticlesBySearch(ArticleConditionParams params);

    /**
     * 保存或更新文章
     *
     * @param articleVO 文章vo
     */
    void saveOrUpdateArticle(ArticleVO articleVO);

    /**
     * 按id查询文章
     *
     * @param articleId 文章id
     * @return {@link ArticleVO}
     */
    ArticleVO getArticleBackById(Integer articleId);

    /**
     * 删除文章
     *
     * @param articleIdList 文章id列表
     */
    void deleteArticles(List<Integer> articleIdList);

    /**
     * 逻辑删除文章
     *
     * @param deleteVO 删除vo
     */
    void updateArticleDelete(DeleteVO deleteVO);

    /**
     * 文章点赞
     *
     * @param articleId 文章id
     */
    void saveArticleLike(Integer articleId);

    /**
     * 按id获取文章
     *
     * @param articleId 文章id
     * @return {@link ArticleDTO}
     */
    ArticleDTO getArticleById(Integer articleId);

    /**
     * 按条件列出文章
     *
     * @param condition 条件
     * @return {@link ArticlePreviewListDTO}
     */
    ArticlePreviewListDTO listArticlesByCondition(ArticleConditionParams condition);

    /**
     * 文章置顶
     *
     * @param articleTopVO 文章顶部vo
     */
    void updateArticleTop(ArticleTopVO articleTopVO);

    /**
     * 首页文章列表
     *
     * @return {@link List}<{@link ArticleHomeDTO}>
     */
    List<ArticleHomeDTO> listArticles();

    /**
     * 文章归档
     *
     * @return {@link PageResult}<{@link ArchiveDTO}>
     */
    PageResult<ArchiveDTO> listArchives();

    /**
     * 后台文章列表
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link ArticleBackDTO}>
     */
    PageResult<ArticleBackDTO> listArticleBacks(ArticleConditionParams condition);

    /**
     * 导出文章
     *
     * @param articleExportVO 文章id列表
     * @return {@link List}<{@link String}>
     */
    List<String> exportArticles(ArticleExportVO articleExportVO);

    /**
     * 文章爬虫
     *
     * @param url url
     * @return {@link Result}<{@link ?}>
     */
    Result<?> reptile(String url);
}

