package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.OauthConfig;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.OauthInfoVo;
import cn.icatw.blog.domain.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 三方登录配置表(OauthConfig)表服务接口
 *
 * @author icatw
 * @since 2024-04-16 17:22:56
 */
public interface OauthConfigService extends IService<OauthConfig> {
    /**
     * 删除oauth
     *
     * @param oauthIdList Oauth ID
     */
    void deleteOauth(List<Integer> oauthIdList);

    /**
     * 保存或更新oauth
     *
     * @param oauthInfoVo oauth信息vo
     */
    void saveOrUpdateOauth(OauthInfoVo oauthInfoVo);

    PageResult<OauthInfoVo> listOauth(ConditionParams conditionParams);
}

