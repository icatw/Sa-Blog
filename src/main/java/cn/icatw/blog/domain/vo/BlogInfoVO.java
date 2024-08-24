package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博客信息
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "博客信息")
public class BlogInfoVO {

    /**
     * 关于我内容
     */
    @Schema(name = "aboutContent", description = "关于我内容")
    private String aboutContent;

}
