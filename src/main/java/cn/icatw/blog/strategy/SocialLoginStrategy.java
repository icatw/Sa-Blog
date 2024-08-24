package cn.icatw.blog.strategy;


import cn.icatw.blog.domain.dto.UserDetailDTO;
import jakarta.servlet.http.HttpServletResponse;
import me.zhyd.oauth.model.AuthCallback;

import java.util.Map;

/**
 * 社交登录策略
 *
 * @author 王顺
 * @date 2024/04/07
 */
public interface SocialLoginStrategy {

    /**
     * 登录
     *
     * @param source   来源
     * @param callback 回调
     * @param response 回答
     * @return {@link UserDetailDTO}
     */
    UserDetailDTO login(String source, AuthCallback callback, HttpServletResponse response);

    /**
     * 身份验证
     *
     * @param source 来源
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> renderAuth(String source);
}

