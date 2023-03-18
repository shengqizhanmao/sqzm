package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.SMenu;
import com.lin.common.service.SMenuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/sMenu")
public class SMenuController {
    @Autowired
    private SMenuService sMenuService;

    //系统用户获取sMenu列表
    @RequiresAuthentication
    @GetMapping("/get")
    public Result get(@RequestHeader("Authorization") String token) {
        return sMenuService.getSMenuBySUserId(token);
    }

    //分页获取sMenu列表
    @RequiresPermissions("sMenu:get")
    @GetMapping("/getPage/{size}/{page}")
    public Result getPage(@PathVariable("size") Long size, @PathVariable("page") Long page) {
        if (size <= 0L || page <= 0L) {
            return Result.fail("参数错误,参数不能小于0");
        }
        return sMenuService.getSMenu(size, page);
    }

    //添加系统目录
    @RequiresPermissions("sMenu:add")
    @PostMapping("/add")
    public Result add(@RequestBody SMenu sMenu) {
        if (sMenu == null) {
            return Result.fail("参数不能为空");
        }
        boolean b = sMenuService.save(sMenu);
        if (b) {
            return Result.succ("添加系统菜单成功");
        }
        return Result.fail("添加系统菜单失败");
    }

    //修改系统目录
    @RequiresPermissions("sMenu:update")
    @PutMapping("/update")
    public Result update(@RequestBody SMenu sMenu) {
        if (sMenu == null) {
            return Result.fail("参数不能为空");
        }
        boolean b = sMenuService.updateById(sMenu);
        if (b) {
            return Result.succ("修改系统菜单成功");
        }
        return Result.fail("修改系统菜单失败");
    }

    //删除系统目录
    @RequiresPermissions("sMenu:delete")
    @DeleteMapping("/delete/{sId}")
    public Result delete(@PathVariable("sId") String sId) {
        if (StringUtils.isEmpty(sId)) {
            return Result.fail("参数不能为空");
        }
        boolean b = sMenuService.removeById(sId);
        sMenuService.remove(sId);
        if (b) {
            return Result.succ("删除系统菜单成功");
        }
        return Result.fail("删除系统菜单失败");
    }

}
