package cn.icatw.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.icatw.blog.domain.dto.*;
import cn.icatw.blog.domain.entity.Article;
import cn.icatw.blog.domain.entity.OauthConfig;
import cn.icatw.blog.domain.entity.WebsiteConfig;
import cn.icatw.blog.excetion.BizException;
import cn.icatw.blog.mapper.*;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.BlogInfoService;
import cn.icatw.blog.service.PageService;
import cn.icatw.blog.service.RedisService;
import cn.icatw.blog.service.UniqueViewService;
import cn.icatw.blog.utils.IpUtil;
import cn.icatw.blog.domain.vo.BlogInfoVO;
import cn.icatw.blog.domain.vo.PageVO;
import cn.icatw.blog.domain.vo.WebsiteConfigVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static cn.icatw.blog.constant.CommonConst.*;
import static cn.icatw.blog.constant.RedisPrefixConst.*;
import static cn.icatw.blog.enums.ArticleStatusEnum.PUBLIC;
import static cn.icatw.blog.enums.UserAreaTypeEnum.getUserAreaType;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/3/22
 */
@Slf4j
@Service
public class BlogInfoServiceImpl implements BlogInfoService {
    @Resource
    HttpServletRequest request;
    @Resource
    RedisService redisService;
    @Resource
    ArticleMapper articleMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    MessageMapper messageMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    TagMapper tagMapper;
    @Resource
    WebsiteConfigMapper websiteConfigMapper;
    @Resource
    UniqueViewService uniqueViewService;
    @Resource
    PageService pageService;
    @Resource
    RestTemplate restTemplate;
    @Resource
    OauthConfigMapper oauthConfigMapper;

    /**
     * 访问
     */
    @Override
    public void visit() {
        // ip地址
        String ipAddress = IpUtil.getIpAddress(request);

        // 获取访问设备
        UserAgent userAgent = IpUtil.getUserAgent(request);
        // 根据ip和设备信息生成用户唯一标识
        String uuid = ipAddress + userAgent.getBrowser().getName() + userAgent.getOperatingSystem().getName();
        String md5 = DigestUtil.md5Hex(uuid);
        // 判断是否访问过
        if (!redisService.sIsMember(UNIQUE_VISITOR, md5)) {
            // 统计游客地域分布
            String ipSource = IpUtil.getIpSource(ipAddress);
            if (StrUtil.isNotBlank(ipSource)) {
                // 分割城市
                ipSource = ipSource.substring(0, 2)
                        .replaceAll(PROVINCE, "")
                        .replaceAll(CITY, "");
                redisService.hIncr(VISITOR_AREA, ipSource, 1L);
            } else {
                redisService.hIncr(VISITOR_AREA, UNKNOWN, 1L);
            }
            // 网站访问量+1
            redisService.incr(BLOG_VIEWS_COUNT, 1L);
            // 保存唯一标识
            redisService.sAdd(UNIQUE_VISITOR, md5);
        }


    }

    @Override
    public BlogBackInfoDTO getBlogBackHomeInfo() {
        Object count = redisService.get(BLOG_VIEWS_COUNT);
        // 网站访问量
        String viewCount = Optional.ofNullable(count).orElse(0).toString();
        // 分类
        List<CategoryDTO> categoryDTOList = categoryMapper.listCategoryDTO();
        // 标签
        List<TagDTO> tagDTOList = tagMapper.listTagDto();
        // 文章统计
        List<ArticleStatisticsDTO> articleStatisticsList = articleMapper.listArticleStatisticsDTO();
        // 最近一周用户访问量
        List<UniqueViewDTO> uniqueViewDTOList = uniqueViewService.listUniqueViewDTO();
        List<ArticleRankDTO> articleRankDTOList = new ArrayList<>();
        // redis中文章浏览量排行key-articleId，v-浏览量
        Map<Object, Double> articleViewsCountMap = redisService.zReverseRangeWithScore(ARTICLE_VIEWS_COUNT, 0, 4);
        if (CollUtil.isNotEmpty(articleViewsCountMap)) {
            // 获取map中的key取出所有id
            List<Integer> articleIdList = articleViewsCountMap.keySet().stream().map(id -> Integer.parseInt(id.toString())).toList();
            List<ArticleRankDTO> list = new ArrayList<>();
            for (Integer s : articleIdList) {
                Article article = articleMapper
                        .selectOne(new LambdaQueryWrapper<Article>()
                                .select(Article::getId, Article::getArticleTitle)
                                .eq(Article::getId, s).eq(Article::getIsDelete, FALSE));
                if (Objects.isNull(article)) {
                    // redis的文章失效
                    redisService.zRem(ARTICLE_VIEWS_COUNT, s.toString());
                    continue;
                }
                ArticleRankDTO apply = ArticleRankDTO
                        .builder()
                        .articleTitle(article.getArticleTitle())
                        .viewsCount(articleViewsCountMap.get(s).intValue()).build();
                list.add(apply);
            }
            list.sort(Comparator.comparing(ArticleRankDTO::getViewsCount).reversed());
            articleRankDTOList = list;
        }

        // build全部属性
        return BlogBackInfoDTO.builder()
                .uniqueViewDTOList(uniqueViewDTOList)
                .viewsCount(Integer.parseInt(viewCount))
                .messageCount(messageMapper.selectCount(null).intValue())
                .userCount(userMapper.selectCount(null).intValue())
                .categoryDTOList(categoryDTOList)
                .articleCount(articleMapper.selectCount(null).intValue())
                .tagDTOList(tagDTOList)
                .articleStatisticsList(articleStatisticsList)
                .articleRankDTOList(articleRankDTOList)
                .build();
    }

    @Override
    public List<UserAreaDTO> listUserAreas(ConditionParams conditionParams) {
        List<UserAreaDTO> result = new ArrayList<>();
        switch (Objects.requireNonNull(getUserAreaType(conditionParams.getType()))) {
            case USER:
                // 查询注册用户区域分布
                Object userArea = redisService.get(USER_AREA);
                if (Objects.nonNull(userArea)) {
                    result = JSON.parseObject(userArea.toString(), new TypeReference<>() {
                    });
                }
                return result;
            case VISITOR:
                // 查询游客区域分布
                Map<String, Object> visitorArea = redisService.hGetAll(VISITOR_AREA);
                if (Objects.nonNull(visitorArea)) {
                    result = visitorArea.entrySet().stream().map(item -> UserAreaDTO.builder()
                            .name(item.getKey())
                            .value(Long.valueOf(item.getValue().toString()))
                            .build()).toList();
                }
                return result;
            default:
                throw new IllegalStateException("Unexpected value: " + Objects.requireNonNull(getUserAreaType(conditionParams.getType())));
        }
    }

    @Override
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        // 修改网站配置
        WebsiteConfig websiteConfig = WebsiteConfig.builder()
                .id(1)
                .config(JSON.toJSONString(websiteConfigVO))
                .build();
        websiteConfigMapper.updateById(websiteConfig);
        // 删除缓存
        redisService.del(WEBSITE_CONFIG);
    }

    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        WebsiteConfigVO websiteConfigVO;
        // 获取缓存数据
        Object websiteConfig = redisService.get(WEBSITE_CONFIG);
        Object oauthConfig = redisService.get(OAUTH_CONFIG);
        if (Objects.nonNull(websiteConfig)) {
            websiteConfigVO = JSON.parseObject(websiteConfig.toString(), WebsiteConfigVO.class);
        } else {
            // 从数据库中加载
            String config = websiteConfigMapper.selectById(1).getConfig();
            websiteConfigVO = JSON.parseObject(config, WebsiteConfigVO.class);
            redisService.set(WEBSITE_CONFIG, config);
        }
        if (Objects.nonNull(oauthConfig)) {
            websiteConfigVO.setSocialLoginList(JSON.parseArray(oauthConfig.toString(), String.class));
        } else {
            List<String> socialLoginList = oauthConfigMapper.selectList(new LambdaQueryWrapper<OauthConfig>()
                    .eq(OauthConfig::getIsDisable, FALSE)).stream().map(OauthConfig::getOauthName).toList();
            websiteConfigVO.setSocialLoginList(socialLoginList);
            redisService.set(OAUTH_CONFIG, JSON.toJSONString(socialLoginList));
        }
        return websiteConfigVO;
    }

    public String getAbout() {
        Object value = redisService.get(ABOUT);
        return Objects.nonNull(value) ? value.toString() : "";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAbout(BlogInfoVO blogInfoVO) {
        redisService.set(ABOUT, blogInfoVO.getAboutContent());
    }

    @Override
    public BlogHomeInfoDTO getBlogHomeInfo() {
        // 查询文章数量
        long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));
        // 查询分类数量
        long categoryCount = categoryMapper.selectCount(null);
        // 查询标签数量
        long tagCount = tagMapper.selectCount(null);
        // 查询访问量
        Object count = redisService.get(BLOG_VIEWS_COUNT);
        String viewsCount = Optional.ofNullable(count).orElse(0).toString();
        // 查询网站配置
        WebsiteConfigVO websiteConfig = this.getWebsiteConfig();
        // 查询页面图片
        List<PageVO> pageVOList = pageService.listPages();
        // 封装数据
        return BlogHomeInfoDTO.builder()
                .articleCount((int) articleCount)
                .categoryCount((int) categoryCount)
                .tagCount((int) tagCount)
                .viewsCount(viewsCount)
                .websiteConfig(websiteConfig)
                .pageList(pageVOList)
                .build();
    }

    @Override
    public String getRandomCover() {
        // 异步获取随机封面
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            CoverDTO coverDTO;
            String jsonStr = restTemplate.getForObject(RANDOM_IMG_API, String.class);
            coverDTO = JSON.parseObject(jsonStr, CoverDTO.class);
            if (coverDTO != null) {
                return coverDTO.getImgurl();
            }
            return "";
        });
        future.exceptionally(e -> {
            throw new BizException("随机封面获取失败！");
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("随机封面获取失败！", e);
            throw new BizException("随机封面获取失败！");
        }

    }
}
