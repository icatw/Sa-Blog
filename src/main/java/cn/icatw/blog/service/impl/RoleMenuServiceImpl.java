package cn.icatw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.icatw.blog.domain.entity.RoleMenu;
import cn.icatw.blog.mapper.RoleMenuMapper;
import cn.icatw.blog.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * (RoleMenu)表服务实现类
 *
 * @author icatw
 * @since 2024-03-21 11:22:23
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}

