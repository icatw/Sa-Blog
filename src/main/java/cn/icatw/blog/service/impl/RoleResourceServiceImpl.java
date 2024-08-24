package cn.icatw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.icatw.blog.domain.entity.RoleResource;
import cn.icatw.blog.mapper.RoleResourceMapper;
import cn.icatw.blog.service.RoleResourceService;
import org.springframework.stereotype.Service;

/**
 * (RoleResource)表服务实现类
 *
 * @author icatw
 * @since 2024-03-21 11:22:24
 */
@Service("roleResourceService")
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {
}

