package cn.icatw.blog.mapper;

import cn.icatw.blog.domain.vo.ReviewVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/3/28
 */
public interface ReviewMapper {
    /**
     * 更新审核状态
     *
     * @param reviewVO  审核VO
     * @param tableName 表名称
     */
    void updateReview(@Param("reviewVO") ReviewVO reviewVO, @Param("tableName") String tableName);
}
