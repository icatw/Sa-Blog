package cn.icatw.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.icatw.blog.domain.entity.User;
import cn.icatw.blog.service.UserService;
import cn.icatw.blog.common.Result;

import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (User)表控制层
 *
 * @author icatw
 * @since 2024-04-12 10:12:54
 */
@RestController
@RequestMapping("user")
public class UserController {

    /**
     * 构造方法依赖注入
     */
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询所有数据
     */
    @GetMapping
    public Result<Page<User>> page(@RequestParam int current, @RequestParam int size) {
        Page<User> page = new Page<>(current, size);
        return Result.ok(this.userService.page(page));
    }


    /**
     * 通过主键查询单条数据
     */
    @GetMapping("{id}")
    public Result<User> selectOne(@PathVariable Serializable id) {
        return Result.ok(this.userService.getById(id));
    }

    /**
     * 新增数据
     */
    @PostMapping
    public Result<Boolean> save(@RequestBody User user) {
        return Result.ok(this.userService.save(user));
    }

    /**
     * 修改数据
     */
    @PutMapping
    public Result<Boolean> updateById(@RequestBody User user) {
        return Result.ok(this.userService.updateById(user));
    }

    /**
     * 单条/批量删除数据
     */
    @DeleteMapping
    public Result<Boolean> delete(@RequestParam List<Long> id) {
        return Result.ok(this.userService.removeByIds(id));
    }
}

