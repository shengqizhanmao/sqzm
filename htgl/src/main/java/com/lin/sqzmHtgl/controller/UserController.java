package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.SUser;
import com.lin.common.pojo.User;
import com.lin.common.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    //分页查询用户
    @RequiresPermissions("user:get")
    @GetMapping("/get/{size}/{page}")
    public Result get(@PathVariable("size") Long size, @PathVariable("page") Long page) {
        return userService.listPage(size, page);
    }
    /**
     获取系统用户的列表,分页
     @Param Long size,分页的大小
     @Param Long page,第N页
     @return Result,自定义返回数据
     **/


    //添加用户
    @RequiresPermissions("user:add")
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        return userService.addUser(user);
    }

    //修改用户
    @RequiresPermissions("user:update")
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    //修改用户状态,1
    @RequiresPermissions("sUser:update")
    @PutMapping("/updateEnableFlag/{id}/{enableFlag}")
    public Result UpdateEnableFlag(@PathVariable("id") String id, @PathVariable("enableFlag") String enableFlag) {
        if (StringUtils.isEmpty(id)) {
            return Result.fail(403, "id参数不能为空");
        }
        if (StringUtils.isEmpty(enableFlag)) {
            return Result.fail(403, "enableFlag参数不能为空");
        }
        return userService.updateEnableFlag(id, enableFlag);
    }

    //修改头像
    @RequiresPermissions("user:update")
    @PutMapping("/updateUserAvatar")
    public Result updateUserAvatar(MultipartFile file, String id) {
        return userService.upAvatar(file, id);
    }

    //删除用户
    @RequiresPermissions("user:delete")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        return userService.deleteUser(id);
    }

}
