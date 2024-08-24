package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.Talk;
import cn.icatw.blog.domain.dto.TalkBackDTO;
import cn.icatw.blog.domain.dto.TalkDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.TalkVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Talk)表服务接口
 *
 * @author icatw
 * @since 2024-03-22 21:02:31
 */
public interface TalkService extends IService<Talk> {
    /**
     * 查看首页说说
     *
     * @return {@link List}<{@link String}>
     */
    List<String> listHomeTalks();

    /**
     * 说说列表
     *
     * @return {@link PageResult}<{@link TalkDTO}>
     */
    PageResult<TalkDTO> listTalks();

    /**
     * 通过id获取说说
     *
     * @param talkId talk id
     * @return {@link TalkDTO}
     */
    TalkDTO getTalkById(Integer talkId);

    /**
     * 点赞说说
     *
     * @param talkId talk id
     */
    void saveTalkLike(Integer talkId);

    /**
     * 保存或更新说说
     *
     * @param talkVO talk vo
     */
    void saveOrUpdateTalk(TalkVO talkVO);

    /**
     * 删除说说
     *
     * @param talkIdList 通话id列表
     */
    void deleteTalks(List<Integer> talkIdList);

    /**
     * 后台说说列表
     *
     * @param conditionVO 条件vo
     * @return {@link PageResult}<{@link TalkBackDTO}>
     */
    PageResult<TalkBackDTO> listBackTalks(ConditionParams conditionVO);

    /**
     * 根据id查看后台说说
     *
     * @param talkId talk id
     * @return {@link TalkBackDTO}
     */
    TalkBackDTO getBackTalkById(Integer talkId);
}

