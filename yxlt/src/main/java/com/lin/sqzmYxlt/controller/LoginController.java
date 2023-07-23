package com.lin.sqzmYxlt.controller;

import com.lin.common.RedisStatus;
import com.lin.common.Result;
import com.lin.common.pojo.User;
import com.lin.common.service.UserService;
import com.lin.common.utils.CodeUtils;
import com.lin.sqzmYxlt.controller.param.EmailCode;
import com.lin.sqzmYxlt.controller.param.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * @author lin
 * @apiNote 登录
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping()
    public Result login(@NotNull @RequestBody LoginForm loginForm, @RequestParam("time") String codeDate) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        String code = loginForm.getCode();
        return userService.Login(username, password, code, codeDate);
    }
    //生成code图片
    @GetMapping("/code")
    public void verifyCode(@NotNull HttpServletResponse res, @RequestParam("time") String date) {
        CodeUtils code = new CodeUtils();
        BufferedImage image = code.getImage();
        String codeText = code.getText();
        System.out.println(codeText);
        redisTemplate.opsForValue().set(RedisStatus.USER_CODE + date, codeText, 1, TimeUnit.MINUTES);
        try {
            code.output(image, res.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/getEmailCode")
    public Result loginEmailCode(@RequestParam("email") String email) {
        return userService.getEmailCode(email);
    }

    @PostMapping("/loginEmail")
    public Result loginEmail(@NotNull @RequestBody EmailCode emailCode) {
        String code = emailCode.getCode();
        String email = emailCode.getEmail();
        return userService.LoginEmail(email, code);
    }

    @GetMapping("/logout")
    public Result Logout(@RequestHeader("Authorization") String token) {
        try {
            redisTemplate.delete(RedisStatus.TOKEN_USER + token);
        } catch (Exception e) {
            log.error("redis删除token失败");
            return Result.fail("退出登录失败");
        }
        return Result.succ("退出成功");
    }

    @NotNull
    @GetMapping("/LoginUrl")
    public Result LoginUrl() {
        return Result.fail(401, "未登录,请跳转到登录页");
    }

    @NotNull
    @GetMapping("/Unauthorized")
    public Result Unauthorized() {
        return Result.fail(403, "权限不够,请联系管理员");
    }

    @NotNull
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        Result result = userService.addUser(user);
        int code = result.getCode();
        if (code == 200) {
            return Result.succ("注册成功");
        }
        return result;
    }
}
