package cn.icatw.blog.service.impl;

import cn.icatw.blog.utils.HtmlUtil;
import cn.icatw.blog.domain.entity.Talk;
import cn.icatw.blog.domain.dto.CommentCountDTO;
import cn.icatw.blog.domain.dto.TalkBackDTO;
import cn.icatw.blog.domain.dto.TalkDTO;
import cn.icatw.blog.mapper.CommentMapper;
import cn.icatw.blog.mapper.TalkMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.RedisService;
import cn.icatw.blog.service.TalkService;
import cn.icatw.blog.utils.AssertUtil;
import cn.icatw.blog.utils.BeanCopyUtil;
import cn.icatw.blog.utils.PageUtil;
import cn.icatw.blog.utils.UserUtil;
import cn.icatw.blog.domain.vo.PageResult;
import cn.icatw.blog.domain.vo.TalkVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.icatw.blog.constant.RedisPrefixConst.TALK_LIKE_COUNT;
import static cn.icatw.blog.constant.RedisPrefixConst.TALK_USER_LIKE;
import static cn.icatw.blog.enums.ArticleStatusEnum.PUBLIC;

/**
 * (Talk)表服务实现类
 *
 * @author icatw
 * @since 2024-03-22 21:02:31
 */
@Service("talkService")
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements TalkService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private RedisService redisService;

    @Override
    public List<String> listHomeTalks() {

        // 查询最新10条说说
        return baseMapper.selectList(new LambdaQueryWrapper<Talk>()
                        .eq(Talk::getStatus, PUBLIC.getStatus())
                        .orderByDesc(Talk::getIsTop)
                        .orderByDesc(Talk::getId)
                        .last("limit 10"))
                .stream()
                .map(item -> item.getContent().length() > 200 ? HtmlUtil.filter(item.getContent().substring(0, 200)) : HtmlUtil.filter(item.getContent()))
                .collect(Collectors.toList());
    }

    public PageResult<TalkDTO> listTalks() {
        // 查询说说总量
        long count = baseMapper.selectCount((new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, PUBLIC.getStatus())));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkDTO> talkDTOList = baseMapper.listTalks(PageUtil.getLimitCurrent(), PageUtil.getSize());
        // 查询说说评论量
        List<Integer> talkIdList = talkDTOList.stream()
                .map(TalkDTO::getId)
                .collect(Collectors.toList());
        Map<Integer, Integer> commentCountMap = commentMapper.listCommentCountByTalkIds(talkIdList)
                .stream()
                .collect(Collectors.toMap(CommentCountDTO::getId, CommentCountDTO::getCommentCount));
        // 查询说说点赞量
        Map<String, Object> likeCountMap = redisService.hGetAll(TALK_LIKE_COUNT);
        for (TalkDTO item : talkDTOList) {
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
            item.setCommentCount(commentCountMap.get(item.getId()));
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(JSON.parseObject(item.getImages(), new TypeReference<>() {
                }));
            }
        }
        return new PageResult<>(talkDTOList, (int) count);
    }

    @Override
    public TalkDTO getTalkById(Integer talkId) {
        // 查询说说信息
        TalkDTO talkDTO = baseMapper.getTalkById(talkId);
        AssertUtil.notNull(talkDTO, "说说不存在");
        // 查询说说点赞量
        talkDTO.setLikeCount((Integer) redisService.hGet(TALK_LIKE_COUNT, talkId.toString()));
        // 转换图片格式
        if (Objects.nonNull(talkDTO.getImages())) {
            talkDTO.setImgList(JSON.parseObject(talkDTO.getImages(), new TypeReference<>() {
            }));
        }
        return talkDTO;
    }

    @Override
    public void saveTalkLike(Integer talkId) {
        // 判断是否点赞
        redisService.likeOpt(talkId, TALK_USER_LIKE, TALK_LIKE_COUNT);
    }

    @Override
    public void saveOrUpdateTalk(TalkVO talkVO) {
        Talk talk = BeanCopyUtil.copyProperties(talkVO, Talk.class);
        talk.setUserId(UserUtil.getCurrentUser().getUserId());
        this.saveOrUpdate(talk);
    }

    @Override
    public void deleteTalks(List<Integer> talkIdList) {
        baseMapper.deleteBatchIds(talkIdList);
    }

    @Override
    public PageResult<TalkBackDTO> listBackTalks(ConditionParams conditionVO) {
        // 查询说说总量
        long count = baseMapper.selectCount(new LambdaQueryWrapper<Talk>()
                .eq(Objects.nonNull(conditionVO.getStatus()), Talk::getStatus, conditionVO.getStatus()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkBackDTO> talkDTOList = baseMapper.listBackTalks(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionVO);
        // 转换图片格式
        for (TalkBackDTO item : talkDTOList) {
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(JSON.parseObject(item.getImages(), new TypeReference<>() {
                }));
            }
        }
        return new PageResult<>(talkDTOList, (int) count);
    }

    @Override
    public TalkBackDTO getBackTalkById(Integer talkId) {
        TalkBackDTO talkBackDTO = baseMapper.getBackTalkById(talkId);
        // 转换图片格式
        if (Objects.nonNull(talkBackDTO.getImages())) {
            talkBackDTO.setImgList(JSON.parseObject(talkBackDTO.getImages(), new TypeReference<>() {
            }));
        }
        return talkBackDTO;
    }
}

