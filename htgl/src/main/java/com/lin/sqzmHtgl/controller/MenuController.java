package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.sqzmHtgl.pojo.Menu;
import com.lin.sqzmHtgl.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    MenuService menuService;
    //获取menu
    @RequiresAuthentication
    @GetMapping("/get")
    public Result get(){
        List<Menu> list = menuService.list();
        return Result.succ("获取用户菜单",list);
    }
    //添加menu
    @RequiresPermissions("menu:add")
    @PostMapping("/add")
    public Result add(@RequestBody Menu menu){
        boolean b = menuService.save(menu);
        if (b){
            return Result.succ("添加用户菜单成功");
        }
        return Result.fail("添加用户菜单失败");
    }
    //修改menu
    @RequiresPermissions("menu:update")
    @PostMapping("/update")
    public Result update(@RequestBody Menu menu){
        boolean b = menuService.updateById(menu);
        if (b){
            return Result.succ("修改用户菜单成功");
        }
        return Result.fail("修改用户菜单失败");
    }
    //删除menu
    @RequiresPermissions("menu:delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody Menu menu){
        boolean b = menuService.removeById(menu.getId());
        if (b){
            return Result.succ("删除用户菜单成功");
        }
        return Result.fail("删除用户菜单失败");
    }
}
