package cn.icatw.blog.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 响应结果状态枚举类
 *
 * @author icatw
 * @date 2022/04/28
 */
@NoArgsConstructor
@AllArgsConstructor
public enum ResultStatusEnum {
    /**
     * 请求成功
     */
    SUCCESS(200, "请求成功！"),

    /**
     * 密码错误
     */
    PASSWORD_NOT_MATCHING(300, "用户名或密码错误"),
    /**
     * 用户存在
     */
    USER_EXISTS(301, "该用户名已存在"),
    NO_TOKEN_EXCEPTION(302, "没有token，请重新登陆"),
    TOKEN_VERIFICATION_FAILED(303, "token验证失败，请重新登录"),
    NOT_USER(304, "用户不存在，请重新登录"),
    /**
     * 系统异常
     */
    SYSTEM_EXCEPTION(401, "系统错误"),
    /**
     * 校验错误
     */
    VALID_ERROR(403, "校验异常"),
    /**
     * 其他异常
     */
    OTHER_EXCEPTION(500, "其他错误");

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String message;
}

