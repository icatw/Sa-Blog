package cn.icatw.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (Message)实体类
 *
 * @author icatw
 * @since 2024-03-22 20:39:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_message")
public class Message implements Serializable {
    private static final long serialVersionUID = 368607356287438845L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 留言内容
     */
    @TableField(value = "message_content")
    private String messageContent;

    /**
     * 用户ip
     */
    @TableField(value = "ip_address")
    private String ipAddress;

    /**
     * 用户地址
     */
    @TableField(value = "ip_source")
    private String ipSource;

    /**
     * 弹幕速度
     */
    @TableField(value = "time")
    private Integer time;

    /**
     * 是否审核
     */
    @TableField(value = "is_review")
    private Integer isReview;

    /**
     * 发布时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

