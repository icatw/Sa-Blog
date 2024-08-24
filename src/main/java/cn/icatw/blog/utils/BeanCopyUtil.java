package cn.icatw.blog.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote Bean复制工具类
 * @since 2024/3/28
 */
@Slf4j
public class BeanCopyUtil extends BeanUtil {
    /**
     * 将列表复制到列表
     *
     * @param source 来源
     * @param target 目标
     * @return {@link List}<{@link K}>
     */
    public static <T, K> List<K> copyListToList(List<T> source, Class<K> target) {
        if (CollUtil.isEmpty(source)) {
            log.error("source is empty");
            return new ArrayList<>();
        }
        return source.stream().map(
                item -> BeanUtil.copyProperties(item, target)
        ).toList();
    }
}
