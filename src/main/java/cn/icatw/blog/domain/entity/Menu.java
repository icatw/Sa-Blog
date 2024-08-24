package cn.icatw.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (Menu)实体类
 *
 * @author icatw
 * @since 2024-03-22 09:11:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_menu")
public class Menu  implements Serializable{
    @Serial
    private static final long serialVersionUID = 437961954681587606L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 菜单路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 组件
     */
    @TableField(value = "component")
    private String component;

    /**
     * 菜单icon
     */
    @TableField(value = "icon")
    private String icon;

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
     * 排序
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 父id
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 是否隐藏  0否1是
     */
    @TableField(value = "is_hidden")
    private Integer isHidden;
}

