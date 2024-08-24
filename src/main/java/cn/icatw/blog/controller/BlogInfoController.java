package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.constant.OptTypeConst;
import cn.icatw.blog.domain.dto.BlogBackInfoDTO;
import cn.icatw.blog.domain.dto.BlogHomeInfoDTO;
import cn.icatw.blog.domain.dto.UserAreaDTO;
import cn.icatw.blog.enums.FilePathEnum;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.BlogInfoService;
import cn.icatw.blog.service.impl.WebSocketServiceImpl;
import cn.icatw.blog.strategy.context.UploadStrategyContext;
import cn.icatw.blog.domain.vo.BlogInfoVO;
import cn.icatw.blog.domain.vo.VoiceVO;
import cn.icatw.blog.domain.vo.WebsiteConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/3/22
 */
@RestController
@Tag(name = "博客信息模块", description = "博客信息模块")
public class BlogInfoController {
    @Resource
    BlogInfoService blogInfoService;
    @Resource
    UploadStrategyContext uploadStrategyContext;
    @Resource
    private WebSocketServiceImpl webSocketService;

    /**
     * 保存访问信息
     *
     * @return {@link Result}<{@link ?}>
     */
    @PostMapping("visit")
    @Operation(summary = "保存访问信息", description = "保存访问信息")
    public Result<?> visit() {
        blogInfoService.visit();
        return Result.ok();
    }

    /**
     * 获取博客后台首页信息
     *
     * @return {@link Result}<{@link BlogBackInfoDTO}>
     */
    @GetMapping("/admin/backHomeInfo")
    @Operation(summary = "获取博客后台信息", description = "获取博客后台信息")
    public Result<BlogBackInfoDTO> getBlogBackInfo() {
        return Result.ok(blogInfoService.getBlogBackHomeInfo());
    }

    /**
     * 获取用户区域分布
     *
     * @param conditionParams 条件参数
     * @return {@link Result}<{@link List}<{@link UserAreaDTO}>>
     */
    @GetMapping("/admin/users/area")
    @Operation(summary = "获取用户区域分布", description = "获取用户区域分布")
    public Result<List<UserAreaDTO>> listUserAreas(ConditionParams conditionParams) {
        return Result.ok(blogInfoService.listUserAreas(conditionParams));
    }

    /**
     * 上传博客配置图片
     *
     * @param file 文件
     * @return {@link Result<String>} 博客配置图片
     */
    @Operation(summary = "上传博客配置图片")
    @PostMapping("/admin/config/images")
    public Result<String> savePhotoAlbumCover(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CONFIG.getPath()));
    }

    /**
     * 更新网站配置
     *
     * @param websiteConfigVO 网站配置信息
     * @return {@link Result}
     */
    @Operation(summary = "更新网站配置")
    @PutMapping("/admin/website/config")
    public Result<?> updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVO websiteConfigVO) {
        blogInfoService.updateWebsiteConfig(websiteConfigVO);
        return Result.ok();
    }

    /**
     * 获取网站配置
     *
     * @return {@link Result<WebsiteConfigVO>} 网站配置
     */
    @Operation(summary = "获取网站配置")
    @GetMapping("/admin/website/config")
    public Result<WebsiteConfigVO> getWebsiteConfig() {
        return Result.ok(blogInfoService.getWebsiteConfig());
    }

    /**
     * 查看关于我信息
     *
     * @return {@link Result<String>} 关于我信息
     */
    @Operation(summary = "查看关于我信息")
    @GetMapping("/about")
    public Result<String> getAbout() {
        return Result.ok(blogInfoService.getAbout());
    }

    /**
     * 修改关于我信息
     *
     * @param blogInfoVO 博客信息
     * @return {@link Result<>}
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @Operation(summary = "修改关于我信息")
    @PutMapping("/admin/about")
    public Result<?> updateAbout(@Valid @RequestBody BlogInfoVO blogInfoVO) {
        blogInfoService.updateAbout(blogInfoVO);
        return Result.ok();
    }

    /**
     * 查看博客信息
     *
     * @return {@link Result<BlogHomeInfoDTO>} 博客信息
     */
    @Operation(summary = "查看博客信息")
    @GetMapping("/")
    public Result<BlogHomeInfoDTO> getBlogHomeInfo() {
        return Result.ok(blogInfoService.getBlogHomeInfo());
    }

    /**
     * 保存语音信息
     *
     * @param voiceVO 语音信息
     * @return {@link Result<String>} 语音地址
     */
    @Operation(summary = "上传语音")
    @PostMapping("/voice")
    public Result<String> sendVoice(VoiceVO voiceVO) {
        webSocketService.sendVoice(voiceVO);
        return Result.ok();
    }
}
