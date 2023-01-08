package com.lin.sqzmHtgl.controller;

import com.alibaba.fastjson.JSON;
import com.lin.sqzmHtgl.common.Result;
import com.lin.sqzmHtgl.controller.param.LoginForm;
import com.lin.sqzmHtgl.pojo.SUser;
import com.lin.sqzmHtgl.pojo.User;
import com.lin.sqzmHtgl.pojo.Vo.SUserTokenVo;
import com.lin.sqzmHtgl.utils.JWTUtils;
import com.lin.sqzmHtgl.utils.Md5Utils;
import com.lin.sqzmHtgl.service.SUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


/**
 * @author lin
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    SUserService sUserService;
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @PostMapping()
    public Result login(@RequestBody LoginForm loginForm){
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        if(username.equals("")){
            return Result.fail(401,"登录失败: 用户名不能为空");
        }
        if(password.equals("")){
            return Result.fail(401,"登录失败: 密码不能为空");
        }
        //获取当前登录对象
        SUser sUser = null;
        sUser =sUserService.getSUserByUsername(username);
        //用户是否存在
        if(sUser==null){
            return Result.fail("登录失败,用户不存在") ;
        }
        //进行加密
        String salt = sUser.getSalt();
        String password2 = Md5Utils.md5Encryption(password,salt);
        //密码是否正确
        if(!password2.equals(sUser.getPassword())){
            return Result.fail("登录失败,密码错误") ;
        }
        if(sUser.getStatus().equals("-1")){
            return Result.fail(402,"帐号被禁用");
        }
        String jwt = JWTUtils.createToken(sUser.getUsername());
        SUserTokenVo sUserTokenVo=new SUserTokenVo();
        sUserTokenVo.setId(sUser.getsId());
        sUserTokenVo.setUserName(sUser.getUsername());
        sUserTokenVo.setNickName(sUser.getNickname());
        sUserTokenVo.setSex(sUser.getGender());
        sUserTokenVo.setEnableFlag(sUser.getStatus());
        redisTemplate.opsForValue().set("TOKEN_" + jwt, JSON.toJSONString(sUserTokenVo), 1, TimeUnit.DAYS);
        return Result.succ("登录成功",jwt);
    }
    @GetMapping("/logout")
    public Result Logout(@RequestHeader("Authorization") String token){
        try {
            redisTemplate.delete("TOKEN_" + token);
        } catch (Exception e) {
            log.error("redis删除token失败");
            return Result.fail("退出登录失败");
        }
        return Result.succ("退出成功");
    }
    @GetMapping("/LoginUrl")
    public Result LoginUrl(){
        return Result.fail(401,"未登录,请跳转到登录页");
    }
    @GetMapping("/Unauthorized")
    public Result Unauthorized(){
        return Result.fail(403,"权限不够,请联系管理员");
    }

    @PostMapping("/register")
    public Result register(@RequestBody SUser sUser){
        Result result = sUserService.addSUser(sUser);
        int code = result.getCode();
        if (code==200){
            return Result.succ("注册成功");
        }else{
            return result;
        }
    }
}
