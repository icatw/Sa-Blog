package cn.icatw.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.*;
import cn.icatw.blog.domain.entity.Article;
import cn.icatw.blog.domain.entity.ArticleTag;
import cn.icatw.blog.domain.entity.Category;
import cn.icatw.blog.domain.entity.Tag;
import cn.icatw.blog.domain.vo.*;
import cn.icatw.blog.enums.ArticleTypeEnum;
import cn.icatw.blog.enums.FileExtEnum;
import cn.icatw.blog.enums.FilePathEnum;
import cn.icatw.blog.excetion.BizException;
import cn.icatw.blog.mapper.ArticleMapper;
import cn.icatw.blog.mapper.ArticleTagMapper;
import cn.icatw.blog.mapper.CategoryMapper;
import cn.icatw.blog.mapper.TagMapper;
import cn.icatw.blog.domain.params.ArticleConditionParams;
import cn.icatw.blog.service.ArticleService;
import cn.icatw.blog.service.BlogInfoService;
import cn.icatw.blog.service.RedisService;
import cn.icatw.blog.strategy.context.SearchStrategyContext;
import cn.icatw.blog.strategy.context.UploadStrategyContext;
import cn.icatw.blog.utils.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static cn.icatw.blog.constant.CommonConst.*;
import static cn.icatw.blog.constant.RedisPrefixConst.*;
import static cn.icatw.blog.enums.ArticleStatusEnum.DRAFT;
import static cn.icatw.blog.enums.ArticleStatusEnum.PUBLIC;

/**
 * (Article)表服务实现类
 *
 * @author icatw
 * @date 2024/03/27
 * @since 2024-03-22 20:36:28
 */
@Slf4j
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    RedisService redisService;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    ArticleTagMapper articleTagMapper;
    @Resource
    TagMapper tagMapper;
    @Resource
    SearchStrategyContext searchStrategyContext;
    @Resource
    private HttpSession session;
    @Resource
    BlogInfoService blogInfoService;
    @Resource
    private UploadStrategyContext uploadStrategyContext;

    /**
     * 文章列表
     *
     * @param params params
     * @return {@link PageResult}<{@link ArticleBackDTO}>
     */
    @Override
    public PageResult<ArticleBackDTO> listArticleBacks(ArticleConditionParams params) {
        Integer count = baseMapper.numberOfValidArticles(params);
        if (count == 0) {
            return new PageResult<>();
        }
        List<ArticleBackDTO> result = baseMapper.listArticlesBySearch(PageUtil.getLimitCurrent(), PageUtil.getSize(), params);
        // 查询文章点赞和浏览量
        Map<Object, Double> viewsCountMap = redisService.zAllScore(ARTICLE_VIEWS_COUNT);
        Map<String, Object> likeCountMap = redisService.hGetAll(ARTICLE_LIKE_COUNT);
        // 封装点赞量和浏览量
        result.forEach(item -> {
            // 封装浏览量
            item.setViewsCount(Objects.isNull(viewsCountMap.get(item.getId())) ? 0 : viewsCountMap.get(item.getId()).intValue());
            // 封装点赞量
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
        });
        return new PageResult<>(result, count);
    }

    @Override
    public List<String> exportArticles(ArticleExportVO articleExportVO) {
        // 查询文章信息
        List<Article> articleList = baseMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getArticleTitle, Article::getArticleContent)
                .in(Article::getId, articleExportVO.getIdList()));
        // AssertUtil.isTrue(articleExportVO.getType().equalsIgnoreCase("md"), "暂不支持导出该类型");
        // 写入文件并上传
        String extName = FileExtEnum.getFileExt("." + articleExportVO.getType().toLowerCase()).getExtName();
        String path = FilePathEnum.getFilePath("." + articleExportVO.getType().toLowerCase()).getPath();
        List<String> urlList = new ArrayList<>();
        for (Article article : articleList) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(articleExportVO.getType().equalsIgnoreCase("pdf") ?
                    MarkdownUtil.convertMarkdownToByte(article.getArticleContent())
                    : article.getArticleContent().getBytes())) {
                String url = uploadStrategyContext.executeUploadStrategy(article.getArticleTitle() + extName, inputStream, path);
                urlList.add(url);
            } catch (Exception e) {
                log.error(StrUtil.format("导入文章失败,堆栈:{}", ExceptionUtil.stacktraceToString(e)));
                throw new BizException("导出文章失败");
            }
        }
        return urlList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> reptile(String url) {
        try {
            // 解码URL并获取网页文档
            url = URLUtil.decode(url);
            Document document = Jsoup.connect(url).get();

            // 获取文章标题
            Elements title = document.getElementsByClass("title-article");

            // 获取文章内容
            Elements content = document.getElementsByClass("article_content");
            if (StringUtils.isBlank(content.toString())) {
                throw new BizException("文章爬虫失败！");
            }

            // 获取内容并处理图片和代码块
            String newContent = content.get(0).html();

            // 处理图片，将<img>标签转换为Markdown格式
            newContent = newContent.replaceAll(
                    "<img[^>]*src=\"([^\"]+)\"[^>]*>",
                    "![]($1)"
            );

            // 将代码块的`<code>`标签转为Markdown格式
            newContent = newContent.replaceAll("<code>(.*?)</code>", "`$1`");
            newContent = newContent.replaceAll("<pre><code class=\"lang-java\">(.*?)</code></pre>", "```java\n$1\n```");

            // 将HTML内容转换为Markdown格式
            MutableDataSet options = new MutableDataSet();
            String markdown = FlexmarkHtmlConverter.builder(options).build().convert(newContent);

            // 获取随机封面
            String randomCover = blogInfoService.getRandomCover();

            // 解析分类专栏和文章标签
            Element tagsBox = document.selectFirst("div.tags-box");
            String category = "转载";
            List<String> tagsList = new ArrayList<>();

            if (!Objects.isNull(tagsBox)) {
                // 提取分类专栏
                Element categorySpan = tagsBox.selectFirst("span:contains(分类专栏)");
                if (categorySpan != null) {
                    for (Element sibling = categorySpan.nextElementSibling(); sibling != null && !sibling.tagName().equals("span"); sibling = sibling.nextElementSibling()) {
                        if (sibling.tagName().equals("a")) {
                            category = sibling.text(); // 只保存第一个分类
                            break;
                        }
                    }
                }

                // 提取文章标签
                Element tagsSpan = tagsBox.selectFirst("span:contains(文章标签)");
                if (tagsSpan != null) {
                    for (Element sibling = tagsSpan.nextElementSibling(); sibling != null && sibling.tagName().equals("a"); sibling = sibling.nextElementSibling()) {
                        tagsList.add(sibling.text());
                    }
                }
            }

            // 创建并保存文章
            Article article = Article.builder()
                    .userId(UserUtil.getCurrentUser().getUserId())
                    .articleContent(markdown)
                    .categoryId(OTHER_CATEGORY_ID) // 假设你有方法获取分类ID，这里只是示例
                    .originalUrl(url)
                    .articleTitle(title.get(0).text())
                    .articleCover(randomCover)
                    .type(ArticleTypeEnum.REPRINTED.getType())
                    .status(PUBLIC.getStatus())
                    .build();

            baseMapper.insert(article);

            // 为文章添加标签
            if (tagsList.isEmpty()) {
                tagsList.add("转载"); // 没有标签时添加默认标签
            }
            List<Integer> tagsId = new ArrayList<>();
            tagsList.forEach(tag -> {
                Tag result = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getTagName, tag));
                if (result == null) {
                    result = Tag.builder().tagName(tag).build();
                    tagMapper.insert(result);
                }
                tagsId.add(result.getId());
            });
            tagMapper.saveArticleTag(article.getId(), tagsId);

            log.info("文章抓取成功，内容为:{}", JSON.toJSONString(article));
        } catch (IOException e) {
            throw new BizException("文章爬虫失败！");
        }

        return Result.ok();
    }


    @Override
    public List<ArticleSearchDTO> listArticlesBySearch(ArticleConditionParams condition) {
        return searchStrategyContext.executeSearchStrategy(condition.getKeywords());
    }

    /**
     * 保存或更新文章
     *
     * @param articleVO 文章vo
     */
    @Override
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        WebsiteConfigVO websiteConfig = blogInfoService.getWebsiteConfig();
        String articleCover = articleVO.getArticleCover();
        if (StrUtil.isBlank(articleCover)) {
            String randomCover = websiteConfig.getIsRandomCover().equals(TRUE) ? blogInfoService.getRandomCover() : null;
            articleVO.setArticleCover(randomCover);
            AssertUtil.notBlank(randomCover, "请上传封面!");
        }
        // 保存文章分类
        Category category = saveArticleCategory(articleVO);
        // 保存或修改文章
        Article article = BeanUtil.copyProperties(articleVO, Article.class);
        if (Objects.nonNull(category)) {
            article.setCategoryId(category.getId());
        }
        article.setUserId(UserUtil.getCurrentUser().getUserId());
        this.saveOrUpdate(article);
        // 保存文章标签
        saveArticleTag(articleVO, article.getId());
    }


    /**
     * 根据文章 ID 获取文章信息（包括分类和标签信息）
     *
     * @param articleId 文章 ID
     * @return ArticleVO 包含文章信息的视图对象
     */
    @Override
    public ArticleVO getArticleBackById(Integer articleId) {
        // 使用 Optional 避免空指针异常，并获取文章信息
        Optional<Article> articleOptional = Optional.ofNullable(this.getById(articleId));
        Article article = articleOptional.orElseThrow(() -> new BizException("Article not found"));

        // 查询文章对应的分类信息
        Category category = categoryMapper.selectById(article.getCategoryId());

        // 查询文章对应的标签信息列表
        List<Tag> tagList = articleTagMapper.selectList(
                        new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, article.getId()))
                .stream().map(articleTag -> tagMapper.selectById(articleTag.getTagId()))
                .toList();

        // 将标签列表转换为标签 ID 和标签名称的映射
        Map<Integer, String> tagIdNameMap = tagList.stream()
                .collect(Collectors.toMap(Tag::getId, Tag::getTagName));

        // 封装文章视图对象，并设置分类名称和标签名称列表
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        articleVO.setCategoryName(category.getCategoryName());
        articleVO.setTagNameList(new ArrayList<>(tagIdNameMap.values()));

        return articleVO;
    }

    /**
     * 删除文章
     *
     * @param articleIdList 物品id列表
     */
    @Override
    public void deleteArticles(List<Integer> articleIdList) {
        // 删除文章标签关联
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        // 删除文章
        baseMapper.deleteBatchIds(articleIdList);
    }

    @Override
    public void updateArticleDelete(DeleteVO deleteVO) {
        // 修改文章逻辑删除状态
        deleteVO.getIdList().forEach(id -> {
            // 构建更新字段的 LambdaUpdateWrapper
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article::getId, id) // 设置更新条件：id = id
                    .set(Article::getIsTop, FALSE) // 设置更新字段：is_top = false
                    .set(Article::getIsDelete, deleteVO.getIsDelete()); // 设置更新字段：is_delete = deleteVO.getIsDelete()
            // 执行更新操作
            baseMapper.update(null, updateWrapper);
        });

    }

    @Override
    public void saveArticleLike(Integer articleId) {
        // 判断是否点赞
        redisService.likeOpt(articleId, ARTICLE_USER_LIKE, ARTICLE_LIKE_COUNT);
    }

    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        // 查询推荐文章
        CompletableFuture<List<ArticleRecommendDTO>> recommendArticleList = CompletableFuture
                .supplyAsync(() -> baseMapper.listRecommendArticles(articleId));
        // 查询最新文章
        CompletableFuture<List<ArticleRecommendDTO>> newestArticleList = CompletableFuture
                .supplyAsync(() -> {
                    List<Article> articleList = baseMapper.selectList(new LambdaQueryWrapper<Article>()
                            .select(Article::getId, Article::getArticleTitle, Article::getArticleCover, Article::getCreateTime)
                            .eq(Article::getIsDelete, FALSE)
                            .eq(Article::getStatus, PUBLIC.getStatus())
                            .orderByDesc(Article::getId)
                            .last("limit 5"));
                    return BeanCopyUtil.copyToList(articleList, ArticleRecommendDTO.class);
                });
        // 查询id对应文章
        ArticleDTO article = baseMapper.getArticleById(articleId);
        AssertUtil.notNull(article, "文章不存在");
        // 更新文章浏览量
        updateArticleViewsCount(articleId);
        // 查询上一篇下一篇文章
        Article lastArticle = baseMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .lt(Article::getId, articleId)
                .orderByDesc(Article::getId)
                .last("limit 1"));
        Article nextArticle = baseMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .gt(Article::getId, articleId)
                .orderByAsc(Article::getId)
                .last("limit 1"));
        article.setLastArticle(BeanCopyUtil.copyProperties(lastArticle, ArticlePaginationDTO.class));
        article.setNextArticle(BeanCopyUtil.copyProperties(nextArticle, ArticlePaginationDTO.class));
        // 封装点赞量和浏览量
        Double score = redisService.zScore(ARTICLE_VIEWS_COUNT, articleId);
        if (Objects.nonNull(score)) {
            article.setViewsCount(score.intValue());
        }
        article.setLikeCount((Integer) redisService.hGet(ARTICLE_LIKE_COUNT, articleId.toString()));
        // 封装文章信息
        try {
            article.setRecommendArticleList(recommendArticleList.get());
            article.setNewestArticleList(newestArticleList.get());
        } catch (Exception e) {
            log.error("获取文章信息失败", e);
        }
        return article;
    }

    /**
     * 更新文章浏览量
     *
     * @param articleId 文章id
     */
    private void updateArticleViewsCount(Integer articleId) {
        Set<Integer> articleSet = Convert.toSet(Integer.class, Optional.ofNullable(session.getAttribute(ARTICLE_SET)).orElse(new HashSet<>()));
        if (!articleSet.contains(articleId)) {
            articleSet.add(articleId);
            session.setAttribute(ARTICLE_SET, articleSet);
            // 浏览量+1
            redisService.zIncr(ARTICLE_VIEWS_COUNT, articleId, 1D);
        }
    }

    @Override
    public ArticlePreviewListDTO listArticlesByCondition(ArticleConditionParams condition) {
        // 查询文章
        List<ArticlePreviewDTO> articlePreviewDTOList = baseMapper.listArticlesByCondition(PageUtil.getLimitCurrent(), PageUtil.getSize(), condition);
        // 搜索条件对应名(标签或分类名)
        String name;
        if (Objects.nonNull(condition.getCategoryId())) {
            name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                            .select(Category::getCategoryName)
                            .eq(Category::getId, condition.getCategoryId()))
                    .getCategoryName();
        } else {
            name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                            .select(Tag::getTagName)
                            .eq(Tag::getId, condition.getTagId()))
                    .getTagName();
        }
        return ArticlePreviewListDTO.builder()
                .articlePreviewDTOList(articlePreviewDTOList)
                .name(name)
                .build();
    }

    @Override
    public void updateArticleTop(ArticleTopVO articleTopVO) {
        // 修改文章置顶状态
        Article article = Article.builder()
                .id(articleTopVO.getId())
                .isTop(articleTopVO.getIsTop())
                .build();
        baseMapper.updateById(article);
    }

    @Override
    public List<ArticleHomeDTO> listArticles() {
        return baseMapper.listArticles(PageUtil.getLimitCurrent(), PageUtil.getSize());
    }

    @Override
    public PageResult<ArchiveDTO> listArchives() {
        Page<Article> page = new Page<>(PageUtil.getCurrent(), PageUtil.getSize());
        // 获取分页数据
        Page<Article> articlePage = baseMapper.selectPage(page, new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getCreateTime)
                .orderByDesc(Article::getCreateTime)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus()));
        List<ArchiveDTO> archiveDTOList = BeanCopyUtil.copyToList(articlePage.getRecords(), ArchiveDTO.class);
        return new PageResult<>(archiveDTOList, (int) articlePage.getTotal());
    }


    /**
     * 保存文章标签
     *
     * @param articleVO articleVo
     * @param articleId 文章id
     */
    private void saveArticleTag(ArticleVO articleVO, Integer articleId) {
        // 编辑文章则删除文章所有标签
        if (Objects.nonNull(articleVO.getId())) {
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, articleVO.getId()));
        }
        // 添加文章标签
        List<String> tagNameList = articleVO.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询已存在的标签
            List<Tag> existTagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                    .in(Tag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream()
                    .map(Tag::getTagName)
                    .toList();
            List<Integer> existTagIdList = existTagList.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            // 对比新增不存在的标签
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                List<Tag> tagList = tagNameList.stream().map(item -> {
                    Tag tag = Tag.builder().tagName(item).build();
                    tagMapper.insert(tag);
                    return tag;
                }).toList();
                List<Integer> tagIdList = tagList.stream()
                        .map(Tag::getId)
                        .toList();
                existTagIdList.addAll(tagIdList);
            }
            // 提取标签id绑定文章
            existTagIdList.forEach(item -> {
                ArticleTag articleTag = ArticleTag.builder()
                        .articleId(articleId)
                        .tagId(item)
                        .build();
                articleTagMapper.insert(articleTag);
            });
        }
    }

    /**
     * 保存文章类别
     *
     * @param articleVO articleVo
     * @return {@link Category}
     */
    private Category saveArticleCategory(ArticleVO articleVO) {
        // 判断分类是否存在
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, articleVO.getCategoryName()));
        if (Objects.isNull(category) && !articleVO.getStatus().equals(DRAFT.getStatus())) {
            category = Category.builder()
                    .categoryName(articleVO.getCategoryName())
                    .build();
            categoryMapper.insert(category);
        }
        return category;
    }
}

