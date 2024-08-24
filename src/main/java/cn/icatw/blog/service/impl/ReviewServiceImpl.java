package cn.icatw.blog.service.impl;

import cn.icatw.blog.mapper.ReviewMapper;
import cn.icatw.blog.service.ReviewService;
import cn.icatw.blog.domain.vo.ReviewVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/3/28
 */
@Service
public class ReviewServiceImpl implements ReviewService {
    @Resource
    private ReviewMapper reviewMapper;

    @Override
    public void updateReview(ReviewVO reviewVO, String tableName) {
        reviewMapper.updateReview(reviewVO, tableName);
    }
}
