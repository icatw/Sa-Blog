package cn.icatw.blog.mapper;

import cn.icatw.blog.domain.dto.*;
import cn.icatw.blog.domain.entity.Article;
import cn.icatw.blog.domain.params.ArticleConditionParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Article)表数据库访问层
 *
 * @author icatw
 * @since 2024-03-22 20:36:28
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 列出文章统计数据dto
     *
     * @return {@link List}<{@link ArticleStatisticsDTO}>
     */
    List<ArticleStatisticsDTO> listArticleStatisticsDTO();


    /**
     * 文章列表（搜索）
     *
     * @param current current
     * @param size    size
     * @param params  params
     * @return {@link List}<{@link ArticleBackDTO}>
     */
    List<ArticleBackDTO> listArticlesBySearch(@Param("current") Long current, @Param("size") Long size, @Param("params") ArticleConditionParams params);

    /**
     * 有效文章数
     *
     * @param params params
     * @return long
     */
    Integer numberOfValidArticles(@Param("params") ArticleConditionParams params);

    /**
     * 文章列表
     *
     * @param current 现在
     * @param size    大小
     * @return {@link List}<{@link ArticleHomeDTO}>
     */
    List<ArticleHomeDTO> listArticles(@Param("current") Long current, @Param("size") Long size);

    /**
     * 按条件列出文章
     *
     * @param current   现在
     * @param size      大小
     * @param condition 条件
     * @return {@link List}<{@link ArticlePreviewDTO}>
     */
    List<ArticlePreviewDTO> listArticlesByCondition(@Param("current") Long current, @Param("size") Long size, @Param("condition") ArticleConditionParams condition);

    /**
     * 列出推荐文章
     *
     * @param articleId 文章id
     * @return {@link List}<{@link ArticleRecommendDTO}>
     */
    List<ArticleRecommendDTO> listRecommendArticles(@Param("articleId") Integer articleId);

    /**
     * 按id获取文章
     *
     * @param articleId 文章id
     * @return {@link ArticleDTO}
     */
    ArticleDTO getArticleById(@Param("articleId") Integer articleId);
}

