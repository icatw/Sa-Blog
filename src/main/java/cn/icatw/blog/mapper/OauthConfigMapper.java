package cn.icatw.blog.mapper;

import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.OauthInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.OauthConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 三方登录配置表(OauthConfig)表数据库访问层
 *
 * @author icatw
 * @since 2024-04-16 17:22:56
 */
public interface OauthConfigMapper extends BaseMapper<OauthConfig> {
    Integer countOauth(@Param("conditionParams") ConditionParams conditionParams);

    List<OauthInfoVo> listOauth(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("conditionParams") ConditionParams conditionParams);

}

