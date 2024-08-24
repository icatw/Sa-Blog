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
 * (Category)实体类
 *
 * @author icatw
 * @since 2024-03-22 20:39:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_category")
@Builder
public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = -55817687502487198L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名
     */
    @TableField(value = "category_name")
    private String categoryName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}

