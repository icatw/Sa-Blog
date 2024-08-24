package cn.icatw.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型枚举
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 邮箱登录
     */
    EMAIL(1, "邮箱登录", "email", ""),
    /**
     * QQ登录
     */
    QQ(2, "QQ登录", "qq", "qqLoginStrategyImpl"),
    /**
     * 微博登录
     */
    WEIBO(3, "微博登录", "weibo", "weiboLoginStrategyImpl"),
    /**
     * gitee
     */
    GITEE(4, "Gitee登录", "gitee", "giteeLoginStrategyImpl"),
    /**
     * github
     */
    GITHUB(5, "Github登录", "github", "githubLoginStrategyImpl");

    /**
     * 登录方式
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;
    private final String code;

    /**
     * 策略
     */
    private final String strategy;

    /**
     * 获取登录策略
     *
     * @param source 来源
     * @return {@link String} 策略
     */
    public static String getStrategy(String source) {
        for (LoginTypeEnum value : LoginTypeEnum.values()) {
            if (value.getCode().equals(source)) {
                return value.getStrategy();
            }
        }
       throw new IllegalArgumentException("不支持的登录方式");
    }

    /**
     * 获取登录类型
     *
     * @param source 来源
     * @return {@link LoginTypeEnum} 登录类型
     */
    public static Integer getLoginType(String source) {
        for (LoginTypeEnum loginType : LoginTypeEnum.values()) {
            if (loginType.getCode().equals(source.toLowerCase())) {
                return loginType.getType();
            }
        }
        throw new IllegalArgumentException("不支持的登录方式");
    }
}
