package cn.icatw.blog.domain.params;

import lombok.*;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/4/2
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserConditionParams extends ConditionParams {
    private Integer loginType;
}
