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
 * 照片(Photo)实体类
 *
 * @author icatw
 * @since 2024-04-07 10:21:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_photo")
@Builder
public class Photo implements Serializable {
    @Serial
    private static final long serialVersionUID = -85159381231933999L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 相册id
     */
    @TableField(value = "album_id")
    private Integer albumId;

    /**
     * 照片名
     */
    @TableField(value = "photo_name")
    private String photoName;

    /**
     * 照片描述
     */
    @TableField(value = "photo_desc")
    private String photoDesc;

    /**
     * 照片地址
     */
    @TableField(value = "photo_src")
    private String photoSrc;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

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

