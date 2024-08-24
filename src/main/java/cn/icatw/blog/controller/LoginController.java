package cn.icatw.blog.controller;

import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.UserDetailDTO;
import cn.icatw.blog.service.UserService;
import cn.icatw.blog.strategy.context.SocialLoginStrategyContext;
import cn.icatw.blog.domain.vo.UserVO;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/4/3
 */
@Tag(name = "登录模块", description = "登录模块")
@RestController
@Slf4j
public class LoginController {
    @Resource
    private UserService userService;
    @Resource
    private SocialLoginStrategyContext socialLoginStrategyContext;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "根据用户名和密码登录")
    public Result<UserDetailDTO> doLogin(@NotBlank String username, @NotBlank String password) {
        UserDetailDTO userDetailDto = userService.login(username, password);
        return Result.ok(userDetailDto);
    }

    /**
     * 注销
     *
     * @return {@link Result}<{@link String}>
     */
    @RequestMapping("logout")
    @Operation(summary = "注销登录", description = "注销登录")
    public Result<String> logout() {
        userService.logout();
        return Result.ok("注销成功");
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return {@link Result<>}
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody UserVO user) {
        userService.register(user);
        return Result.ok();
    }


    /**
     * 获取授权链接并跳转到第三方授权页面
     *
     * @param response response
     * @throws IOException response可能存在的异常
     */
    @RequestMapping("/oauth/render/{source}")
    public Result<?> renderAuth(HttpServletResponse response, @PathVariable String source) throws IOException {
        Map<String, Object> map = socialLoginStrategyContext.renderAuth(source);
        return Result.ok(map);
    }


    /**
     * 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数
     *
     * @param callback 第三方回调时的入参
     */
    @RequestMapping("/oauth/callback/{source}")
    public void login(@PathVariable("source") String source, AuthCallback callback, HttpServletResponse response) throws IOException {
        UserDetailDTO userDetailDTO = socialLoginStrategyContext.executeSocialLoginStrategy(source, callback, response);
        log.info("用户信息：{}", JSON.toJSONString(userDetailDTO));
        String userDetailParams = URLEncoder.encode(JSON.toJSONString(userDetailDTO), StandardCharsets.UTF_8);
        response.sendRedirect("http://localhost:8081/loading?userDetail=" + userDetailParams);
    }

}
