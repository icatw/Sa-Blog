package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.Message;
import cn.icatw.blog.domain.dto.MessageBackDTO;
import cn.icatw.blog.domain.dto.MessageDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.MessageVO;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.ReviewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Message)表服务接口
 *
 * @author icatw
 * @since 2024-03-22 20:39:51
 */
public interface MessageService extends IService<Message> {
    /**
     * 后台留言列表DTO
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link MessageBackDTO}>
     */
    PageResult<MessageBackDTO> listMessageBackDTO(ConditionParams condition);

    /**
     * 更新留言审核状态
     *
     * @param reviewVO 审查vo
     */
    void updateMessagesReview(ReviewVO reviewVO);

    /**
     * 保存留言
     *
     * @param messageVO 消息vo
     */
    void saveMessage(MessageVO messageVO);

    /**
     * 留言列表
     *
     * @return {@link List}<{@link MessageDTO}>
     */
    List<MessageDTO> listMessages();
}

