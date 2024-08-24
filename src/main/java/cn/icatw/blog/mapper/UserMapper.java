package cn.icatw.blog.mapper;

import cn.icatw.blog.domain.dto.UserBackDTO;
import cn.icatw.blog.domain.params.UserConditionParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.icatw.blog.domain.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author icatw
 * @since 2024-04-12 10:12:54
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过社交获取用户
     *
     * @param source 来源
     * @param uuid   uuid
     * @return {@link User}
     */
    User getUserBySocial(@Param("source") String source, @Param("uuid") String uuid);

    /**
     * 用户数量
     *
     * @param condition 条件
     * @return {@link Integer}
     */
    Integer countUser(@Param("condition") UserConditionParams condition);

    /**
     * 用户列表
     *
     * @param current   current
     * @param size      size
     * @param condition 条件
     * @return {@link List}<{@link UserBackDTO}>
     */
    List<UserBackDTO> listUsers(@Param("current") Long current, @Param("size") Long size, @Param("condition") UserConditionParams condition);
}

