package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.OperationLog;
import cn.icatw.blog.domain.dto.OperationLogDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (OperationLog)表服务接口
 *
 * @author icatw
 * @since 2024-03-27 10:59:18
 */
public interface OperationLogService extends IService<OperationLog> {
    /**
     * 查询日志列表
     *
     * @param conditionVO 条件
     * @return 日志列表
     */
    PageResult<OperationLogDTO> listOperationLogs(ConditionParams conditionVO);
}

