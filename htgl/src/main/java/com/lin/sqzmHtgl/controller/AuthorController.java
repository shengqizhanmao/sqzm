package com.lin.sqzmHtgl.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lin.common.Result;
import com.lin.common.pojo.Author;
import com.lin.common.pojo.User;
import com.lin.common.service.AuthorService;
import com.lin.common.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/author")
public class AuthorController {

    @Resource
    private AuthorService authorService;
    @Resource
    private UserService userService;

    @RequiresPermissions("author:get")
    @GetMapping("/get/{status}/{size}/{page}")
    public Result get(@PathVariable("status") String status, @PathVariable("size") Long size, @PathVariable("page") Long page) {
        if (status.equals("")) {
            return Result.fail("参数不能为空");
        }
        return authorService.getAuthorVoList(status, size, page);
    }

    @RequiresPermissions("author:add")
    @PostMapping("/add/{username}/{status}")
    public Result add(@PathVariable("username") String username, @PathVariable("status") String status) {
        if (username.equals("")) {
            return Result.fail("参数不能为空");
        }
        if (status.equals("")) {
            return Result.fail("参数不能为空");
        }
        User userByUsername = userService.getUserByUsername(username);
        if (userByUsername == null) {
            return Result.fail("用户不存在");
        }
        return authorService.add(userByUsername.getId(), status);
    }

    @RequiresPermissions("author:update")
    @PutMapping("/update/{id}/{status}")
    public Result update(@PathVariable("id") String id, @PathVariable("status") String status) {
        if (status.equals("")) {
            return Result.fail("参数不能为空");
        }
        if (id.equals("")) {
            return Result.fail("参数不能为空");
        }
        LambdaUpdateWrapper<Author> authorLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        authorLambdaUpdateWrapper.eq(Author::getId, id).set(Author::getStatus, status);
        boolean b = authorService.update(authorLambdaUpdateWrapper);
        if (b) {
            return Result.succ("修改成功");
        }
        return Result.fail("修改失败");
    }

    @RequiresPermissions("author:delete")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        if (id.equals("")) {
            return Result.fail("参数不能为空");
        }
        boolean b = authorService.removeById(id);
        if (b) {
            return Result.succ("删除成功");
        }
        return Result.fail("删除失败");
    }
}
