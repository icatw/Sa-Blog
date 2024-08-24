package cn.icatw.blog.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.excetion.BizException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static cn.icatw.blog.common.ResultStatusEnum.SYSTEM_EXCEPTION;
import static cn.icatw.blog.common.ResultStatusEnum.VALID_ERROR;


/**
 * 全局异常处理
 *
 * @author 王顺
 * @since 2024/03/21
 */
@Log4j2
@RestControllerAdvice
public class ControllerAdviceHandler {

    /**
     * 处理服务异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = BizException.class)
    public Result<?> errorHandler(BizException e) {
        log.error(ExceptionUtil.stacktraceToString(e));
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> errorHandler(MethodArgumentNotValidException e) {
        log.error(ExceptionUtil.stacktraceToString(e));
        return Result.fail(VALID_ERROR.getCode(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * 处理系统异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> errorHandler(Exception e) {
        log.error(ExceptionUtil.stacktraceToString(e));
        return Result.fail(SYSTEM_EXCEPTION.getCode(), SYSTEM_EXCEPTION.getMessage());
    }

}
