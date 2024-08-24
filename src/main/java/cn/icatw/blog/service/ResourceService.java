package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.Resource;
import cn.icatw.blog.domain.dto.LabelOptionDTO;
import cn.icatw.blog.domain.dto.ResourceDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.ResourceVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Resource)表服务接口
 *
 * @author icatw
 * @date 2024/04/02
 * @since 2024-03-21 11:22:24
 */
public interface ResourceService extends IService<Resource> {
    /**
     * 后台资源列表
     *
     * @param params params
     * @return {@link List}<{@link ResourceDTO}>
     */
    List<ResourceDTO> listResources(ConditionParams params);

    /**
     * 删除资源
     *
     * @param resourceId 资源id
     */
    void deleteResource(Integer resourceId);

    /**
     * 保存或更新资源
     *
     * @param resourceVO 资源vo
     */
    void saveOrUpdateResource(ResourceVO resourceVO);

    /**
     * 列出资源选项
     *
     * @return {@link List}<{@link LabelOptionDTO}>
     */
    List<LabelOptionDTO> listResourceOption();

    /**
     * 导入swagger接口
     */
    void importSwagger();

    /**
     * 是否为匿名接口
     *
     * @param requestURI 请求uri
     * @param method     方法
     * @return {@link Boolean}
     */
    Boolean isAnonymous(String requestURI, String method);
}

