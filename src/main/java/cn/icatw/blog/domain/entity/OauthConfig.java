package cn.icatw.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 三方登录配置表(OauthConfig)实体类
 *
 * @author icatw
 * @since 2024-04-16 17:22:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_oauth_config")
public class OauthConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 879997232682931019L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * client_id
     */
    @TableField(value = "client_id")
    private String clientId;

    /**
     * 三方登录type
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 三方名
     */
    @TableField(value = "oauth_name")
    private String oauthName;
    /**
     * appName
     */
    @TableField(value = "app_name")
    private String appName;
    /**
     * client_secret
     */
    @TableField(value = "client_secret")
    private String clientSecret;

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
     * 是否禁用0启用，1禁用
     */
    @TableField(value = "is_disable")
    private Integer isDisable;
}

