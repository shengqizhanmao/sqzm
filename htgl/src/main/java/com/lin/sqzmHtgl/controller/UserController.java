package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.User;
import com.lin.common.pojo.Vo.UserVo;
import com.lin.common.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
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
    @NotNull
    @RequiresPermissions("user:get")
    @GetMapping("/get")
    public Result get(){
        List<User> list = userService.list();
        List<UserVo> list1=new ArrayList<>();
        for (User user:list) {
            UserVo userVo= new UserVo();
            BeanUtils.copyProperties(user, userVo);
            list1.add(userVo);
        }
        return Result.succ("查询普通用户成功",list1);
    }
    @RequiresPermissions("user:add")
    @PostMapping("/add")
    public Result add(@RequestBody User user){
        return userService.addUser(user);
    }
    @RequiresPermissions("user:update")
    @PostMapping("/update")
    public Result update(@RequestBody User user){
        return userService.updateUser(user);
    }
    @RequiresPermissions("user:update")
    @PostMapping("/updateUserAvatar")
    public Result updateUserAvatar(MultipartFile file, String id) {
        return userService.upAvatar(file,id);
    }
    @RequiresPermissions("user:delete")
    @PostMapping("/delete")
    public Result delete(@NotNull @RequestBody User user){
        return userService.deleteUser(user.getId());
    }

}
