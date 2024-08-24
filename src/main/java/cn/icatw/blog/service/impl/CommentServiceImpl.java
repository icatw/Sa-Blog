package cn.icatw.blog.service.impl;

import cn.icatw.blog.domain.dto.*;
import cn.icatw.blog.domain.entity.Comment;
import cn.icatw.blog.mapper.*;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.BlogInfoService;
import cn.icatw.blog.service.CommentService;
import cn.icatw.blog.service.RedisService;
import cn.icatw.blog.utils.HtmlUtil;
import cn.icatw.blog.utils.PageUtil;
import cn.icatw.blog.utils.UserUtil;
import cn.icatw.blog.domain.vo.CommentVO;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.ReviewVO;
import cn.icatw.blog.domain.vo.WebsiteConfigVO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static cn.icatw.blog.constant.CommonConst.*;
import static cn.icatw.blog.constant.MQPrefixConst.EMAIL_EXCHANGE;
import static cn.icatw.blog.constant.RedisPrefixConst.COMMENT_LIKE_COUNT;
import static cn.icatw.blog.constant.RedisPrefixConst.COMMENT_USER_LIKE;
import static cn.icatw.blog.enums.CommentTypeEnum.*;

/**
 * (Comment)表服务实现类
 *
 * @author icatw
 * @since 2024-03-22 20:39:50
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private static final String TABLE_NAME = "tb_comment";
    @Resource
    private ReviewMapper reviewMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private BlogInfoService blogInfoService;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TalkMapper talkMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * 网站网址
     */
    @Value("${website.url}")
    private String websiteUrl;

    /**
     * 后台评论列表
     *
     * @param condition 条件
     * @return {@link PageResult}<{@link CommentBackDTO}>
     */
    @Override
    public PageResult<CommentBackDTO> listCommentBackDTO(ConditionParams condition) {
        // 统计后台评论量
        Integer count = baseMapper.countCommentDTO(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台评论集合
        List<CommentBackDTO> commentBackDTOList = baseMapper.listCommentBackDTO(PageUtil.getLimitCurrent(), PageUtil.getSize(), condition);
        return new PageResult<>(commentBackDTOList, count);
    }

    /**
     * 更新评论审核状态
     *
     * @param reviewVO 审查vo
     */
    @Override
    public void updateCommentsReview(ReviewVO reviewVO) {
        // 修改评论审核状态
        reviewMapper.updateReview(reviewVO, TABLE_NAME);
    }

    @Override
    public PageResult<CommentDTO> listComments(CommentVO commentVO) {
        // 查询评论量
        long commentCount = baseMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(ARTICLE.getType().equals(commentVO.getType()), Comment::getArticleId, commentVO.getArticleId())
                .eq(TALK.getType().equals(commentVO.getType()), Comment::getTalkId, commentVO.getTalkId())
                .eq(LINK.getType().equals(commentVO.getType()), Comment::getType, commentVO.getType())
                .isNull(Comment::getParentId)
                .eq(Comment::getIsReview, TRUE));
        if (commentCount == 0) {
            return new PageResult<>();
        }
        // 分页查询评论集合
        List<CommentDTO> commentDTOList = baseMapper.listComments(PageUtil.getLimitCurrent(), PageUtil.getSize(), commentVO);
        if (CollectionUtils.isEmpty(commentDTOList)) {
            return new PageResult<>();
        }
        // 查询redis的评论点赞数据
        Map<String, Object> likeCountMap = redisService.hGetAll(COMMENT_LIKE_COUNT);
        // 提取评论id集合
        List<Integer> commentIdList = commentDTOList.stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
        // 根据评论id集合查询回复数据
        List<ReplyDTO> replyDTOList = baseMapper.listReplies(commentIdList);
        // 封装回复点赞量
        replyDTOList.forEach(item -> item.setLikeCount((Integer) likeCountMap.get(item.getId().toString())));
        // 根据评论id分组回复数据
        Map<Integer, List<ReplyDTO>> replyMap = replyDTOList.stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));
        // 根据评论id查询回复量
        Map<Integer, Integer> replyCountMap = baseMapper.listReplyCountByCommentId(commentIdList)
                .stream().collect(Collectors.toMap(ReplyCountDTO::getCommentId, ReplyCountDTO::getReplyCount));
        // 封装评论数据
        commentDTOList.forEach(item -> {
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
            item.setReplyDTOList(replyMap.get(item.getId()));
            item.setReplyCount(replyCountMap.get(item.getId()));
        });
        return new PageResult<>(commentDTOList, (int) commentCount);
    }

    @Override
    public void saveComment(CommentVO commentVO) {
        // 判断是否需要审核
        WebsiteConfigVO websiteConfig = blogInfoService.getWebsiteConfig();
        Integer isReview = websiteConfig.getIsCommentReview();
        // 过滤标签
        commentVO.setCommentContent(HtmlUtil.filter(commentVO.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(UserUtil.getCurrentUser().getUserId())
                .replyUserId(commentVO.getReplyUserId())
                .articleId(commentVO.getArticleId())
                .talkId(commentVO.getTalkId())
                .commentContent(commentVO.getCommentContent())
                .parentId(commentVO.getParentId())
                .type(commentVO.getType())
                .isReview(isReview == TRUE ? FALSE : TRUE)
                .build();
        baseMapper.insert(comment);
        // 判断是否开启邮箱通知,通知用户
        CompletableFuture.runAsync(() -> {
            if (websiteConfig.getIsEmailNotice().equals(TRUE)) {
                notice(comment);
            }
        });
    }

    /**
     * 邮箱通知评论用户
     *
     * @param comment 评论信息
     */
    public void notice(Comment comment) {
        // 查询回复用户邮箱号
        Integer userId = BLOGGER_ID;
        String id = "";
        switch (Objects.requireNonNull(getCommentEnum(comment.getType()))) {
            case ARTICLE:
                userId = articleMapper.selectById(comment.getArticleId()).getUserId();
                id = comment.getArticleId().toString();
                break;
            case TALK:
                userId = talkMapper.selectById(comment.getTalkId()).getUserId();
                id = comment.getTalkId().toString();
                break;
            default:
                break;
        }
        if (Objects.nonNull(comment.getReplyUserId())) {
            userId = comment.getReplyUserId();
        }
        String email = userMapper.selectById(userId).getEmail();
        if (StringUtils.isNotBlank(email)) {
            // 发送消息
            EmailDTO emailDTO = new EmailDTO();
            if (comment.getIsReview().equals(TRUE)) {
                // 评论提醒
                emailDTO.setEmail(email);
                emailDTO.setSubject("评论提醒");
                // 获取评论路径
                String url = websiteUrl + getCommentPath(comment.getType()) + id;
                emailDTO.setContent("您收到了一条新的回复，请前往" + url + "\n页面查看");
            } else {
                // 管理员审核提醒
                String adminEmail = userMapper.selectById(BLOGGER_ID).getEmail();
                emailDTO.setEmail(adminEmail);
                emailDTO.setSubject("审核提醒");
                emailDTO.setContent("您收到了一条新的回复，请前往后台管理页面审核");
            }
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        }
    }

    @Override
    public List<ReplyDTO> listRepliesByCommentId(Integer commentId) {
        // 转换页码查询评论下的回复
        List<ReplyDTO> replyDTOList = baseMapper.listRepliesByCommentId(PageUtil.getLimitCurrent(), PageUtil.getSize(), commentId);
        // 查询redis的评论点赞数据
        Map<String, Object> likeCountMap = redisService.hGetAll(COMMENT_LIKE_COUNT);
        // 封装点赞数据
        replyDTOList.forEach(item -> item.setLikeCount((Integer) likeCountMap.get(item.getId().toString())));
        return replyDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveCommentLike(Integer commentId) {
        redisService.likeOpt(commentId, COMMENT_USER_LIKE, COMMENT_LIKE_COUNT);
    }

}

