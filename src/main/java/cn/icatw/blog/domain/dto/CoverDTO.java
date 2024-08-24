package cn.icatw.blog.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/4/16
 */
@Data
@Builder
public class CoverDTO {
    /**
     * 路径
     */
    private String imgurl;
    private String code;
    private String width;
    private String height;
}
