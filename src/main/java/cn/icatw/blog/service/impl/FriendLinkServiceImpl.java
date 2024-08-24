package cn.icatw.blog.service.impl;

import cn.icatw.blog.domain.entity.FriendLink;
import cn.icatw.blog.domain.dto.FriendLinkBackDTO;
import cn.icatw.blog.domain.dto.FriendLinkDTO;
import cn.icatw.blog.mapper.FriendLinkMapper;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.service.FriendLinkService;
import cn.icatw.blog.utils.BeanCopyUtil;
import cn.icatw.blog.utils.PageUtil;
import cn.icatw.blog.domain.vo.FriendLinkVO;
import cn.icatw.blog.domain.vo.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 好友链接服务impl
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    @Override
    public List<FriendLinkDTO> listFriendLinks() {
        // 查询友链列表
        List<FriendLink> friendLinkList = baseMapper.selectList(null);
        return BeanCopyUtil.copyToList(friendLinkList, FriendLinkDTO.class);
    }

    @Override
    public PageResult<FriendLinkBackDTO> listFriendLinkDTO(ConditionParams condition) {
        // 分页查询友链列表
        Page<FriendLink> page = new Page<>(PageUtil.getCurrent(), PageUtil.getSize());
        Page<FriendLink> friendLinkPage = baseMapper.selectPage(page, new LambdaQueryWrapper<FriendLink>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), FriendLink::getLinkName, condition.getKeywords()));
        // 转换DTO
        List<FriendLinkBackDTO> friendLinkBackDTOList = BeanCopyUtil.copyToList(friendLinkPage.getRecords(), FriendLinkBackDTO.class);
        return new PageResult<>(friendLinkBackDTOList, (int) friendLinkPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO) {
        FriendLink friendLink = BeanCopyUtil.copyProperties(friendLinkVO, FriendLink.class);
        this.saveOrUpdate(friendLink);
    }

}
