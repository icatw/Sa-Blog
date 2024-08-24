package cn.icatw.blog.service.impl;

import cn.icatw.blog.domain.entity.SocialUser;
import cn.icatw.blog.mapper.SocialUserMapper;
import cn.icatw.blog.service.SocialUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 社会化用户表(SocialUser)表服务实现类
 *
 * @author icatw
 * @since 2024-04-12 10:36:57
 */
@Service("socialUserService")
public class SocialUserServiceImpl extends ServiceImpl<SocialUserMapper, SocialUser> implements SocialUserService {
    @Override
    public Boolean checkSocialUserExist(String source, String uuid) {
        return exists(new LambdaQueryWrapper<SocialUser>()
                .eq(SocialUser::getSource, source)
                .eq(SocialUser::getUuid, uuid));
    }
}

