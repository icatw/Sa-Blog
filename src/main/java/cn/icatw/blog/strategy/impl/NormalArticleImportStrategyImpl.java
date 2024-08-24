package cn.icatw.blog.strategy.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.icatw.blog.excetion.BizException;
import cn.icatw.blog.service.ArticleService;
import cn.icatw.blog.strategy.ArticleImportStrategy;
import cn.icatw.blog.domain.vo.ArticleVO;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static cn.icatw.blog.enums.ArticleStatusEnum.DRAFT;


/**
 * 普通文章导入策略impl
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Slf4j
@Service("normalArticleImportStrategyImpl")
public class NormalArticleImportStrategyImpl implements ArticleImportStrategy {
    @Resource
    private ArticleService articleService;

    @Override
    public void importArticles(MultipartFile file) {
        // 获取文件名作为文章标题
        String filename = file.getOriginalFilename();
        log.info("导入文件初始名：{}", filename);
        if (StringUtils.isBlank(filename)) {
            throw new BizException("文件名不能为空");
        }
        String[] arr = filename.split("\\.");
        String articleTitle;
        if (arr.length > 1) {
            articleTitle = arr[arr.length - 2];
        } else {
            articleTitle = arr[arr.length - 1];
        }
        // 获取文章内容
        StringBuilder articleContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            while (reader.ready()) {
                articleContent.append((char) reader.read());
            }
        } catch (IOException e) {
            log.error(StrUtil.format("导入文章失败, 堆栈:{}", ExceptionUtil.stacktraceToString(e)));
            throw new BizException("导入文章失败");
        }
        // 保存文章
        ArticleVO articleVO = ArticleVO.builder()
                .articleTitle(articleTitle)
                .articleContent(articleContent.toString())
                .status(DRAFT.getStatus())
                .build();
        articleService.saveOrUpdateArticle(articleVO);
    }
}
