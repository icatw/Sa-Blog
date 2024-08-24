package cn.icatw.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (User)实体类
 *
 * @author icatw
 * @since 2024-04-12 10:12:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user")
@Builder
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 697027755635560221L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "username")
    private String username;

    @TableField(value = "password")
    private String password;

    @TableField(value = "login_type")
    private Integer loginType;

    @TableField(value = "ip_address")
    private String ipAddress;

    @TableField(value = "ip_source")
    private String ipSource;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;
    /**
     * email
     */
    @TableField(value = "email")
    private String email;
    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 个人简介
     */
    @TableField(value = "intro")
    private String intro;

    /**
     * 个人网站
     */
    @TableField(value = "web_site")
    private String webSite;

    /**
     * 是否禁用
     */
    @TableField(value = "is_disable")
    private Integer isDisable;
}

