package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.SMenu;
import com.lin.common.service.SMenuService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/sMenu")
public class SMenuController {
    @Autowired
    SMenuService sMenuService;
    //获取sMenu
    @RequiresAuthentication
    @GetMapping("/get")
    public Result get(@RequestHeader("Authorization") String token){
        return sMenuService.getSMenuBySUserId(token);
    }

    //获取sMenu
    @NotNull
    @RequiresPermissions("sMenu:get")
    @GetMapping("/get2")
    public Result get2(){
        List<SMenu> list = sMenuService.list();
        return Result.succ("获取系统菜单",list);
    }
    //添加sMenu
    @NotNull
    @RequiresPermissions("sMenu:add")
    @PostMapping("/add")
    public Result add(@RequestBody SMenu sMenu){
        boolean b = sMenuService.save(sMenu);
        if (b){
            return Result.succ("添加系统菜单成功");
        }
        return Result.fail("添加系统菜单失败");
    }
    //修改sMenu
    @NotNull
    @RequiresPermissions("sMenu:update")
    @PostMapping("/update")
    public Result update(@RequestBody SMenu sMenu){
        boolean b = sMenuService.updateById(sMenu);
        if (b){
            return Result.succ("修改系统菜单成功");
        }
        return Result.fail("修改系统菜单失败");
    }
    //删除sMenu
    @NotNull
    @RequiresPermissions("sMenu:delete")
    @PostMapping("/delete")
    public Result delete(@NotNull @RequestBody SMenu sMenu){
        boolean b = sMenuService.removeById(sMenu.getsId());
        if (b){
            return Result.succ("删除系统菜单成功");
        }
        return Result.fail("删除系统菜单失败");
    }
    
    
}
