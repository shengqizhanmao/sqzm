package com.lin.sqzmHtgl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.common.Result;
import com.lin.common.pojo.Menu;
import com.lin.common.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
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
    @NotNull
    @RequiresAuthentication
    @GetMapping("/get")
    public Result get() {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.orderByAsc(Menu::getSort);
        List<Menu> list = menuService.list(menuLambdaQueryWrapper);
        return Result.succ("获取用户菜单", list);
    }

    @RequiresPermissions("menu:get")
    @GetMapping("/get/{page}/{size}")
    public Result getMenu(@PathVariable("page") Long page,@PathVariable("size") Long size) {
        return menuService.getMenuPage(page,size);
    }

    //添加menu
    @NotNull
    @RequiresPermissions("menu:add")
    @PostMapping("/add")
    public Result add(@RequestBody Menu menu) {
        boolean b = menuService.save(menu);
        if (b) {
            return Result.succ("添加用户菜单成功");
        }
        return Result.fail("添加用户菜单失败");
    }

    //修改menu
    @RequiresPermissions("menu:update")
    @PutMapping("/update")
    public Result update(@RequestBody Menu menu) {
        boolean b = menuService.updateById(menu);
        if (b) {
            return Result.succ("修改用户菜单成功");
        }
        return Result.fail("修改用户菜单失败");
    }

    //删除menu
    @NotNull
    @RequiresPermissions("menu:delete")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return Result.fail("参数不能为空");
        }
        boolean b = menuService.removeById(id);
        if (b) {
            return Result.succ("删除用户菜单成功");
        }
        return Result.fail("删除用户菜单失败");
    }
}
