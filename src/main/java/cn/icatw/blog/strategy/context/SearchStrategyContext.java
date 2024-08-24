package cn.icatw.blog.strategy.context;

import cn.icatw.blog.domain.dto.ArticleSearchDTO;
import cn.icatw.blog.strategy.SearchStrategy;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static cn.icatw.blog.enums.SearchModeEnum.getStrategy;


/**
 * 搜索策略上下文
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Service
public class SearchStrategyContext {
    /**
     * 搜索模式
     */
    @Value("${search.mode}")
    private String searchMode;

    @Resource
    private Map<String, SearchStrategy> searchStrategyMap;

    /**
     * 执行搜索策略
     *
     * @param keywords 关键字
     * @return {@link List < ArticleSearchDTO >} 搜索文章
     */
    public List<ArticleSearchDTO> executeSearchStrategy(String keywords) {
        return searchStrategyMap.get(getStrategy(searchMode)).searchArticle(keywords);
    }

}
