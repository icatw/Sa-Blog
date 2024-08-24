package cn.icatw.blog.service;

import cn.icatw.blog.domain.entity.Page;
import cn.icatw.blog.domain.vo.PageVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 页面服务
 *
 * @author 王顺
 * @date 2024/04/07
 */
public interface PageService extends IService<Page> {

    /**
     * 保存或更新页面
     *
     * @param pageVO 页面信息
     */
    void saveOrUpdatePage(PageVO pageVO);

    /**
     * 删除页面
     *
     * @param pageId 页面id
     */
    void deletePage(Integer pageId);

    /**
     * 获取页面列表
     *
     * @return {@link List <PageVO>} 页面列表
     */
    List<PageVO> listPages();

}
