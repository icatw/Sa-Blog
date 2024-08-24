package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.UniqueView;
import cn.icatw.blog.domain.dto.UniqueViewDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (UniqueView)表服务接口
 *
 * @author icatw
 * @since 2024-03-22 21:02:30
 */
public interface UniqueViewService extends IService<UniqueView> {
    /**
     * 最近一周用户访问量列表
     *
     * @return {@link List}<{@link UniqueViewDTO}>
     */
    List<UniqueViewDTO> listUniqueViewDTO();
}

