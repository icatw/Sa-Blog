package cn.icatw.blog.service;

import cn.icatw.blog.domain.vo.ReviewVO;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/3/28
 */
public interface ReviewService {
    /**
     * 更新审核状态
     *
     * @param reviewVO  审核VO
     * @param tableName 表名称
     */
    void updateReview(ReviewVO reviewVO, String tableName);
}
