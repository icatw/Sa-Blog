package cn.icatw.blog.controller;

import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.OperationLogDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.OperationLogService;
import cn.icatw.blog.domain.vo.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (OperationLog)表控制层
 *
 * @author icatw
 * @since 2024-03-27 10:59:18
 */
@RestController
@Tag(name = "日志模块", description = "日志模块")
public class OperationLogController {

    /**
     * 构造方法依赖注入
     */
    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 查看操作日志
     *
     * @param conditionVO 条件
     * @return {@link Result <OperationLogDTO>} 日志列表
     */
    @Operation(summary = "查看操作日志")
    @GetMapping("/admin/operation/logs")
    public Result<PageResult<OperationLogDTO>> listOperationLogs(ConditionParams conditionVO) {
        return Result.ok(operationLogService.listOperationLogs(conditionVO));
    }

    /**
     * 删除操作日志
     *
     * @param logIdList 日志id列表
     * @return {@link Result<>}
     */
    @Operation(summary = "删除操作日志")
    @DeleteMapping("/admin/operation/logs")
    public Result<?> deleteOperationLogs(@RequestBody List<Integer> logIdList) {
        operationLogService.removeByIds(logIdList);
        return Result.ok();
    }
}

