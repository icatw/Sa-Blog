package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 网站配置信息
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "网站配置")
public class WebsiteConfigVO {

    /**
     * 网站头像
     */
    @Schema(name = "websiteAvatar", description = "网站头像")
    private String websiteAvatar;

    /**
     * 网站名称
     */
    @Schema(name = "websiteName", description = "网站名称" )
    private String websiteName;

    /**
     * 网站作者
     */
    @Schema(name = "websiteAuthor", description = "网站作者" )
    private String websiteAuthor;

    /**
     * 网站介绍
     */
    @Schema(name = "websiteIntro", description = "网站介绍" )
    private String websiteIntro;

    /**
     * 网站公告
     */
    @Schema(name = "websiteNotice", description = "网站公告" )
    private String websiteNotice;

    /**
     * 网站创建时间
     */
    @Schema(name = "websiteCreateTime", description = "网站创建时间")
    private String websiteCreateTime;

    /**
     * 网站备案号
     */
    @Schema(name = "websiteRecordNo", description = "网站备案号" )
    private String websiteRecordNo;

    /**
     * 社交登录列表
     */
    @Schema(name = "socialLoginList", description = "社交登录列表")
    private List<String> socialLoginList;

    /**
     * 社交url列表
     */
    @Schema(name = "socialUrlList", description = "社交url列表")
    private List<String> socialUrlList;

    /**
     * qq
     */
    @Schema(name = "qq", description = "qq" )
    private String qq;

    /**
     * github
     */
    @Schema(name = "github", description = "github" )
    private String github;

    /**
     * gitee
     */
    @Schema(name = "gitee", description = "gitee" )
    private String gitee;

    /**
     * 游客头像
     */
    @Schema(name = "touristAvatar", description = "游客头像" )
    private String touristAvatar;

    /**
     * 用户头像
     */
    @Schema(name = "userAvatar", description = "用户头像" )
    private String userAvatar;

    /**
     * 是否评论审核
     */
    @Schema(name = "isCommentReview", description = "是否评论审核")
    private Integer isCommentReview;

    /**
     * 是否留言审核
     */
    @Schema(name = "isMessageReview", description = "是否留言审核")
    private Integer isMessageReview;
    /**
     * 是否敏感词过滤
     */
    @Schema(name = "isMessageReview", description = "是否敏感词过滤")
    private Integer isSensitiveWordFilter;

    /**
     * 是否邮箱通知
     */
    @Schema(name = "isEmailNotice", description = "是否邮箱通知")
    private Integer isEmailNotice;

    /**
     * 是否打赏
     */
    @Schema(name = "isReward", description = "是否打赏")
    private Integer isReward;

    /**
     * 微信二维码
     */
    @Schema(name = "weiXinQRCode", description = "微信二维码" )
    private String weiXinQRCode;

    /**
     * 支付宝二维码
     */
    @Schema(name = "alipayQRCode", description = "支付宝二维码" )
    private String alipayQRCode;

    /**
     * 是否开启聊天室
     */
    @Schema(name = "isChatRoom", description = "是否开启聊天室")
    private Integer isChatRoom;
    /**
     * 是否开启默认随机封面
     */
    @Schema(name = "isRandomCover", description = "是否开启默认随机封面")
    private Integer isRandomCover;

    /**
     * websocket地址
     */
    @Schema(name = "websocketUrl", description = "websocket地址" )
    private String websocketUrl;

    /**
     * 是否开启音乐
     */
    @Schema(name = "isMusicPlayer", description = "是否开启音乐")
    private Integer isMusicPlayer;

}
