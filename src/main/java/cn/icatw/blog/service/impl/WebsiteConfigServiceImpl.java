package cn.icatw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.icatw.blog.domain.entity.WebsiteConfig;
import cn.icatw.blog.mapper.WebsiteConfigMapper;
import cn.icatw.blog.service.WebsiteConfigService;
import org.springframework.stereotype.Service;

/**
 * (WebsiteConfig)表服务实现类
 *
 * @author icatw
 * @since 2024-03-22 21:02:32
 */
@Service("websiteConfigService")
public class WebsiteConfigServiceImpl extends ServiceImpl<WebsiteConfigMapper, WebsiteConfig> implements WebsiteConfigService {
}

