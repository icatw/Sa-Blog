package cn.icatw.blog.controller;

import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.LabelOptionDTO;
import cn.icatw.blog.domain.dto.ResourceDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.ResourceService;
import cn.icatw.blog.domain.vo.ResourceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Resource)表控制层
 *
 * @author icatw
 * @since 2024-03-21 11:31:55
 */
@RestController
@Tag(name = "资源模块", description = "资源模块")
public class ResourceController {

    /**
     * 构造方法依赖注入
     */
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Operation(summary = "导入swagger接口")
    @GetMapping("/admin/resources/import/swagger")
    public Result<?> importSwagger() {
        resourceService.importSwagger();
        return Result.ok();
    }

    /**
     * 查看资源列表
     *
     * @param params params
     * @return {@link Result}<{@link List}<{@link ResourceDTO}>>
     */
    @Operation(summary = "查看资源列表")
    @GetMapping("/admin/resources")
    public Result<List<ResourceDTO>> listResources(ConditionParams params) {
        return Result.ok(resourceService.listResources(params));
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源id
     * @return {@link Result<>}
     */
    @Operation(summary = "删除资源")
    @DeleteMapping("/admin/resources/{resourceId}")
    public Result<?> deleteResource(@PathVariable("resourceId") Integer resourceId) {
        resourceService.deleteResource(resourceId);
        return Result.ok();
    }

    /**
     * 新增或修改资源
     *
     * @param resourceVO 资源信息
     * @return {@link Result<>}
     */
    @Operation(summary = "新增或修改资源")
    @PostMapping("/admin/resources")
    public Result<?> saveOrUpdateResource(@RequestBody @Valid ResourceVO resourceVO) {
        resourceService.saveOrUpdateResource(resourceVO);
        return Result.ok();
    }

    /**
     * 查看角色资源选项
     *
     * @return {@link Result<LabelOptionDTO>} 角色资源选项
     */
    @Operation(summary = "查看角色资源选项", description = "查看角色资源选项")
    @GetMapping("/admin/role/resources")
    public Result<List<LabelOptionDTO>> listResourceOption() {
        return Result.ok(resourceService.listResourceOption());
    }
}

