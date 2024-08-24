package cn.icatw.blog.handler;

import cn.hutool.core.util.StrUtil;
import cn.icatw.blog.utils.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

import static cn.icatw.blog.constant.CommonConst.*;


/**
 * 分页拦截器
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Slf4j
public class PageableHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        String currentPage = request.getParameter(CURRENT);
        String pageSize = Optional.ofNullable(request.getParameter(SIZE)).orElse(DEFAULT_SIZE);
        if (StrUtil.isNotEmpty(currentPage)) {
            log.info("分页参数：当前页码{},每页条数{}", currentPage, pageSize);
            PageUtil.setCurrentPage(new Page<>( Long.parseLong(currentPage), Long.parseLong(pageSize)));
        }
        return true;
    }


    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request,@Nonnull HttpServletResponse response,@Nonnull Object handler, Exception ex) {
        PageUtil.remove();
    }

}
