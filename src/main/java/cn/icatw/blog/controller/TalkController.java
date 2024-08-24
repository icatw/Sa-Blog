package cn.icatw.blog.controller;

import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.dto.TalkBackDTO;
import cn.icatw.blog.domain.dto.TalkDTO;
import cn.icatw.blog.enums.FilePathEnum;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.TalkService;
import cn.icatw.blog.strategy.context.UploadStrategyContext;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.TalkVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (Talk)表控制层
 *
 * @author icatw
 * @since 2024-03-22 21:02:31
 */
@RestController
@Tag(name = "说说模块", description = "说说模块")
public class TalkController {

    /**
     * 构造方法依赖注入
     */
    private final TalkService talkService;
    private final UploadStrategyContext uploadStrategyContext;

    public TalkController(TalkService talkService, UploadStrategyContext uploadStrategyContext) {
        this.talkService = talkService;
        this.uploadStrategyContext = uploadStrategyContext;
    }

    /**
     * 查看首页说说
     *
     * @return {@link Result<String>}
     */
    @Operation(summary = "查看首页说说")
    @GetMapping("/home/talks")
    public Result<List<String>> listHomeTalks() {
        return Result.ok(talkService.listHomeTalks());
    }

    /**
     * 查看说说列表
     *
     * @return {@link Result<TalkDTO>}
     */
    @Operation(summary = "查看说说列表")
    @GetMapping("/talks")
    public Result<PageResult<TalkDTO>> listTalks() {
        return Result.ok(talkService.listTalks());
    }

    /**
     * 根据id查看说说
     *
     * @param talkId 说说id
     * @return {@link Result<TalkDTO>}
     */
    @Operation(summary = "根据id查看说说")
    @GetMapping("/talks/{talkId}")
    public Result<TalkDTO> getTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.ok(talkService.getTalkById(talkId));
    }

    /**
     * 点赞说说
     *
     * @param talkId 说说id
     * @return {@link Result<>}
     */
    @Operation(summary = "点赞说说")
    @PostMapping("/talks/{talkId}/like")
    public Result<?> saveTalkLike(@PathVariable("talkId") Integer talkId) {
        talkService.saveTalkLike(talkId);
        return Result.ok();
    }

    /**
     * 上传说说图片
     *
     * @param file 文件
     * @return {@link Result<String>} 说说图片地址
     */
    @Operation(summary = "上传说说图片")
    @PostMapping("/admin/talks/images")
    public Result<String> saveTalkImages(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.TALK.getPath()));
    }

    /**
     * 保存或修改说说
     *
     * @param talkVO 说说信息
     * @return {@link Result<>}
     */
    @Operation(summary = "保存或修改说说")
    @PostMapping("/admin/talks")
    public Result<?> saveOrUpdateTalk(@Valid @RequestBody TalkVO talkVO) {
        talkService.saveOrUpdateTalk(talkVO);
        return Result.ok();
    }

    /**
     * 删除说说
     *
     * @param talkIdList 说说id列表
     * @return {@link Result<>}
     */
    @Operation(summary = "删除说说")
    @DeleteMapping("/admin/talks")
    public Result<?> deleteTalks(@RequestBody List<Integer> talkIdList) {
        talkService.deleteTalks(talkIdList);
        return Result.ok();
    }

    /**
     * 查看后台说说
     *
     * @param conditionVO 条件
     * @return {@link Result<TalkBackDTO>} 说说列表
     */
    @Operation(summary = "查看后台说说")
    @GetMapping("/admin/talks")
    public Result<PageResult<TalkBackDTO>> listBackTalks(ConditionParams conditionVO) {
        return Result.ok(talkService.listBackTalks(conditionVO));
    }

    /**
     * 根据id查看后台说说
     *
     * @param talkId 说说id
     * @return {@link Result<TalkDTO>}
     */
    @Operation(summary = "根据id查看后台说说")
    @GetMapping("/admin/talks/{talkId}")
    public Result<TalkBackDTO> getBackTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.ok(talkService.getBackTalkById(talkId));
    }

}

