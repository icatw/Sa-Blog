package cn.icatw.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * (ArticleTag)实体类
 *
 * @author icatw
 * @since 2024-03-27 14:57:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_article_tag")
@Builder
public class ArticleTag implements Serializable {
    @Serial
    private static final long serialVersionUID = 457643201821458429L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章id
     */
    @TableField(value = "article_id")
    private Integer articleId;

    /**
     * 标签id
     */
    @TableField(value = "tag_id")
    private Integer tagId;
}

