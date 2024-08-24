package cn.icatw.blog.strategy.context;

import cn.icatw.blog.domain.dto.UserDetailDTO;
import cn.icatw.blog.strategy.SocialLoginStrategy;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.stereotype.Service;

import java.util.Map;

import static cn.icatw.blog.enums.LoginTypeEnum.getStrategy;

/**
 * 社交登录策略上下文
 *
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @date 2024/04/12
 * @since 2024/4/12
 */
@Service
public class SocialLoginStrategyContext {
    @Resource
    private Map<String, SocialLoginStrategy> socialLoginStrategyMap;

    /**
     * 执行社交登录策略
     *
     * @param source   来源
     * @param callback 登录回调
     * @param response 响应
     * @return {@link UserDetailDTO}
     */
    public UserDetailDTO executeSocialLoginStrategy(String source, AuthCallback callback, HttpServletResponse response) {
        return socialLoginStrategyMap.get(getStrategy(source)).login(source,callback, response);
    }

    public Map<String, Object> renderAuth(String source) {
        return socialLoginStrategyMap.get(getStrategy(source)).renderAuth(source);
    }
}
