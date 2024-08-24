package cn.icatw.blog.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.icatw.blog.domain.entity.UniqueView;
import cn.icatw.blog.domain.dto.UniqueViewDTO;
import cn.icatw.blog.mapper.UniqueViewMapper;
import cn.icatw.blog.service.RedisService;
import cn.icatw.blog.service.UniqueViewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static cn.icatw.blog.constant.RedisPrefixConst.UNIQUE_VISITOR;
import static cn.icatw.blog.constant.RedisPrefixConst.VISITOR_AREA;
import static cn.icatw.blog.enums.ZoneEnum.SHANGHAI;

/**
 * (UniqueView)表服务实现类
 *
 * @author icatw
 * @since 2024-03-22 21:02:30
 */
@Service("uniqueViewService")
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements UniqueViewService {
    @Resource
    private RedisService redisService;

    @Override
    public List<UniqueViewDTO> listUniqueViewDTO() {
        DateTime startTime = DateUtil.offsetDay(DateUtil.date(), -7);
        DateTime endTime = DateUtil.date();
        return baseMapper.listUniqueViewDTO(startTime, endTime);
    }

    @Scheduled(cron = " 0 0 0 * * ?", zone = "Asia/Shanghai")
    public void saveUniqueView() {
        // 获取每天用户量
        Long count = redisService.sSize(UNIQUE_VISITOR);
        // 获取昨天日期插入数据
        UniqueView uniqueView = UniqueView.builder()
                .createTime(LocalDateTimeUtil.offset(LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())), -1, ChronoUnit.DAYS))
                .viewsCount(Optional.of(count.intValue()).orElse(0))
                .build();
        baseMapper.insert(uniqueView);
    }

    @Scheduled(cron = " 0 1 0 * * ?", zone = "Asia/Shanghai")
    public void clear() {
        // 清空redis访客记录
        redisService.del(UNIQUE_VISITOR);
        // 清空redis游客区域统计
        redisService.del(VISITOR_AREA);
    }
}

