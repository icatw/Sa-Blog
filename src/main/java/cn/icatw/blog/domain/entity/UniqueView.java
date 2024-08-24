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
 * (UniqueView)实体类
 *
 * @author icatw
 * @since 2024-03-22 21:02:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_unique_view")
@Builder
public class UniqueView implements Serializable {
    @Serial
    private static final long serialVersionUID = 979038170037632057L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 访问量
     */
    @TableField(value = "views_count")
    private Integer viewsCount;
}

