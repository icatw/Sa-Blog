package cn.icatw.blog.strategy.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.icatw.blog.excetion.BizException;
import cn.icatw.blog.service.ArticleService;
import cn.icatw.blog.strategy.ArticleImportStrategy;
import cn.icatw.blog.domain.vo.HexoArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.icatw.blog.constant.HexoConst.*;
import static cn.icatw.blog.enums.ArticleStatusEnum.DRAFT;
import static cn.icatw.blog.enums.ArticleStatusEnum.PUBLIC;
import static cn.icatw.blog.enums.ArticleTypeEnum.ORIGINAL;


/**
 * hexo文章导入策略impl
 *
 * @author 王顺
 * @date 2024/04/17
 */
@Slf4j
@Service("hexoArticleImportStrategyImpl")
public class HexoArticleImportStrategyImpl implements ArticleImportStrategy {
    private final ArticleService articleService;

    /**
     * hexo最大分隔符数
     */
    private final int HEXO_MAX_DELIMITER_COUNT = 2;

    /**
     * hexo最小分隔符数
     */
    private final int HEXO_MIN_DELIMITER_COUNT = 1;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HexoArticleImportStrategyImpl(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public void importArticles(MultipartFile file) {
        try {
            HexoArticleVO hexoArticleVO = new HexoArticleVO();
            // 原创
            hexoArticleVO.setType(ORIGINAL.getType());
            // 公开 （DRAFT不保存分类）
            hexoArticleVO.setStatus(PUBLIC.getStatus());

            AtomicInteger hexoDelimiterCount = new AtomicInteger();
            StringBuilder articleContent = new StringBuilder();

            // 分类或标签标记
            AtomicInteger flag = new AtomicInteger(NORMAL_FLAG);

            List<String> tagList = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            reader.lines().forEach(line -> {
                if (hexoDelimiterCount.get() == HEXO_MAX_DELIMITER_COUNT) {
                    // 分隔符结束就是正文
                    articleContent.append(line).append(NEW_LINE);
                } else {
                    if (line.equals(DELIMITER)) {//到达一个“---”分隔符就开始自加
                        hexoDelimiterCount.getAndIncrement();
                    }
                    if (hexoDelimiterCount.get() == HEXO_MIN_DELIMITER_COUNT) {
                        if (line.startsWith(TITLE_PREFIX)) {
                            hexoArticleVO.setArticleTitle(line.replace(TITLE_PREFIX, "").trim());
                        } else if (line.startsWith(DATE_PREFIX)) {
                            hexoArticleVO.setCreateTime(LocalDateTime.parse(line.replace(DATE_PREFIX, "").trim(), formatter));
                        } else if (line.startsWith(CATEGORIES_PREFIX)) {
                            flag.set(CATEGORY_FLAG);
                        } else if (line.startsWith(TAGS_PREFIX)) {
                            flag.set(TAG_FLAG);
                        } else if (line.startsWith(PREFIX) && flag.intValue() == CATEGORY_FLAG) {
                            hexoArticleVO.setCategoryName(line.replace(PREFIX, "").trim());
                        } else if (line.startsWith(PREFIX) && flag.intValue() == TAG_FLAG) {
                            tagList.add(line.replace(PREFIX, "").trim());
                        }
                    }
                }
            });

            hexoArticleVO.setTagNameList(tagList);
            hexoArticleVO.setArticleContent(articleContent.toString());

            // 如果分类或标签为空则设为草稿
            if (CollectionUtils.isEmpty(hexoArticleVO.getTagNameList()) || StrUtil.isBlank(hexoArticleVO.getCategoryName())) {
                hexoArticleVO.setStatus(DRAFT.getStatus());
            }

            articleService.saveOrUpdateArticle(hexoArticleVO);
        } catch (IOException e) {
            log.error(StrUtil.format("导入Hexo文章失败, 堆栈:{}", ExceptionUtil.stacktraceToString(e)));
            throw new BizException("导入Hexo文章失败");
        }
    }

}
