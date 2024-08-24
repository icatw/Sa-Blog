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
 * (UserInfo)实体类
 *
 * @author icatw
 * @since 2024-03-21 11:22:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user_info")
@Builder
public class UserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -95340162453456943L;
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 邮箱号
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 用户头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 用户简介
     */
    @TableField(value = "intro")
    private String intro;

    /**
     * 个人网站
     */
    @TableField(value = "web_site")
    private String webSite;

    /**
     * 是否禁用
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
}

