package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.SocialUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 社会化用户表(SocialUser)表服务接口
 *
 * @author icatw
 * @since 2024-04-12 10:36:57
 */
public interface SocialUserService extends IService<SocialUser> {

     Boolean checkSocialUserExist(String source, String uuid);
}

