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
 * (RoleMenu)实体类
 *
 * @author icatw
 * @since 2024-03-21 11:22:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_role_menu")
@Builder
public class RoleMenu implements Serializable {
    @Serial
    private static final long serialVersionUID = 460330108906357258L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private Integer roleId;

    /**
     * 菜单id
     */
    @TableField(value = "menu_id")
    private Integer menuId;
}

