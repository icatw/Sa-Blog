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
 * 相册(PhotoAlbum)实体类
 *
 * @author icatw
 * @since 2024-04-07 10:21:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_photo_album")
@Builder
public class PhotoAlbum implements Serializable {
    @Serial
    private static final long serialVersionUID = -63528934064963477L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 相册名
     */
    @TableField(value = "album_name")
    private String albumName;

    /**
     * 相册描述
     */
    @TableField(value = "album_desc")
    private String albumDesc;

    /**
     * 相册封面
     */
    @TableField(value = "album_cover")
    private String albumCover;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 状态值 1公开 2私密
     */
    @TableField(value = "status")
    private Integer status;

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

