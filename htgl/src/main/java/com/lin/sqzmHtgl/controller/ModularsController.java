package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.Modulars;
import com.lin.common.service.ModularsService;
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
 * @since 2023-01-09
 */
@RestController
@RequestMapping("/modulars")
public class ModularsController {
    @Autowired
    ModularsService modularsService;
    //获取modulars
    @NotNull
    @RequiresAuthentication
    @GetMapping("/get")
    public Result get(){
        List<Modulars> list = modularsService.list();
        return Result.succ("获取模块",list);
    }
    //添加modulars
    @NotNull
    @RequiresPermissions("modulars:add")
    @PostMapping("/add")
    public Result add(@RequestBody Modulars modulars){
        boolean b = modularsService.save(modulars);
        if (b){
            return Result.succ("添加模块成功");
        }
        return Result.fail("添加模块失败");
    }
    //修改modulars
    @RequiresPermissions("modulars:update")
    @PostMapping("/update")
    public Result update(@RequestBody Modulars modulars){
        boolean b = modularsService.updateById(modulars);
        if (b){
            return Result.succ("修改模块成功");
        }
        return Result.fail("修改模块失败");
    }
    //删除modulars
    @NotNull
    @RequiresPermissions("modulars:delete")
    @PostMapping("/delete")
    public Result delete(@NotNull @RequestBody Modulars modulars){
        boolean b = modularsService.removeById(modulars.getId());
        if (b){
            return Result.succ("删除模块成功");
        }
        return Result.fail("删除模块失败");
    }
}
