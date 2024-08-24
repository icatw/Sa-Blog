package cn.icatw.blog.annotation;

import cn.icatw.blog.enums.LimitType;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EnableAspectJAutoProxy
public @interface Limit {

    /**
     * 资源名称，用于描述接口功能
     *
     * @return {@link String}
     */
    String name() default "";

    /**
     * 资源 key
     *
     * @return {@link String}
     */
    String key() default "";

    /**
     * key prefix
     *
     * @return {@link String}
     */
    String prefix() default "";

    /**
     * 时间的，单位秒
     *
     * @return int
     */
    int period();

    /**
     * 限制访问次数
     *
     * @return int
     */
    int count();

    /**
     * 限制类型
     *
     * @return {@link LimitType}
     */
    LimitType limitType() default LimitType.CUSTOMER;
}

