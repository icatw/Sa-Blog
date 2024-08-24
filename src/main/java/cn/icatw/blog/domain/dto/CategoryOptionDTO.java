package cn.icatw.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类别选项dto
 *
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @date 2024/03/26
 * @since 2024/3/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryOptionDTO {
    private Integer id;
    private String categoryName;
}
