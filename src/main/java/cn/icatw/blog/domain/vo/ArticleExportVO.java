package cn.icatw.blog.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/4/17
 */
@Data
public class ArticleExportVO {
    private List<Integer> idList;
    private String type;
}
