package cn.icatw.blog.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author 王顺
 * @date 2024/03/27
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {

    /**
     * @return 操作类型
     */
    String optType() default "";

}
