package cn.icatw.blog.controller;

import cn.icatw.blog.annotation.OptLog;
import cn.icatw.blog.common.Result;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.OauthConfigService;
import cn.icatw.blog.domain.vo.OauthInfoVo;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.icatw.blog.constant.OptTypeConst.REMOVE;
import static cn.icatw.blog.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/4/16
 */
@RestController
public class OauthController {
    @Resource
    OauthConfigService oauthConfigService;

    /**
     * 删除三方登录信息
     *
     * @param oauthIdList oauth列表
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @Operation(summary = "删除三方登录信息")
    @DeleteMapping("/admin/oauth")
    public Result<?> deleteOauth(@RequestBody List<Integer> oauthIdList) {
        oauthConfigService.deleteOauth(oauthIdList);
        return Result.ok();
    }
    /**
     * 保存或更新三方登录信息
     *
     * @param oauthInfoVo 三方登录信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @Operation(summary = "保存或更新")
    @PostMapping("/admin/oauth")
    public Result<?> saveOrUpdatePage(@Valid @RequestBody OauthInfoVo oauthInfoVo) {
        oauthConfigService.saveOrUpdateOauth(oauthInfoVo);
        return Result.ok();
    }

    /**
     * 获取三方登录信息列表
     *
     * @return {@link Result<PageVO>}
     */
    @Operation(summary = "获取三方登录信息列表")
    @GetMapping("/admin/oauth")
    public Result<PageResult<OauthInfoVo>> listOauth(ConditionParams conditionParams) {
        return Result.ok(oauthConfigService.listOauth(conditionParams));
    }
}
