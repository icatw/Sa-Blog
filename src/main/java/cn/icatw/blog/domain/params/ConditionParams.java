package cn.icatw.blog.domain.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 查询条件
 *
 * @author 王顺
 * @date 2024/03/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConditionParams {

    /**
     * 页码
     */
    private Long current;

    /**
     * 条数
     */
    private Long size;

    /**
     * 搜索内容
     */
    private String keywords;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 是否审核
     */
    private Integer isReview;

}
