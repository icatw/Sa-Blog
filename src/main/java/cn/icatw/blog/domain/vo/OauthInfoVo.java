package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/4/16
 */
@Data
@Schema(description = "三方登录信息")
public class OauthInfoVo {

    /**
     * id
     */
    @Schema(name = "id", description = "id")
    private Integer id;

    /**
     * 类型id
     */
    @Schema(name = "type", description = "类型")
    private Integer type;
    /**
     * 类型名
     */
    @Schema(name = "oauthName", description = "三方名")
    private String oauthName;

    /**
     * clientId
     */
    @Schema(name = "clientId", description = "clientId")
    private String clientId;

    /**
     * clientSecret
     */
    @Schema(name = "clientSecret", description = "clientSecret")
    private String clientSecret;

    /**
     * 回调地址
     */
    @Schema(name = "redirectUri", description = "回调地址")
    private String redirectUri;

    /**
     * 是否启用
     */
    @Schema(name = "isDisable", description = "是否启用")
    private Integer isDisable;
    /**
     * 创建时间
     */
    @Schema(name = "createTime", description = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @Schema(name = "updateTime", description = "更新时间")
    private LocalDateTime updateTime;
    /**
     * 应用程序名称
     */
    @Schema(name = "appName", description = "应用程序名称")
    private String appName;
}
