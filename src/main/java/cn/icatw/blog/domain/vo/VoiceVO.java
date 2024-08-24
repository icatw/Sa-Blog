package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * 音频
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "音频")
public class VoiceVO {

    /**
     * 消息类型
     */
    @Schema(name = "type", description = "消息类型")
    private Integer type;

    /**
     * 文件
     */
    @Schema(name = "file", description = "文件")
    private MultipartFile file;

    /**
     * 用户id
     */
    @Schema(name = "userId", description = "用户id")
    private Integer userId;

    /**
     * 用户昵称
     */
    @Schema(name = "nickname", description = "用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @Schema(name = "avatar", description = "用户头像")
    private String avatar;

    /**
     * 聊天内容
     */
    @Schema(name = "content", description = "聊天内容")
    private String content;

    /**
     * 创建时间
     */
    @Schema(name = "createTime", description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 用户登录ip
     */
    @Schema(name = "ipAddress", description = "用户登录ip")
    private String ipAddress;

    /**
     * ip来源
     */
    @Schema(name = "ipSource", description = "ip来源")
    private String ipSource;

}
