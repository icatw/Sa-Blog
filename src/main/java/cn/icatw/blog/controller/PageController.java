package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.service.PageService;
import cn.icatw.blog.domain.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.icatw.blog.constant.OptTypeConst.REMOVE;
import static cn.icatw.blog.constant.OptTypeConst.SAVE_OR_UPDATE;


/**
 * 页面控制器
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Tag(name = "页面模块", description = "页面模块")
@RestController
public class PageController {
    @Resource
    private PageService pageService;

    /**
     * 删除页面
     *
     * @param pageId 页面id
     * @return {@link Result <>}
     */
    @OptLog(optType = REMOVE)
    @Operation(summary = "删除页面")
    @DeleteMapping("/admin/pages/{pageId}")
    public Result<?> deletePage(@PathVariable("pageId") Integer pageId) {
        pageService.deletePage(pageId);
        return Result.ok();
    }

    /**
     * 保存或更新页面
     *
     * @param pageVO 页面信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @Operation(summary = "保存或更新页面")
    @PostMapping("/admin/pages")
    public Result<?> saveOrUpdatePage(@Valid @RequestBody PageVO pageVO) {
        pageService.saveOrUpdatePage(pageVO);
        return Result.ok();
    }

    /**
     * 获取页面列表
     *
     * @return {@link Result<PageVO>}
     */
    @Operation(summary = "获取页面列表")
    @GetMapping("/admin/pages")
    public Result<List<PageVO>> listPages() {
        return Result.ok(pageService.listPages());
    }

}
