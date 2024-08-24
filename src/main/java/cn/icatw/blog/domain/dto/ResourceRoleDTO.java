package cn.icatw.blog.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 资源角色
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
public class ResourceRoleDTO {

    /**
     * 资源id
     */
    private Integer id;

    /**
     * 路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 角色名
     */
    private List<String> roleList;

}
