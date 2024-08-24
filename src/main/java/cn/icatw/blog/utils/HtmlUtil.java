package cn.icatw.blog.utils;

import cn.hutool.extra.spring.SpringUtil;
import cn.icatw.blog.service.BlogInfoService;
import cn.icatw.blog.domain.vo.WebsiteConfigVO;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import lombok.extern.slf4j.Slf4j;

import static cn.icatw.blog.constant.CommonConst.TRUE;

/**
 * HTML工具类
 *
 * @author 王顺
 * @date 2024/04/09
 */
@Slf4j
public class HtmlUtil {
    private static final SensitiveWordBs WORD_BS = SensitiveWordBs.newInstance()
            .ignoreCase(true)
            .ignoreWidth(true)
            .ignoreNumStyle(true)
            .ignoreChineseStyle(true)
            .ignoreEnglishStyle(true)
            .ignoreRepeat(true)
            .enableNumCheck(false)
            .enableEmailCheck(false)
            .enableUrlCheck(false)
            .init();


    /**
     * 过滤标签
     *
     * @param source 需要进行剔除HTML的文本
     * @return 过滤后的内容
     */
    public static String filter(String source) {
        BlogInfoService blogInfoService = SpringUtil.getBean(BlogInfoService.class);
        WebsiteConfigVO websiteConfig = blogInfoService.getWebsiteConfig();
        if (websiteConfig.getIsSensitiveWordFilter().equals(TRUE)){
            if (WORD_BS.contains(source)) {
                log.warn("-------检测到的敏感词{}", WORD_BS.findAll(source));
                source = WORD_BS.replace(source);
            }
        }
        // 保留图片标签
        source = source.replaceAll("(?!<(img).*?>)<.*?>", "")
                .replaceAll("(onload(.*?)=)", "")
                .replaceAll("(onerror(.*?)=)", "");
        return deleteHtmlTag(source);
    }

    /**
     * 删除标签
     *
     * @param source 文本
     * @return 过滤后的文本
     */
    public static String deleteHtmlTag(String source) {
        // 删除转义字符
        source = source.replaceAll("&.{2,6}?;", "");
        // 删除script标签
        source = source.replaceAll("<\\s*?script[^>]*?>[\\s\\S]*?<\\s*?/\\s*?script\\s*?>", "");
        // 删除style标签
        source = source.replaceAll("<\\s*?style[^>]*?>[\\s\\S]*?<\\s*?/\\s*?style\\s*?>", "");
        return source;
    }

}
