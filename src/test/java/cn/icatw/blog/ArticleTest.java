package cn.icatw.blog;

import cn.hutool.core.util.URLUtil;
import cn.icatw.blog.domain.entity.Article;
import cn.icatw.blog.domain.entity.Tag;
import cn.icatw.blog.enums.ArticleTypeEnum;
import cn.icatw.blog.excetion.BizException;
import cn.icatw.blog.mapper.ArticleMapper;
import cn.icatw.blog.mapper.TagMapper;
import cn.icatw.blog.service.ArticleService;
import cn.icatw.blog.service.BlogInfoService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.icatw.blog.constant.CommonConst.OTHER_CATEGORY_ID;
import static cn.icatw.blog.enums.ArticleStatusEnum.PUBLIC;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/8/23
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("文章测试")
@Slf4j
public class ArticleTest {
    @Resource
    private ArticleService articleService;
    @Resource
    private BlogInfoService blogInfoService;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private ArticleMapper baseMapper;

    @Test
    public void articleTest() throws IOException {
        System.out.println("测试爬虫");
        String url = "https://blog.csdn.net/bjmsb79/article/details/133107201";
        // articleService.reptile(url);
        // 解析HTML
        Document doc = Jsoup.connect(url).get();
        Element tagsBox = doc.selectFirst("div.tags-box");
        // 提取分类专栏
        System.out.println("分类专栏：");
        Element categorySpan = tagsBox.selectFirst("span:contains(分类专栏)");
        if (categorySpan != null) {
            Elements categoryLinks = new Elements();
            for (Element sibling = categorySpan.nextElementSibling(); sibling != null && !sibling.tagName().equals("span"); sibling = sibling.nextElementSibling()) {
                if (sibling.tagName().equals("a")) {
                    categoryLinks.add(sibling);
                }
            }
            if (categoryLinks.isEmpty()) {
                System.out.println("- 转载");
            } else {
                for (Element category : categoryLinks) {
                    System.out.println("- " + category.text());
                }
            }
        } else {
            System.out.println("- 转载");
        }

        // 提取文章标签
        System.out.println("文章标签：");
        Element tagsSpan = tagsBox.selectFirst("span:contains(文章标签)");
        if (tagsSpan != null) {
            Elements tagLinks = new Elements();
            for (Element sibling = tagsSpan.nextElementSibling(); sibling != null && sibling.tagName().equals("a"); sibling = sibling.nextElementSibling()) {
                tagLinks.add(sibling);
            }
            if (tagLinks.isEmpty()) {
                System.out.println("- 转载");
            } else {
                for (Element tag : tagLinks) {
                    System.out.println("- " + tag.text());
                }
            }
        } else {
            System.out.println("- 转载");
        }
    }

    @Test
    public void test() {
        System.out.println("测试");
        // String url = "https://blog.csdn.net/GISuuser/article/details/119547354";
        // String url = "https://blog.csdn.net/u012272367/article/details/121557644";
        String url = "https://blog.csdn.net/GISuuser/article/details/119547354";
        try {
            // 解码URL并获取网页文档
            url = URLUtil.decode(url);
            Document document = Jsoup.connect(url).get();

            // 获取文章标题
            Elements title = document.getElementsByClass("title-article");

            // 获取文章内容
            // 获取文章内容
            Elements content = document.getElementsByClass("article_content");
            if (StringUtils.isBlank(content.toString())) {
                throw new BizException("文章爬虫失败！");
            }

            // 获取内容并处理图片和小标题
            String newContent = content.get(0).html();

            // 处理图片，将<img>标签转换为Markdown格式，去掉#pic_center等附加参数
            newContent = newContent.replaceAll(
                    "<img[^>]*src=\"([^\"]+)\"[^>]*>",
                    "![]($1)"
            );

            // 移除小标题中的锚点标记
            newContent = newContent.replaceAll("\\{#.*?\\}", "");

            // 处理代码块，将<code>标签转为Markdown格式
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
                    .userId(1)
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

    }
}

