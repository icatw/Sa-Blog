package cn.icatw.blog.domain.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/3/26
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleConditionParams extends ConditionParams {
    /**
     * 文章类型
     */
    private Integer type;
    /**
     * 标签id
     */
    private Integer tagId;
    /**
     * 类别id
     */
    private Integer categoryId;

}
