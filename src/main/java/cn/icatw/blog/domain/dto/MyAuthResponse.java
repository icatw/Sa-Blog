package cn.icatw.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 授权响应
 *
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @date 2024/04/12
 * @since 2024/4/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyAuthResponse {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 性别
     */
    private String gender;
    /**
     * 来源
     */
    private String source;

}
