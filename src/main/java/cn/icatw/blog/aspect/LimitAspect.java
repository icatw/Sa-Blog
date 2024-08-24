package cn.icatw.blog.aspect;

import cn.hutool.core.text.StrFormatter;
import cn.icatw.blog.annotation.Limit;
import cn.icatw.blog.enums.LimitType;
import cn.icatw.blog.excetion.BizException;
import cn.icatw.blog.service.RedisService;
import cn.icatw.blog.utils.IpUtil;
import com.google.common.collect.ImmutableList;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class LimitAspect {


    private final RedisService redisService;

    public LimitAspect(RedisService redisService) {
        this.redisService = redisService;
    }

    @Pointcut("@annotation(cn.icatw.blog.annotation.Limit)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Signature pointSignature = point.getSignature();
        MethodSignature signature = (MethodSignature) pointSignature;
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnnotation.limitType();
        String name = limitAnnotation.name();
        String key;
        String ip = IpUtil.getIpAddress(request);
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();
        key = switch (limitType) {
            case IP -> ip;
            case CUSTOMER -> limitAnnotation.key();
        };
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limitAnnotation.prefix() + "_", key, ip));
        String luaScript = buildLuaScript();
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long count = redisService.execute(redisScript, keys, limitCount, limitPeriod);
        log.info("IP:{} 第 {} 次访问key为 {}，描述为 [{}] 的接口", ip, count, keys, name);
        if (count != null && count.intValue() <= limitCount) {
            return point.proceed();
        } else {
            throw new BizException(StrFormatter.format(" {} 接口访问过于频繁，请 {} 秒后再试!", name, limitPeriod));
        }
    }

    /**
     * 限流脚本
     * 调用的时候不超过阈值，则直接返回并执行计算器自加。
     *
     * @return lua脚本
     */
    private String buildLuaScript() {
        return """
                local c\

                c = redis.call('get',KEYS[1])\

                if c and tonumber(c) > tonumber(ARGV[1]) then\

                return c;\

                end\

                c = redis.call('incr',KEYS[1])\

                if tonumber(c) == 1 then\

                redis.call('expire',KEYS[1],ARGV[2])\

                end\

                return c;""";
    }

}

