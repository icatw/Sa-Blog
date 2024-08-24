package cn.icatw.blog.strategy.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.icatw.blog.domain.entity.OauthConfig;
import cn.icatw.blog.domain.entity.User;
import cn.icatw.blog.domain.dto.MyAuthResponse;
import cn.icatw.blog.domain.dto.UserDetailDTO;
import cn.icatw.blog.excetion.BizException;
import cn.icatw.blog.service.OauthConfigService;
import cn.icatw.blog.service.UserService;
import cn.icatw.blog.strategy.SocialLoginStrategy;
import cn.icatw.blog.utils.AssertUtil;
import cn.icatw.blog.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.icatw.blog.constant.CommonConst.FALSE;
import static cn.icatw.blog.constant.UserInfoConstant.USER_INFO;
import static cn.icatw.blog.constant.UserInfoConstant.USER_ROLE;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/4/12
 */
@Slf4j
@Service
public abstract class AbstractSocialLoginStrategyImpl implements SocialLoginStrategy {
    @Resource
    UserService userService;
    @Resource
    OauthConfigService oauthConfigService;

    @Value("${baseUrl}")
    private String baseUrl;

    @Override
    public UserDetailDTO login(String source, AuthCallback callback, HttpServletResponse response) {
        try {
            UserDetailDTO userDetailDTO;
            // 获取第三方登录信息
            AuthRequest authRequest = getAuthRequest(source);
            AuthResponse loginResponse = authRequest.login(callback);
            AssertUtil.isTrue(loginResponse.ok(), "第三方登录失败");
            //把第三方登录信息转换为json字符串
            MyAuthResponse myAuthResponse = convertAuthResponse(loginResponse);
            //根据uuid和source查询用户是否存在
            User user = checkSocialUserExist(source, myAuthResponse.getUuid());
            if (Objects.nonNull(user)) {
                //如果存在则直接登录
                userDetailDTO = UserUtil.getUserDetailDtoByUserId(user.getId().toString());
            } else {
                //如果不存在则注册
                userDetailDTO = userService.registerBySocial(myAuthResponse);
            }
            StpUtil.login(userDetailDTO.getUserId());
            StpUtil.getSession().set(USER_INFO, userDetailDTO);
            StpUtil.getSession().set(USER_ROLE, userDetailDTO.getRoleList());
            userDetailDTO.setSaTokenInfo(StpUtil.getTokenInfo());
            return userDetailDTO;
        } catch (Exception e) {
            log.error(StrUtil.format("三方登录异常, 堆栈:{}", ExceptionUtil.stacktraceToString(e)));
            throw new BizException("第三方登录失败");
        }
    }

    private User checkSocialUserExist(String source, String uuid) {
        return userService.getUserBySocial(source, uuid);
    }

    public MyAuthResponse convertAuthResponse(AuthResponse loginResponse) {
        String jsonResponse = JSON.toJSONString(loginResponse.getData());
        log.info("第三方登录信息：{}", jsonResponse);
        return JSON.parseObject(jsonResponse, MyAuthResponse.class);
    }

    /**
     * 获取授权Request
     *
     * @return AuthRequest
     */
    private AuthRequest getAuthRequest(String source) {
        String oauthUrl = baseUrl + "/oauth";
        AuthRequest authRequest;
        OauthConfig oauthConfig = getOauthConfig(source);
        if (oauthConfig != null) {
            String clientId = oauthConfig.getClientId();
            String clientSecret = oauthConfig.getClientSecret();
            String redirectUri = oauthUrl + "/callback/" + source.toLowerCase();

            AuthConfig authConfig = AuthConfig.builder()
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .redirectUri(redirectUri)
                    .build();

            authRequest = switch (source.toLowerCase()) {
                case "gitee" -> new AuthGiteeRequest(authConfig);
                case "github" -> new AuthGithubRequest(authConfig);
                case "qq" -> new AuthQqRequest(authConfig);
                default -> throw new UnsupportedOperationException("不支持的第三方登录");
            };
        } else {
            throw new IllegalArgumentException("第三方登录配置信息不存在或已禁用");
        }
        return authRequest;
    }

    private OauthConfig getOauthConfig(String source) {
        return oauthConfigService.getOne(new LambdaQueryWrapper<OauthConfig>()
                .eq(OauthConfig::getOauthName, source)
                .eq(OauthConfig::getIsDisable, FALSE));
    }

    @Override
    public Map<String, Object> renderAuth(String source) {
        String authorizeUrl;
        try {
            AuthRequest authRequest = getAuthRequest(source);
            String token = AuthStateUtils.createState();
            authorizeUrl = authRequest.authorize(token);
        } catch (Exception e) {
            log.error("获取授权链接失败", e);
            throw new BizException("获取授权链接失败");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("url", authorizeUrl);
        return map;
    }
}
