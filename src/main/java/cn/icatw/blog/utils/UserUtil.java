package cn.icatw.blog.utils;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.icatw.blog.domain.entity.User;
import cn.icatw.blog.domain.dto.UserDetailDTO;
import cn.icatw.blog.mapper.RoleMapper;
import cn.icatw.blog.service.RedisService;
import cn.icatw.blog.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static cn.icatw.blog.constant.RedisPrefixConst.*;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/3/21
 */
@Component
public class UserUtil {

/*    public static UserDetailDTO getUserDetailDtoById(String userAuthId) {
        HttpServletRequest request = SpringMVCUtil.getRequest();
        //获取springboot上下文bean
        UserAuthService userAuthService = SpringUtil.getBean(UserAuthService.class);
        UserInfoService userInfoService = SpringUtil.getBean(UserInfoService.class);
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        RoleMapper roleMapper = SpringUtil.getBean(RoleMapper.class);
        UserAuth userAuth = userAuthService.getOne(
                new LambdaQueryWrapper<UserAuth>()
                        .select(UserAuth::getId, UserAuth::getUserInfoId, UserAuth::getLoginType,
                                UserAuth::getLastLoginTime, UserAuth::getIpAddress, UserAuth::getIpSource)
                        .eq(UserAuth::getId, userAuthId));
        AssertUtil.notNull(userAuth, "用户不存在");
        UserInfo userInfo = userInfoService.getById(userAuth.getUserInfoId());
        AssertUtil.notNull(userInfo, "用户信息不存在");
        // 查询账号点赞信息
        Set<Object> articleLikeSet = redisService.sMembers(ARTICLE_USER_LIKE + userInfo.getId());
        Set<Object> commentLikeSet = redisService.sMembers(COMMENT_USER_LIKE + userInfo.getId());
        Set<Object> talkLikeSet = redisService.sMembers(TALK_USER_LIKE + userInfo.getId());
        // 查询账号角色
        List<String> roleList = roleMapper.listRolesByUserInfoId(String.valueOf(userInfo.getId()));
        // 获取设备信息
        UserAgent userAgent = IpUtil.getUserAgent(request);
        return UserDetailDTO.builder()
                .id(userAuth.getId())
                .loginType(userAuth.getLoginType())
                //.userInfoId(userAuth.getUserInfoId())
                .ipAddress(userAuth.getIpAddress())
                .ipSource(userAuth.getIpSource())
                .os(userAgent.getOperatingSystem().getName())
                .browser(userAgent.getBrowser().getName())
                .username(userAuth.getUsername())
                .password(userAuth.getPassword())
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .email(userInfo.getEmail())
                .intro(userInfo.getIntro())
                .isDisable(userInfo.getIsDisable())
                .lastLoginTime(userAuth.getLastLoginTime())
                .webSite(userInfo.getWebSite())
                .roleList(roleList)
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .talkLikeSet(talkLikeSet)
                .build();
    }*/

    public static UserDetailDTO getUserDetailDtoByUserId(String userId) {
        HttpServletRequest request = SpringMVCUtil.getRequest();
        //获取springboot上下文bean
        UserService userService = SpringUtil.getBean(UserService.class);
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        RoleMapper roleMapper = SpringUtil.getBean(RoleMapper.class);
        User user = userService.getOne(
                new LambdaQueryWrapper<User>().eq(User::getId, userId));
        AssertUtil.notNull(user, "用户不存在");
        // 查询账号点赞信息
        Set<Object> articleLikeSet = redisService.sMembers(ARTICLE_USER_LIKE + user.getId());
        Set<Object> commentLikeSet = redisService.sMembers(COMMENT_USER_LIKE + user.getId());
        Set<Object> talkLikeSet = redisService.sMembers(TALK_USER_LIKE + user.getId());
        // 查询账号角色
        List<String> roleList = roleMapper.listRolesByUserInfoId(String.valueOf(user.getId()));
        // 获取设备信息
        UserAgent userAgent = IpUtil.getUserAgent(request);
        return UserDetailDTO.builder()
                .userId(user.getId())
                .loginType(user.getLoginType())
                .ipAddress(user.getIpAddress())
                .ipSource(user.getIpSource())
                .os(userAgent.getOperatingSystem().getName())
                .browser(userAgent.getBrowser().getName())
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .intro(user.getIntro())
                .isDisable(user.getIsDisable())
                .lastLoginTime(user.getLastLoginTime())
                .webSite(user.getWebSite())
                .roleList(roleList)
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .talkLikeSet(talkLikeSet)
                .build();
    }

    public static UserDetailDTO getCurrentUser() {
        return getUserDetailDtoByUserId(StpUtil.getLoginId().toString());
    }
}
