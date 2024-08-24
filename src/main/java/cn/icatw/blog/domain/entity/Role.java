package cn.icatw.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * (Role)实体类
 *
 * @author icatw
 * @since 2024-03-21 11:22:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_role")
@Builder
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 264001206483911915L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名
     */
    @TableField(value = "role_name")
    private String roleName;

    /**
     * 角色描述
     */
    @TableField(value = "role_label")
    private String roleLabel;

    /**
     * 是否禁用  0否 1是
     */
    @TableField(value = "is_disable")
    private Integer isDisable;

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
     * 资源id列表
     */
    @TableField(exist = false)
    private List<Integer> resourceIdList;
    /**
     * 菜单id列表
     */
    @TableField(exist = false)
    private List<Integer> menuIdList;

}

