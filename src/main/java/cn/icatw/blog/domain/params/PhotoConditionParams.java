package cn.icatw.blog.domain.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PhotoConditionParams extends ConditionParams {
    private Integer albumId;
}
