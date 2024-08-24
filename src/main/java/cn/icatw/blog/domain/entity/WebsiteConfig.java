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
 * (WebsiteConfig)实体类
 *
 * @author icatw
 * @since 2024-03-22 21:02:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_website_config")
@Builder
public class WebsiteConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 115664312170114064L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 配置信息
     */
    @TableField(value = "config")
    private String config;

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

