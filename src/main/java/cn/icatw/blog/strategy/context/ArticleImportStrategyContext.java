package cn.icatw.blog.strategy.context;

import cn.icatw.blog.enums.MarkdownTypeEnum;
import cn.icatw.blog.strategy.ArticleImportStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文章导入策略上下文
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Service
public class ArticleImportStrategyContext {
    @Resource
    private Map<String, ArticleImportStrategy> articleImportStrategyMap;

    public void importArticles(MultipartFile file, String type) {
        articleImportStrategyMap.get(MarkdownTypeEnum.getMarkdownType(type)).importArticles(file);
    }
}
