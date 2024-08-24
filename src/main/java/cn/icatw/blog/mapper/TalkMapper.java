package cn.icatw.blog.mapper;

import cn.icatw.blog.domain.dto.TalkBackDTO;
import cn.icatw.blog.domain.dto.TalkDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.Talk;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Talk)表数据库访问层
 *
 * @author icatw
 * @since 2024-03-22 21:02:31
 */
public interface TalkMapper extends BaseMapper<Talk> {
    /**
     * 说说列表
     *
     * @param current 现在
     * @param size    大小
     * @return {@link List}<{@link TalkDTO}>
     */
    List<TalkDTO> listTalks(@Param("current") Long current, @Param("size") Long size);

    /**
     * 通过id获取说说
     *
     * @param talkId talk id
     * @return {@link TalkDTO}
     */
    TalkDTO getTalkById(@Param("talkId") Integer talkId);

    /**
     * 分页说说列表
     *
     * @param current 现在
     * @param size    大小
     * @param params  params
     * @return {@link List}<{@link TalkBackDTO}>
     */
    List<TalkBackDTO> listBackTalks(@Param("current") Long current, @Param("size") Long size, @Param("params") ConditionParams params);

    /**
     * 根据说说id查看说说
     *
     * @param talkId talk id
     * @return {@link TalkBackDTO}
     */
    TalkBackDTO getBackTalkById(@Param("talkId") Integer talkId);
}

