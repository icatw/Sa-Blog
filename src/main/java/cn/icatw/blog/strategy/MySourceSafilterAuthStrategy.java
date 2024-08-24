package cn.icatw.blog.strategy;

import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.stp.StpUtil;
import cn.icatw.blog.domain.dto.ResourceRoleDTO;
import cn.icatw.blog.domain.dto.UserDetailDTO;
import cn.icatw.blog.excetion.BizException;
import cn.icatw.blog.mapper.RoleMapper;
import cn.icatw.blog.service.RedisService;
import cn.icatw.blog.utils.UserUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;

import static cn.icatw.blog.constant.UserInfoConstant.USER_ONLINE;


/**
 * 资源角色管理策略
 *
 * @author 王顺
 * @date 2024/04/10
 */


@Component
@Slf4j
public class MySourceSafilterAuthStrategy implements SaFilterAuthStrategy {

    /**
     * 资源角色列表，用static能直接更改静态属性，以至于可以在类里面使用
     */
    private static List<ResourceRoleDTO> resourceRoleList;


    private final RoleMapper roleDao;

    private final HttpServletRequest request;

    private final RedisService redisService;

    public MySourceSafilterAuthStrategy(RoleMapper roleDao, HttpServletRequest request, RedisService redisService) {
        this.roleDao = roleDao;
        this.request = request;
        this.redisService = redisService;
    }


    /**
     * 加载资源角色信息
     */
    @PostConstruct // 在Spring中：Constructor >> @Autowired >> @PostConstruct
    private void loadDataSource() {
        resourceRoleList = roleDao.listResourceRoles();
        //log.info("服务器 resourceRoleList 加载： {}", resourceRoleList);
    }

    /**
     * 加载在线用户信息
     */
    @PostConstruct // 在Spring中：Constructor >> @Autowired >> @PostConstruct
    private void initOnlineUser() {
        redisService.set(USER_ONLINE, new HashSet<UserDetailDTO>());
        //log.info("服务器启动 Redis中在线用户list初始化");
    }


    /**
     * 清空接口角色信息
     */
    public void clearDataSource() {
        resourceRoleList = null;
        //log.info("服务器 resourceRoleList 清空");
    }

    /**
     * 校验接口权限
     */
    @Override
    public void run(Object r) {

        try {
            // 修改接口角色关系后重新加载
            if (CollectionUtils.isEmpty(resourceRoleList)) {
                this.loadDataSource();
            }
            // 获取用户请求方式
            String method = request.getMethod();
            // 获取用户请求Url
            String url = request.getRequestURI();
            // 获取用户的角色
            List<String> curUserRoleList = StpUtil.getRoleList();

            log.info("method = {}", method);
            log.info("url = {}", url);
            log.info("curUserRoleList = {}", curUserRoleList);
            //管理员放行
            if (curUserRoleList.contains("admin")) {
                log.info("管理员放行");
                return;
            }
            //不是admin开头的接口，放行
            if (!url.startsWith("/admin")) {
                log.info("不是admin开头的接口，放行");
                return;
            }
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            // 获取接口角色信息，若为匿名接口则放行，若无对应角色则禁止
            for (ResourceRoleDTO resourceRoleDTO : resourceRoleList) {
                if (antPathMatcher.match(resourceRoleDTO.getUrl(), url) && resourceRoleDTO.getRequestMethod().equals(method)) {
                    List<String> roleList = resourceRoleDTO.getRoleList();
                    for (String role : roleList) {
                        if (curUserRoleList.contains(role)) {
                            log.info("用户通过了其中一个角色验证：{}", role);
                            return;//说明放行
                        }
                    }
                    throw new BizException("用户：" + UserUtil.getCurrentUser().getUsername() + " 权限不够；url：" + url);

                }
            }
        } catch (NullPointerException e) {
            throw new BizException("发送空指针异常");
        }
        log.warn("这个接口没有被数据库记录！");

    }
}
