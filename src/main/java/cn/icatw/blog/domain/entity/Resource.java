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
 * (Resource)实体类
 *
 * @author icatw
 * @since 2024-03-21 11:22:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_resource")
@Builder
public class Resource implements Serializable {
    @Serial
    private static final long serialVersionUID = -36745111624452593L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 资源名
     */
    @TableField(value = "resource_name")
    private String resourceName;

    /**
     * 权限路径
     */
    @TableField(value = "url")
    private String url;

    /**
     * 请求方式
     */
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * 父权限id
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 是否匿名访问 0否 1是
     */
    @TableField(value = "is_anonymous")
    private Integer isAnonymous;

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

}

