package cn.icatw.blog.service;

import cn.icatw.blog.domain.dto.BlogBackInfoDTO;
import cn.icatw.blog.domain.dto.BlogHomeInfoDTO;
import cn.icatw.blog.domain.dto.UserAreaDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.BlogInfoVO;
import cn.icatw.blog.domain.vo.WebsiteConfigVO;

import java.util.List;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/3/22
 */
public interface BlogInfoService {
    /**
     * 访问
     */
    void visit();

    /**
     * 获取博客后台首页信息
     *
     * @return {@link BlogBackInfoDTO}
     */
    BlogBackInfoDTO getBlogBackHomeInfo();

    /**
     * 获取用户区域分步
     *
     * @param conditionParams 条件参数
     * @return {@link List}<{@link UserAreaDTO}>
     */
    List<UserAreaDTO> listUserAreas(ConditionParams conditionParams);

    /**
     * 更新网站配置
     *
     * @param websiteConfigVO 网站配置vo
     */
    void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO);

    /**
     * 获取网站配置
     *
     * @return {@link WebsiteConfigVO}
     */
    WebsiteConfigVO getWebsiteConfig();

    /**
     * 查看关于我信息
     *
     * @return {@link String}
     */
    String getAbout();

    /**
     * 更新关于我
     *
     * @param blogInfoVO 博客信息vo
     */
    void updateAbout(BlogInfoVO blogInfoVO);

    /**
     * 获取博客主页信息
     *
     * @return {@link BlogHomeInfoDTO}
     */
    BlogHomeInfoDTO getBlogHomeInfo();

    /**
     * 获得随机封面
     *
     * @return {@link String}
     */
    String getRandomCover();
}
