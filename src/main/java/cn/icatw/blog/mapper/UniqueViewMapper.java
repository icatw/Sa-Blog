package cn.icatw.blog.mapper;

import cn.hutool.core.date.DateTime;
import cn.icatw.blog.domain.dto.UniqueViewDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.UniqueView;

import java.util.List;

/**
 * (UniqueView)表数据库访问层
 *
 * @author icatw
 * @since 2024-03-22 21:02:30
 */
public interface UniqueViewMapper extends BaseMapper<UniqueView> {

    /**
     * 查询给定时间范围用户访问量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List}<{@link UniqueViewDTO}>
     */
    List<UniqueViewDTO> listUniqueViewDTO(DateTime startTime, DateTime endTime);
}

