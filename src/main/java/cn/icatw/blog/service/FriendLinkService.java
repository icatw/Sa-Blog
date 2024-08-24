package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.FriendLink;
import cn.icatw.blog.domain.dto.FriendLinkBackDTO;
import cn.icatw.blog.domain.dto.FriendLinkDTO;
import cn.icatw.blog.domain.params.ConditionParams;
import cn.icatw.blog.domain.vo.FriendLinkVO;
import cn.icatw.blog.domain.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 好友链接服务
 *
 * @author 王顺
 * @date 2024/04/07
 */
public interface FriendLinkService extends IService<FriendLink> {

    /**
     * 查看友链列表
     *
     * @return 友链列表
     */
    List<FriendLinkDTO> listFriendLinks();

    /**
     * 查看后台友链列表
     *
     * @param condition 条件
     * @return 友链列表
     */
    PageResult<FriendLinkBackDTO> listFriendLinkDTO(ConditionParams condition);

    /**
     * 保存或更新友链
     *
     * @param friendLinkVO 友链
     */
    void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO);

}
