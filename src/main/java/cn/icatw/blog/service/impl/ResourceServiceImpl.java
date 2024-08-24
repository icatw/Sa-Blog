package cn.icatw.blog.service.impl;

import cn.icatw.blog.domain.entity.Resource;
import cn.icatw.blog.domain.entity.RoleResource;
import cn.icatw.blog.domain.dto.LabelOptionDTO;
import cn.icatw.blog.domain.dto.ResourceDTO;
import cn.icatw.blog.mapper.ResourceMapper;
import cn.icatw.blog.mapper.RoleResourceMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.ResourceService;
import cn.icatw.blog.strategy.MySourceSafilterAuthStrategy;
import cn.icatw.blog.utils.AssertUtil;
import cn.icatw.blog.utils.BeanCopyUtil;
import cn.icatw.blog.domain.vo.ResourceVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.icatw.blog.constant.CommonConst.FALSE;
import static cn.icatw.blog.constant.CommonConst.TRUE;

/**
 * (Resource)表服务实现类
 *
 * @author icatw
 * @date 2024/04/02
 * @since 2024-03-21 11:22:24
 */
@Service("resourceService")
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    private final RoleResourceMapper roleResourceMapper;
    private final RestTemplate restTemplate;
    private final MySourceSafilterAuthStrategy mySourceSafilterAuthStrategy;

    public ResourceServiceImpl(RestTemplate restTemplate, RoleResourceMapper roleResourceMapper, MySourceSafilterAuthStrategy mySourceSafilterAuthStrategy) {
        this.restTemplate = restTemplate;
        this.roleResourceMapper = roleResourceMapper;
        this.mySourceSafilterAuthStrategy = mySourceSafilterAuthStrategy;
    }

    /**
     * 资源列表
     *
     * @param params params
     * @return {@link List}<{@link ResourceDTO}>
     */
    @Override
    public List<ResourceDTO> listResources(ConditionParams params) {
        // 查询资源列表
        List<Resource> resourceList = baseMapper.selectList(new LambdaQueryWrapper<Resource>()
                .like(StringUtils.isNotBlank(params.getKeywords()), Resource::getResourceName, params.getKeywords()));
        // 获取所有模块
        List<Resource> parentList = listResourceModule(resourceList);
        // 根据父id分组获取模块下的资源
        Map<Integer, List<Resource>> childrenMap = listResourceChildren(resourceList);
        // 绑定模块下的所有接口
        List<ResourceDTO> resourceDTOList = parentList.stream().map(item -> {
            ResourceDTO resourceDTO = BeanCopyUtil.copyProperties(item, ResourceDTO.class);
            List<ResourceDTO> childrenList = BeanCopyUtil.copyListToList(childrenMap.get(item.getId()), ResourceDTO.class);
            resourceDTO.setChildren(childrenList);
            childrenMap.remove(item.getId());
            return resourceDTO;
        }).collect(Collectors.toList());
        // 若还有资源未取出则拼接
        if (CollectionUtils.isNotEmpty(childrenMap)) {
            List<Resource> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<ResourceDTO> childrenDTOList = childrenList.stream()
                    .map(item -> BeanCopyUtil.copyProperties(item, ResourceDTO.class))
                    .toList();
            resourceDTOList.addAll(childrenDTOList);
        }
        return resourceDTOList;
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源id
     */
    @Override
    public void deleteResource(Integer resourceId) {
        // 查询是否有角色关联
        long count = roleResourceMapper.selectCount(new LambdaQueryWrapper<RoleResource>()
                .eq(RoleResource::getResourceId, resourceId));
        AssertUtil.isTrue(count == 0, "该资源下存在角色，不允许删除！");
        // 删除子资源
        List<Integer> resourceIdList = baseMapper.selectList(new LambdaQueryWrapper<Resource>()
                        .select(Resource::getId).
                        eq(Resource::getParentId, resourceId))
                .stream()
                .map(Resource::getId)
                .collect(Collectors.toList());
        resourceIdList.add(resourceId);
        baseMapper.deleteBatchIds(resourceIdList);
    }

    /**
     * 保存或更新资源
     *
     * @param resourceVO 资源vo
     */
    @Override
    public void saveOrUpdateResource(ResourceVO resourceVO) {
        // 更新资源信息
        Resource resource = BeanCopyUtil.copyProperties(resourceVO, Resource.class);
        this.saveOrUpdate(resource);
        // 重新加载角色资源信息
        mySourceSafilterAuthStrategy.clearDataSource();
    }

    /**
     * 列出资源选项
     *
     * @return {@link List}<{@link LabelOptionDTO}>
     */
    @Override
    public List<LabelOptionDTO> listResourceOption() {
        // 查询资源列表
        List<Resource> resourceList = baseMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getResourceName, Resource::getParentId)
                .eq(Resource::getIsAnonymous, FALSE));
        // 获取所有模块
        List<Resource> parentList = listResourceModule(resourceList);
        // 根据父id分组获取模块下的资源
        Map<Integer, List<Resource>> childrenMap = listResourceChildren(resourceList);
        // 组装父子数据
        return parentList.stream().map(item -> {
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Resource> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                list = children.stream()
                        .map(resource -> LabelOptionDTO.builder()
                                .id(resource.getId())
                                .label(resource.getResourceName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder()
                    .id(item.getId())
                    .label(item.getResourceName())
                    .children(list)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importSwagger() {
        // 删除所有资源
        this.remove(null);
        roleResourceMapper.delete(null);
        List<Resource> resourceList = new ArrayList<>();
        Map<String, Object> data = restTemplate.getForObject("http://localhost:8080/v3/api-docs/default", Map.class);
        // 获取所有模块
        List<Map<String, String>> tagList = (List<Map<String, String>>) data.get("tags");
        tagList.forEach(item -> {
            Resource resource = Resource.builder()
                    .resourceName(item.get("name"))
                    .isAnonymous(FALSE)
                    .build();
            resourceList.add(resource);
        });
        this.saveBatch(resourceList);
        Map<String, Integer> permissionMap = resourceList.stream()
                .collect(Collectors.toMap(Resource::getResourceName, Resource::getId));
        resourceList.clear();
        // 获取所有接口
        Map<String, Map<String, Map<String, Object>>> path = (Map<String, Map<String, Map<String, Object>>>) data.get("paths");
        for (Map.Entry<String, Map<String, Map<String, Object>>> entry : path.entrySet()) {
            try {
                String url = entry.getKey();
                Map<String, Map<String, Object>> value = entry.getValue();
                for (Map.Entry<String, Map<String, Object>> e : value.entrySet()) {

                    String requestMethod = e.getKey();
                    Map<String, Object> info = e.getValue();
                    String permissionName = info.get("summary").toString();
                    List<String> tag = (List<String>) info.get("tags");
                    Integer parentId = permissionMap.get(tag.get(0));
                    Resource resource = Resource.builder()
                            .resourceName(permissionName)
                            .url(url.replaceAll("\\{[^}]*}", "*"))
                            .parentId(parentId)
                            .requestMethod(requestMethod.toUpperCase())
                            .isAnonymous(FALSE)
                            .build();
                    resourceList.add(resource);
                }
            } catch (Exception ex) {
                log.error("数据异常" + entry);
                //throw new RuntimeException(ex);
            }
        }
        this.saveBatch(resourceList);
    }

    @Override
    public Boolean isAnonymous(String requestURI, String method) {
        return baseMapper.selectCount(new LambdaQueryWrapper<Resource>()
                .eq(Resource::getUrl, requestURI)
                .eq(Resource::getRequestMethod, method)
                .eq(Resource::getIsAnonymous, TRUE)) > 0;
    }

    /**
     * 列出资源子项
     *
     * @param resourceList 资源列表
     * @return {@link Map}<{@link Integer}, {@link List}<{@link Resource}>>
     */
    private Map<Integer, List<Resource>> listResourceChildren(List<Resource> resourceList) {
        //根据父id分组
        return resourceList.stream().filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Resource::getParentId));
    }

    /**
     * 获取所有资源模块
     *
     * @param resourceList 资源列表
     * @return {@link List}<{@link Resource}>
     */
    private List<Resource> listResourceModule(List<Resource> resourceList) {
        return resourceList.stream().filter(item -> Objects.isNull(item.getParentId())).toList();
    }
}

