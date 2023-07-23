package com.lin.sqzmYxlt.controller;

import com.lin.common.RedisStatus;
import com.lin.common.Result;
import com.lin.common.pojo.User;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.pojo.Vo.UserVo;
import com.lin.common.service.UserService;
import com.lin.common.utils.CodeUtils;
import com.lin.sqzmYxlt.controller.param.EmailCode;
import com.lin.sqzmYxlt.controller.param.UpdatePassword;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @apiNote 用户
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @RequiresAuthentication
    @GetMapping("/getUserByToken")
    public Result getUserByToken(
            @RequestHeader("Authorization") String token
    ) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        if (userByToken == null) {
            return Result.fail("获取user信息失败");
        }
        return Result.succ("获取user信息成功", userByToken);
    }


    //修改用户
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/getUpdateCodeByEmail")
    public Result getUpdateCodeByEmail(@RequestParam("email") String email) {
        return userService.getUpdateEmailCode(email);
    }

    @PostMapping("/updateEmail")
    public Result updateEmail(@RequestBody EmailCode emailCode) {
        String code = emailCode.getCode();
        String email = emailCode.getEmail();
        String id = emailCode.getId();
        return userService.updateEmail(id, email, code);
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePassword updatePassword) {
        String newPassword = updatePassword.getNewPassword();
        String password = updatePassword.getPassword();
        String id = updatePassword.getId();
        return userService.updatePassword(id, password, newPassword);
    }

    @GetMapping("/getNewCookie")
    public Result getNewCookie(@RequestHeader("Authorization") String token) {
        return userService.getNewCookie(token);
    }

    @PostMapping("/updateUserAvatar")
    public Result updateUserAvatar(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
        return userService.upAvatar(file, id);
    }

    @GetMapping("/getUserById")
    public Result getUserById(@RequestParam("userId") String userId) {
        User byId = userService.getById(userId);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(byId, userVo);
        return Result.succ("查询成功", userVo);
    }

}
