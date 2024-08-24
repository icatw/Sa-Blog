package cn.icatw.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.icatw.blog.domain.entity.Message;
import cn.icatw.blog.domain.dto.MessageBackDTO;
import cn.icatw.blog.domain.dto.MessageDTO;
import cn.icatw.blog.mapper.MessageMapper;
import cn.icatw.blog.mapper.ReviewMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.BlogInfoService;
import cn.icatw.blog.service.MessageService;
import cn.icatw.blog.utils.BeanCopyUtil;
import cn.icatw.blog.utils.HtmlUtil;
import cn.icatw.blog.utils.IpUtil;
import cn.icatw.blog.utils.PageUtil;
import cn.icatw.blog.domain.vo.MessageVO;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.ReviewVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static cn.icatw.blog.constant.CommonConst.FALSE;
import static cn.icatw.blog.constant.CommonConst.TRUE;

/**
 * (Message)表服务实现类
 *
 * @author icatw
 * @since 2024-03-22 20:39:51
 */
@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    private static final String TABLE_NAME = "tb_message";
    @Resource
    ReviewMapper reviewMapper;
    @Resource
    BlogInfoService blogInfoService;
    @Resource
    private HttpServletRequest request;

    @Override
    public PageResult<MessageBackDTO> listMessageBackDTO(ConditionParams condition) {
        // 分页查询留言列表
        Page<Message> page = new Page<>(PageUtil.getCurrent(), PageUtil.getSize());
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<Message>()
                .nested(StringUtils.isNotBlank(condition.getKeywords()),
                        // 使用嵌套的 LambdaQueryWrapper 实现 "keyword 匹配留言内容"
                        nested -> nested.like(Message::getNickname, condition.getKeywords())
                                .or()
                                .like(Message::getMessageContent, condition.getKeywords())
                )
                .eq(Objects.nonNull(condition.getIsReview()), Message::getIsReview, condition.getIsReview())
                .orderByDesc(Message::getId);
        Page<Message> messagePage = baseMapper.selectPage(page, messageLambdaQueryWrapper);
        // 转换DTO
        List<MessageBackDTO> messageBackDTOList = BeanUtil.copyToList(messagePage.getRecords(), MessageBackDTO.class);
        return new PageResult<>(messageBackDTOList, (int) messagePage.getTotal());
    }

    @Override
    public void updateMessagesReview(ReviewVO reviewVO) {
        reviewMapper.updateReview(reviewVO, TABLE_NAME);
    }

    @Override
    public void saveMessage(MessageVO messageVO) {
        // 判断是否需要审核
        Integer isReview = blogInfoService.getWebsiteConfig().getIsMessageReview();
        // 获取用户ip
        String ipAddress = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        Message message = BeanCopyUtil.copyProperties(messageVO, Message.class);
        message.setMessageContent(HtmlUtil.filter(message.getMessageContent()));
        message.setIpAddress(ipAddress);
        message.setIsReview(isReview == TRUE ? FALSE : TRUE);
        message.setIpSource(ipSource);
        baseMapper.insert(message);
    }

    @Override
    public List<MessageDTO> listMessages() {
        // 查询留言列表
        List<Message> messageList = baseMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar, Message::getMessageContent, Message::getTime)
                .eq(Message::getIsReview, TRUE));
        return BeanCopyUtil.copyListToList(messageList, MessageDTO.class);
    }
}

