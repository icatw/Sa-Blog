package cn.icatw.blog.utils;

import cn.hutool.core.lang.Assert;
import cn.icatw.blog.excetion.BizException;

import java.util.Collection;

/**
 * Assert工具类，用于检查参数、状态等，并抛出业务异常
 *
 * @author 王顺
 * @date 2024/03/22
 */
public class AssertUtil extends Assert {

    /**
     * 检查条件是否为真，若为假则抛出业务异常
     *
     * @param expression 条件表达式
     * @param message    异常信息
     * @throws BizException 如果条件表达式为假
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BizException(message);
        }
    }
    /**
     * 检查条件是否为假，若为真则抛出业务异常
     *
     * @param expression 条件表达式
     * @param message    异常信息
     * @throws BizException 如果条件表达式为真
     */
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new BizException(message);
        }
    }

    /**
     * 检查对象是否为null，若不为null则抛出业务异常
     *
     * @param object  待检查对象
     * @param message 异常信息
     * @throws BizException 如果对象不为null
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new BizException(message);
        }
    }

    /**
     * 检查对象是否不为null，若为null则抛出业务异常
     *
     * @param object  待检查对象
     * @param message 异常信息
     * @throws BizException 如果对象为null
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new BizException(message);
        }
    }

    /**
     * 检查字符串是否为空，若为空则抛出业务异常
     *
     * @param str     待检查字符串
     * @param message 异常信息
     * @throws BizException 如果字符串为空
     */
    public static void notEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new BizException(message);
        }
    }

    /**
     * 检查集合是否为空，若为空则抛出业务异常
     *
     * @param collection 待检查集合
     * @param message    异常信息
     * @throws BizException 如果集合为空
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(message);
        }
    }

    // 可以根据需要添加其他的常用断言方法
}
