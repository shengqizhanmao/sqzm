package com.lin.sqzmHtgl.controller;

import com.lin.sqzmHtgl.common.Result;
import com.lin.sqzmHtgl.pojo.SUser;
import com.lin.sqzmHtgl.pojo.Vo.SUserTokenVo;
import com.lin.sqzmHtgl.service.SUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
@RequestMapping("/sUser")
public class SUserController {

    @Resource
    SUserService sUserService;
    @RequiresAuthentication
    @GetMapping("/getUserByToken")
    public Result getUserByToken(
            @RequestHeader("Authorization") String token
    ) {
        SUserTokenVo sUserByToken = sUserService.findUserByToken(token);
        return Result.succ("获取user信息成功",sUserByToken);
    }
    @RequiresPermissions("sUser:get")
    @GetMapping("/get")
    public Result get(){
        return Result.succ("成功");
    }
}
