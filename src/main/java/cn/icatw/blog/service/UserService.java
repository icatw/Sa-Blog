package cn.icatw.blog.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.icatw.blog.domain.entity.User;
import cn.icatw.blog.domain.dto.MyAuthResponse;
import cn.icatw.blog.domain.dto.UserBackDTO;
import cn.icatw.blog.domain.dto.UserDetailDTO;
import cn.icatw.blog.domain.dto.UserOnlineDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.params.UserConditionParams;
import cn.icatw.blog.domain.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * (User)表服务接口
 *
 * @author icatw
 * @since 2024-04-12 10:12:54
 */
public interface UserService extends IService<User> {


    /**
     * 通过社交获取用户
     *
     * @param source 来源
     * @param uuid   uuid
     * @return {@link User}
     */
    User getUserBySocial(String source, String uuid);

    /**
     * 第三方注册账号
     *
     * @param authResponse 身份验证响应
     * @return {@link UserDetailDTO}
     */
    UserDetailDTO registerBySocial(MyAuthResponse authResponse);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 暗语
     * @return {@link SaTokenInfo}
     */
    UserDetailDTO login(String username, String password);

    /**
     * 后台用户列表
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link UserBackDTO}>
     */
    PageResult<UserBackDTO> listUserBackDTO(UserConditionParams condition);

    /**
     * 注销
     */
    void logout();

    /**
     * 发送验证码
     *
     * @param username 用户名
     */
    void sendCode(String username);

    /**
     * 注册
     *
     * @param user 用户vo
     */
    void register(UserVO user);

    /**
     * 更新密码
     *
     * @param user 使用者
     */
    void updatePassword(UserVO user);

    /**
     * 更新管理员密码
     *
     * @param passwordVO 密码vo
     */
    void updateAdminPassword(PasswordVO passwordVO);

    /**
     * 在线用户列表
     *
     * @param conditionVO 条件vo
     * @return {@link PageResult}<{@link UserOnlineDTO}>
     */
    PageResult<UserOnlineDTO> listOnlineUsers(ConditionParams conditionVO);

    /**
     * 用户下线
     *
     * @param userInfoId 用户信息id
     */
    void removeOnlineUser(Integer userId);

    /**
     * 更新用户信息
     *
     * @param userInfoVO 用户信息vo
     */
    void updateUserInfo(UserInfoVO userInfoVO);

    /**
     * 更新用户头像
     *
     * @param file 文件
     * @return {@link String}
     */
    String updateUserAvatar(MultipartFile file);

    /**
     * 绑定用户邮箱
     *
     * @param emailVO 电子邮件vo
     */
    void saveUserEmail(EmailVO emailVO);

    /**
     * 更新用户角色
     *
     * @param userRoleVO 用户角色vo
     */
    void updateUserRole(UserRoleVO userRoleVO);

    /**
     * 修改用户禁用状态
     *
     * @param userDisableVO 用户禁用vo
     */
    void updateUserDisable(UserDisableVO userDisableVO);

    /**
     * 登录处理
     *
     * @param loginId 登录id
     */
    void loginHandle(Integer loginId);
}

