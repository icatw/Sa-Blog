package cn.icatw.blog.aspect;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.constant.CommonConst;
import cn.icatw.blog.domain.entity.OperationLog;
import cn.icatw.blog.mapper.OperationLogMapper;
import cn.icatw.blog.utils.IpUtil;
import cn.icatw.blog.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * 操作日志切面处理
 *
 * @author 王顺
 * @date 2024/03/27
 */
@Aspect
@Component
public class OptLogAspect {

    private final OperationLogMapper operationLogMapper;

    public OptLogAspect(OperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(cn.icatw.blog.annotation.OptLog)")
    public void optLogPointCut() {
    }


    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "optLogPointCut()", returning = "keys")
    public void saveOptLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operationLog = new OperationLog();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // Optional防止空获取操作
        Class<?> declaringType = signature.getDeclaringType();
        Optional<Tag> tagOptional = Optional.ofNullable(declaringType.getAnnotation(Tag.class));
        Optional<Operation> operationOptional = Optional.ofNullable(method.getAnnotation(Operation.class));
        Optional<OptLog> optLogOptional = Optional.ofNullable(method.getAnnotation(OptLog.class));
        // 操作模块
        operationLog.setOptModule(tagOptional.isEmpty() ? CommonConst.UNKNOWN : tagOptional.get().name());
        // 操作类型
        operationLog.setOptType(optLogOptional.isEmpty() ? CommonConst.UNKNOWN : optLogOptional.get().optType());
        // 操作描述
        operationLog.setOptDesc(operationOptional.isEmpty() ? CommonConst.UNKNOWN : operationOptional.get().summary());
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;
        // 请求方式
        operationLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        // 请求方法
        operationLog.setOptMethod(methodName);
        // 请求参数
        operationLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs()));
        // 返回结果
        operationLog.setResponseData(JSON.toJSONString(keys));
        // 请求用户ID
        operationLog.setUserId(UserUtil.getCurrentUser().getUserId());
        // 请求用户
        operationLog.setNickname(UserUtil.getCurrentUser().getNickname());
        // 请求IP
        String ipAddress = IpUtil.getIpAddress(request);
        operationLog.setIpAddress(ipAddress);
        operationLog.setIpSource(IpUtil.getIpSource(ipAddress));
        // 请求URL
        operationLog.setOptUrl(request.getRequestURI());
        operationLogMapper.insert(operationLog);
    }

}
