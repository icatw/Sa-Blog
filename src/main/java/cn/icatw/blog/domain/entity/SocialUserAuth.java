package cn.icatw.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 三方登录-用户关联表(SocialUserAuth)实体类
 *
 * @author icatw
 * @since 2024-04-12 11:04:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_social_user_auth")
@Builder
public class SocialUserAuth implements Serializable {
    @Serial
    private static final long serialVersionUID = 718995026106023411L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户表id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 三方用户表id
     */
    @TableField(value = "social_user_id")
    private String socialUserId;
}

