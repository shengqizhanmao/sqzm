package com.lin.sqzmHtgl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.common.Result;
import com.lin.common.pojo.Modulars;
import com.lin.common.service.ModularsService;
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
 * @since 2023-01-09
 */
@RestController
@RequestMapping("/modulars")
public class ModularsController {
    @Autowired
    ModularsService modularsService;

    //获取modulars列表
    @NotNull
    @RequiresAuthentication
    @GetMapping("/get")
    public Result get() {
        List<Modulars> list = modularsService.list();
        return Result.succ("获取模块", list);
    }

    //获取模块列表,根据板块
    @NotNull
    @RequiresAuthentication
    @GetMapping("/get/{palteId}")
    public Result getByPalteId(@PathVariable("palteId") String palteId) {
        LambdaQueryWrapper<Modulars> modularsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        modularsLambdaQueryWrapper.eq(Modulars::getPalteId, palteId).orderByAsc(Modulars::getSort);
        List<Modulars> list = modularsService.list(modularsLambdaQueryWrapper);
        return Result.succ("获取模块", list);
    }

    //添加模块
    @NotNull
    @RequiresPermissions("modulars:add")
    @PostMapping("/add")
    public Result add(@RequestBody Modulars modulars) {
        boolean b = modularsService.save(modulars);
        if (b) {
            return Result.succ("添加模块成功");
        }
        return Result.fail("添加模块失败");
    }

    //修改模块
    @RequiresPermissions("modulars:update")
    @PutMapping("/update")
    public Result update(@RequestBody Modulars modulars) {
        boolean b = modularsService.updateById(modulars);
        if (b) {
            return Result.succ("修改模块成功");
        }
        return Result.fail("修改模块失败");
    }

    //删除模块
    @NotNull
    @RequiresPermissions("modulars:delete")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return Result.fail("参数不能为空");
        }
        boolean b = modularsService.removeById(id);
        if (b) {
            return Result.succ("删除模块成功");
        }
        return Result.fail("删除模块失败");
    }
}
