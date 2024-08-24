package cn.icatw.blog.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.icatw.blog.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.icatw.blog.constant.UserInfoConstant.USER_ROLE;

/**
 * stp接口impl
 *
 * @author 王顺
 * @apiNote
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    RoleService roleService;

    /**
     * 获取权限列表
     * loginId：账号id，即你在调用 StpUtil.login(id) 时写入的标识值。
     * loginType：账号体系标识，此处可以暂时忽略，在 [ 多账户认证 ] 章节下会对这个概念做详细的解释。
     *
     * @param loginId   登录id
     * @param loginType 登录类型
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //resourceService.getResourceListByUserId(loginId);
        return null;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return (List<String>) StpUtil.getSession().get(USER_ROLE);
    }
}
