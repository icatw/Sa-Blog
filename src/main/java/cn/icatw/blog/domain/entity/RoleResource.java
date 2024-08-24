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
 * (RoleResource)实体类
 *
 * @author icatw
 * @since 2024-03-21 11:22:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_role_resource")
@Builder
public class RoleResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 748083034215470417L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private Integer roleId;

    /**
     * 权限id
     */
    @TableField(value = "resource_id")
    private Integer resourceId;
}

