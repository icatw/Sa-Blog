package cn.icatw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.icatw.blog.domain.entity.SocialUserAuth;
import cn.icatw.blog.mapper.SocialUserAuthMapper;
import cn.icatw.blog.service.SocialUserAuthService;
import org.springframework.stereotype.Service;

/**
 * 三方登录-用户关联表(SocialUserAuth)表服务实现类
 *
 * @author icatw
 * @since 2024-04-12 11:04:20
 */
@Service("socialUserAuthService")
public class SocialUserAuthServiceImpl extends ServiceImpl<SocialUserAuthMapper, SocialUserAuth> implements SocialUserAuthService {
}

