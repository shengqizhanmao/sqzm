package com.lin.sqzmHtgl.controller;

import com.lin.sqzmHtgl.common.Result;
import com.lin.sqzmHtgl.service.SMenuService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/sMenu")
public class SMenuController {
    @Autowired
    SMenuService sMenuService;
    //获取menu
    @RequiresAuthentication
    @GetMapping("/get")
    public Result get(@RequestHeader("Authorization") String token){
        return sMenuService.getSMenuBySUserId(token);
    }
}
