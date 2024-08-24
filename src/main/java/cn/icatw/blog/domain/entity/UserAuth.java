package cn.icatw.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (UserAuth)实体类
 *
 * @author icatw
 * @since 2024-03-21 10:17:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user_auth")
@Builder
public class UserAuth implements Serializable {
    @Serial
    private static final long serialVersionUID = 622350916394823231L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(title = "userId", description = "主键id", defaultValue = "1")
    private Integer id;

    /**
     * 用户信息id
     */
    @TableField(value = "user_info_id")
    private Integer userInfoId;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 登录类型
     */
    @TableField(value = "login_type")
    private Integer loginType;

    /**
     * 用户登录ip
     */
    @TableField(value = "ip_address")
    private String ipAddress;

    /**
     * ip来源
     */
    @TableField(value = "ip_source")
    private String ipSource;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 上次登录时间
     */
    @TableField(value = "last_login_time")
    private LocalDateTime lastLoginTime;
}

