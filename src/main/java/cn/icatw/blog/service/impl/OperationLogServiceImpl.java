package cn.icatw.blog.service.impl;

import cn.icatw.blog.domain.entity.OperationLog;
import cn.icatw.blog.domain.dto.OperationLogDTO;
import cn.icatw.blog.mapper.OperationLogMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.OperationLogService;
import cn.icatw.blog.utils.BeanCopyUtil;
import cn.icatw.blog.utils.PageUtil;
import cn.icatw.blog.domain.vo.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (OperationLog)表服务实现类
 *
 * @author icatw
 * @since 2024-03-27 10:59:18
 */
@Service("operationLogService")
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
    @Override
    public PageResult<OperationLogDTO> listOperationLogs(ConditionParams conditionVO) {
        Page<OperationLog> page = new Page<>(PageUtil.getCurrent(), PageUtil.getSize());
        // 查询日志列表
        Page<OperationLog> operationLogPage = this.page(page, new LambdaQueryWrapper<OperationLog>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptModule, conditionVO.getKeywords())
                .or()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptDesc, conditionVO.getKeywords())
                .orderByDesc(OperationLog::getId));
        List<OperationLogDTO> operationLogDTOList = BeanCopyUtil.copyListToList(operationLogPage.getRecords(), OperationLogDTO.class);
        return new PageResult<>(operationLogDTOList, (int) operationLogPage.getTotal());
    }
}

