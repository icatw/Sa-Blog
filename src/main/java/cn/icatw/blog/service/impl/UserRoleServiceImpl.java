package cn.icatw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.icatw.blog.domain.entity.UserRole;
import cn.icatw.blog.mapper.UserRoleMapper;
import cn.icatw.blog.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * (UserRole)表服务实现类
 *
 * @author icatw
 * @since 2024-03-21 11:22:22
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}

