package cn.icatw.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.icatw.blog.domain.entity.ArticleTag;
import cn.icatw.blog.mapper.ArticleTagMapper;
import cn.icatw.blog.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * (ArticleTag)表服务实现类
 *
 * @author icatw
 * @since 2024-03-27 14:57:36
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}

