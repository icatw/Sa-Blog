package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.TagBackDTO;
import cn.icatw.blog.domain.dto.TagDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.TagService;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.TagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.icatw.blog.constant.OptTypeConst.REMOVE;
import static cn.icatw.blog.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * (Tag)表控制层
 *
 * @author icatw
 * @since 2024-03-22 20:54:59
 */
@RestController
@Tag(name = "标签模块", description = "标签模块")
public class TagController {

    /**
     * 构造方法依赖注入
     */
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * 搜索标签列表
     *
     * @param params params
     * @return {@link Result}<{@link List}<{@link TagDTO}>>
     */
    @GetMapping("/admin/tags/search")
    @Operation(summary = "标签列表（搜索用）")
    public Result<List<TagDTO>> listTagsBySearch(ConditionParams params) {
        return Result.ok(tagService.listTagsBySearch(params));
    }

    /**
     * 查询标签列表
     *
     * @return {@link Result<TagDTO>} 标签列表
     */
    @Operation(summary = "查询标签列表")
    @GetMapping("/tags")
    public Result<PageResult<TagDTO>> listTags() {
        return Result.ok(tagService.listTags());
    }

    /**
     * 查询后台标签列表
     *
     * @param condition 条件
     * @return {@link Result<TagBackDTO>} 标签列表
     */
    @Operation(summary = "查询后台标签列表")
    @GetMapping("/admin/tags")
    public Result<PageResult<TagBackDTO>> listTagBackDTO(ConditionParams condition) {
        return Result.ok(tagService.listTagBackDTO(condition));
    }

    /**
     * 添加或修改标签
     *
     * @param tagVO 标签信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @Operation(summary = "添加或修改标签")
    @PostMapping("/admin/tags")
    public Result<?> saveOrUpdateTag(@Valid @RequestBody TagVO tagVO) {
        tagService.saveOrUpdateTag(tagVO);
        return Result.ok();
    }

    /**
     * 删除标签
     *
     * @param tagIdList 标签id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @Operation(summary = "删除标签")
    @DeleteMapping("/admin/tags")
    public Result<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return Result.ok();
    }
}

