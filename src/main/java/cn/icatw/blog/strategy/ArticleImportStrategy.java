package cn.icatw.blog.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文章导入策略
 *
 * @author 王顺
 * @date 2024/04/17
 */
public interface ArticleImportStrategy {

    /**
     * 导入文章
     *
     * @param file 文件
     */
    void importArticles(MultipartFile file);
}
