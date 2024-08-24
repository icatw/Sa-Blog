package cn.icatw.blog.strategy;


import cn.icatw.blog.domain.dto.ArticleSearchDTO;

import java.util.List;

/**
 * 搜索策略
 *
 * @author 王顺
 * @date 2024/04/07
 */
public interface SearchStrategy {

    /**
     * 搜索文章
     *
     * @param keywords 关键字
     * @return {@link List <ArticleSearchDTO>} 文章列表
     */
    List<ArticleSearchDTO> searchArticle(String keywords);

}
