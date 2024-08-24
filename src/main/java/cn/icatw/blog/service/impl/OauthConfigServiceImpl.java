package cn.icatw.blog.service.impl;

import cn.hutool.core.text.StrFormatter;
import cn.icatw.blog.domain.entity.OauthConfig;
import cn.icatw.blog.enums.LoginTypeEnum;
import cn.icatw.blog.excetion.BizException;
import cn.icatw.blog.mapper.OauthConfigMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.OauthConfigService;
import cn.icatw.blog.service.RedisService;
import cn.icatw.blog.utils.BeanCopyUtil;
import cn.icatw.blog.utils.PageUtil;
import cn.icatw.blog.domain.vo.OauthInfoVo;
import cn.icatw.blog.domain.vo.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.icatw.blog.constant.RedisPrefixConst.OAUTH_CONFIG;

/**
 * 三方登录配置表(OauthConfig)表服务实现类
 *
 * @author icatw
 * @since 2024-04-16 17:22:56
 */
@Service("oauthConfigService")
public class OauthConfigServiceImpl extends ServiceImpl<OauthConfigMapper, OauthConfig> implements OauthConfigService {
    @Resource
    RedisService redisService;

    @Override
    public void deleteOauth(List<Integer> oauthId) {
        baseMapper.deleteBatchIds(oauthId);
    }

    @Override
    public void saveOrUpdateOauth(OauthInfoVo oauthInfoVo) {
        OauthConfig oauthConfig = BeanCopyUtil.copyProperties(oauthInfoVo, OauthConfig.class);
        oauthConfig.setType(LoginTypeEnum.getLoginType(oauthInfoVo.getOauthName()));
        //修改，type和name不能重复
        OauthConfig exist = getOne(new LambdaQueryWrapper<OauthConfig>().eq(OauthConfig::getType, oauthConfig.getType())
                .eq(OauthConfig::getOauthName, oauthConfig.getOauthName()));
        if (exist != null && !exist.getId().equals(oauthConfig.getId())) {
            throw new BizException(StrFormatter.format("{}登录方式已存在！", oauthConfig.getOauthName()));
        }
        redisService.del(OAUTH_CONFIG);
        this.saveOrUpdate(oauthConfig);
    }

    @Override
    public PageResult<OauthInfoVo> listOauth(ConditionParams conditionParams) {
        Integer count = baseMapper.countOauth(conditionParams);
        if (count == 0) {
            return new PageResult<>();
        }
        List<OauthInfoVo> oauthInfoVos = baseMapper.listOauth(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionParams);
        return new PageResult<>(oauthInfoVos, count);
    }
}

