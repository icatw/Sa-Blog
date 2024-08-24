package cn.icatw.blog.domain.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/8/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    private String userId;
    private String message;
}
