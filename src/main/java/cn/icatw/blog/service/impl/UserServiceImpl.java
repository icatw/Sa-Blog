package cn.icatw.blog.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.icatw.blog.constant.CommonConst;
import cn.icatw.blog.constant.MQPrefixConst;
import cn.icatw.blog.domain.dto.*;
import cn.icatw.blog.domain.entity.SocialUser;
import cn.icatw.blog.domain.entity.SocialUserAuth;
import cn.icatw.blog.domain.entity.User;
import cn.icatw.blog.domain.entity.UserRole;
import cn.icatw.blog.domain.vo.*;
import cn.icatw.blog.enums.FilePathEnum;
import cn.icatw.blog.enums.LoginTypeEnum;
import cn.icatw.blog.enums.RoleEnum;
import cn.icatw.blog.mapper.SocialUserAuthMapper;
import cn.icatw.blog.mapper.SocialUserMapper;
import cn.icatw.blog.mapper.UserMapper;
import cn.icatw.blog.mapper.UserRoleMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.params.UserConditionParams;
import cn.icatw.blog.service.BlogInfoService;
import cn.icatw.blog.service.RedisService;
import cn.icatw.blog.service.UserService;
import cn.icatw.blog.strategy.context.UploadStrategyContext;
import cn.icatw.blog.utils.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.icatw.blog.constant.CommonConst.*;
import static cn.icatw.blog.constant.RedisPrefixConst.*;
import static cn.icatw.blog.constant.UserInfoConstant.USER_INFO;
import static cn.icatw.blog.constant.UserInfoConstant.USER_ROLE;
import static cn.icatw.blog.utils.CommonUtil.checkEmail;
import static cn.icatw.blog.utils.CommonUtil.getRandomCode;
import static com.alibaba.fastjson2.util.DateUtils.SHANGHAI_ZONE_ID_NAME;

/**
 * (User)表服务实现类
 *
 * @author icatw
 * @since 2024-04-12 10:12:54
 */
@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    HttpServletRequest request;
    @Resource
    UserRoleMapper userRoleMapper;
    @Resource
    SocialUserAuthMapper socialUserAuthMapper;
    @Resource
    SocialUserMapper socialUserMapper;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    RedisService redisService;
    @Resource
    BlogInfoService blogInfoService;
    @Resource
    UploadStrategyContext uploadStrategyContext;

    @Override
    public User getUserBySocial(String source, String uuid) {
        return baseMapper.getUserBySocial(String.valueOf(LoginTypeEnum.getLoginType(source)), uuid);
    }

    @Override
    public UserDetailDTO registerBySocial(MyAuthResponse authResponse) {
        String ipAddress = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        User user = User.builder()
                .email(authResponse.getEmail())
                .nickname(authResponse.getNickname())
                .username(authResponse.getUuid())
                .avatar(authResponse.getAvatar())
                .loginType(LoginTypeEnum.getLoginType(authResponse.getSource()))
                .lastLoginTime(LocalDateTime.now(ZoneId.of(SHANGHAI_ZONE_ID_NAME)))
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .build();
        this.save(user);
        // 绑定用户角色
        UserRole userRole = UserRole.builder()
                .userId(user.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
        //保存三方登录信息
        SocialUser socialUser = SocialUser.builder().uuid(authResponse.getUuid())
                .source(LoginTypeEnum.getLoginType(authResponse.getSource()))
                //.accessToken(authResponse.getAccessToken())
                .build();
        socialUserMapper.insert(socialUser);
        //保存三方登录和用户关联信息
        SocialUserAuth socialUserAuth = SocialUserAuth.builder()
                .socialUserId(socialUser.getId())
                .userId(user.getId()).build();
        socialUserAuthMapper.insert(socialUserAuth);
        return UserUtil.getUserDetailDtoByUserId(String.valueOf(user.getId()));
    }

    @Override
    public UserDetailDTO login(String username, String password) {
        //region 校验密码，登录成功之后修改最后登录时间等等操作
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        AssertUtil.isTrue(BCrypt.checkpw(password, user.getPassword()), "账号或密码错误");
        log.info("{}登录成功", username);
        UserDetailDTO userDetailDTO = UserUtil.getUserDetailDtoByUserId(String.valueOf(user.getId()));
        StpUtil.login(user.getId());
        StpUtil.getSession().set(USER_INFO, userDetailDTO);
        StpUtil.getSession().set(USER_ROLE, userDetailDTO.getRoleList());
        userDetailDTO.setSaTokenInfo(StpUtil.getTokenInfo());
        return userDetailDTO;
        //endregion

    }


    @Override
    public PageResult<UserBackDTO> listUserBackDTO(UserConditionParams condition) {
        // 获取后台用户数量
        Integer count = baseMapper.countUser(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 获取后台用户列表
        List<UserBackDTO> userBackDTOList = baseMapper.listUsers(PageUtil.getLimitCurrent(), PageUtil.getSize(), condition);
        return new PageResult<>(userBackDTOList, count);
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public void sendCode(String username) {
        //region 发送验证码
        // 校验账号是否合法
        AssertUtil.isTrue(checkEmail(username), "请输入正确邮箱！");
        // 生成六位随机验证码发送
        String code = getRandomCode();
        // 发送验证码
        EmailDTO emailDTO = EmailDTO.builder()
                .email(username)
                .subject("验证码")
                .content("您的验证码为 " + code + " 有效期15分钟，请不要告诉他人哦！")
                .build();
        rabbitTemplate.convertAndSend(MQPrefixConst.EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        // 将验证码存入redis，设置过期时间为15分钟
        redisService.set(USER_CODE_KEY + username, code, CODE_EXPIRE_TIME);
        //endregion
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserVO user) {
        // 校验账号是否合法
        AssertUtil.isTrue(checkUser(user), "邮箱已被注册！");
        // 新增用户信息
        User userInfo = User.builder()
                .email(user.getUsername())
                .nickname(CommonConst.DEFAULT_NICKNAME + IdWorker.getId())
                .avatar(blogInfoService.getWebsiteConfig().getUserAvatar())
                .username(user.getUsername())
                .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .loginType(LoginTypeEnum.EMAIL.getType())
                .build();
        baseMapper.insert(userInfo);
        // 绑定用户角色
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
    }

    @Override
    public void updatePassword(UserVO user) {
        // 校验账号是否合法
        AssertUtil.isTrue(!checkUser(user), "邮箱尚未注册！");
        // 根据用户名修改密码
        baseMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getPassword, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .eq(User::getUsername, user.getUsername()));
    }

    /**
     * 校验用户数据是否合法
     *
     * @param user 用户数据
     * @return 结果
     */
    private Boolean checkUser(UserVO user) {
        AssertUtil.isTrue(user.getCode().equals(redisService.get(USER_CODE_KEY + user.getUsername())), "验证码错误");
        //查询用户名是否存在
        User existUser = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, user.getUsername()));
        return Objects.isNull(existUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAdminPassword(PasswordVO passwordVO) {
        // 查询旧密码是否正确
        User oldUser = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, UserUtil.getCurrentUser().getUserId()));
        // 正确则修改密码，错误则提示不正确
        AssertUtil.isTrue(Objects.nonNull(oldUser) && BCrypt.checkpw(passwordVO.getOldPassword(), oldUser.getPassword()), "旧密码不正确!");
        User user = User.builder()
                .id(UserUtil.getCurrentUser().getUserId())
                .password(BCrypt.hashpw(passwordVO.getNewPassword(), BCrypt.gensalt()))
                .build();
        baseMapper.updateById(user);
    }

    @Override
    public PageResult<UserOnlineDTO> listOnlineUsers(ConditionParams conditionVO) {
        //sa-token获取在线用户列表
        List<UserOnlineDTO> list = new ArrayList<>();
        // 获取所有登录的用户ids
        List<String> logIds = StpUtil.searchSessionId("", 0, -1, false);
        // 便利获取登录信息
        SaSession session;
        for (String logId : logIds) {
            session = StpUtil.getSessionBySessionId(logId);
            UserDetailDTO userDetailDto = session.getModel(USER_INFO, UserDetailDTO.class);
            if (Objects.isNull(userDetailDto)) {
                continue;
            }
            if (StrUtil.isBlank(conditionVO.getKeywords()) || StrUtil.contains(userDetailDto.getNickname(), conditionVO.getKeywords())) {
                list.add(BeanCopyUtil.copyProperties(userDetailDto, UserOnlineDTO.class));
            }
        }
        // 执行分页
        int fromIndex = PageUtil.getLimitCurrent().intValue();
        int size = PageUtil.getSize().intValue();
        int toIndex = list.size() - fromIndex > size ? fromIndex + size : list.size();
        list = list.subList(fromIndex, toIndex);
        return new PageResult<>(list, logIds.size());
    }

    @Override
    public void removeOnlineUser(Integer userId) {
        log.info("踢下线： {}", userId);
        StpUtil.kickout(userId);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoVO userInfoVO) {
        log.info("loginId = {} 更改用户信息", StpUtil.getLoginId());
        UserDetailDTO userDetailDTO = (UserDetailDTO) StpUtil.getSession().get(USER_INFO);
        // 封装用户信息
        User user = User.builder()
                .id(userDetailDTO.getUserId())
                .nickname(userInfoVO.getNickname())
                .intro(userInfoVO.getIntro())
                .webSite(userInfoVO.getWebSite())
                .build();
        baseMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateUserAvatar(MultipartFile file) {
        log.info("loginId = {} 更改用户头像", StpUtil.getLoginId());
        // 头像上传
        String avatar = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        UserDetailDTO userDetailDTO = (UserDetailDTO) StpUtil.getSession().get(USER_INFO);
        // 更新用户信息
        User user = User.builder()
                .id(userDetailDTO.getUserId())
                .avatar(avatar)
                .build();
        baseMapper.updateById(user);
        return avatar;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUserEmail(EmailVO emailVO) {
        log.info("loginId = {} 更改用户邮箱", StpUtil.getLoginId());
        UserDetailDTO userDetailDTO = (UserDetailDTO) StpUtil.getSession().get(USER_INFO);
        AssertUtil.isTrue(emailVO.getCode().equals(redisService.get(USER_CODE_KEY + emailVO.getEmail())), "验证码错误！");
        User user = User.builder()
                .id(userDetailDTO.getUserId())
                .email(emailVO.getEmail())
                .build();
        baseMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(UserRoleVO userRoleVO) {
        // 更新用户角色和昵称
        User user = User.builder()
                .id(userRoleVO.getUserId())
                .nickname(userRoleVO.getNickname())
                .build();
        baseMapper.updateById(user);
        // 删除用户角色重新添加
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleVO.getUserId()));
        //批量新增用户角色
        List<UserRole> userRoleList = userRoleVO.getRoleIdList().stream()
                .map(roleId -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userRoleVO.getUserId())
                        .build())
                .collect(Collectors.toList());
        userRoleMapper.insertBatch(userRoleList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserDisable(UserDisableVO userDisableVO) {
        // 更新用户禁用状态
        User user = User.builder()
                .id(userDisableVO.getId())
                .isDisable(userDisableVO.getIsDisable())
                .build();
        baseMapper.updateById(user);
    }

    @Override
    public void loginHandle(Integer loginId) {
        String ipAddress = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        LocalDateTime now = LocalDateTime.now(ZoneId.of(SHANGHAI_ZONE_ID_NAME));
        //region 登录处理
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .set(User::getLastLoginTime, now)
                .set(User::getIpAddress, ipAddress)
                .set(User::getIpSource, ipSource)
                .eq(User::getId, loginId);
        update(updateWrapper);
        //endregion
    }

    /**
     * 统计用户地区
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void statisticalUserArea() {
        // 统计用户地域分布
        Map<String, Long> userAreaMap = baseMapper.selectList(new LambdaQueryWrapper<User>().select(User::getIpSource))
                .stream()
                .map(item -> {
                    if (StringUtils.isNotBlank(item.getIpSource())) {
                        return item.getIpSource().substring(0, 2)
                                .replaceAll(PROVINCE, "")
                                .replaceAll(CITY, "");
                    }
                    return UNKNOWN;
                })
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        // 转换格式
        List<UserAreaDTO> userAreaList = userAreaMap.entrySet().stream()
                .map(item -> UserAreaDTO.builder()
                        .name(item.getKey())
                        .value(item.getValue())
                        .build())
                .collect(Collectors.toList());
        redisService.set(USER_AREA, JSON.toJSONString(userAreaList));
    }


    /*
    *     @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserVO user) {
        // 校验账号是否合法
        if (checkUser(user)) {
            throw new BizException("邮箱已被注册！");
        }
        // 新增用户信息
        UserInfo userInfo = UserInfo.builder()
                .email(user.getUsername())
                .nickname(CommonConst.DEFAULT_NICKNAME + IdWorker.getId())
                .avatar(blogInfoService.getWebsiteConfig().getUserAvatar())
                .build();
        userInfoMapper.insert(userInfo);
        // 绑定用户角色
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
        // 新增用户账号
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(user.getUsername())
                .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .loginType(LoginTypeEnum.EMAIL.getType())
                .build();
        baseMapper.insert(userAuth);
    }
    *
    * */
}

