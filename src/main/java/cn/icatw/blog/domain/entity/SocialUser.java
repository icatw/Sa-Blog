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
 * 社会化用户表(SocialUser)实体类
 *
 * @author icatw
 * @since 2024-04-12 10:36:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_social_user")
@Builder
public class SocialUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 829588886254470304L;
    /**
     * 主键uuid
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 第三方uuid
     */
    @TableField(value = "uuid")
    private String uuid;

    /**
     * 第三方来源(1:qq,2:gitee,3:github)
     */
    @TableField(value = "source")
    private Integer source;

    /**
     * 用户授权令牌
     */
    @TableField(value = "access_token")
    private String accessToken;
}

