package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.Palte;
import com.lin.common.service.PalteService;
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
@RequestMapping("/palte")
public class PalteController {
    @Autowired
    PalteService palteService;

    //获取palte
    @NotNull
    @RequiresAuthentication
    @GetMapping("/get")
    public Result get() {
        List<Palte> list = palteService.list();
        return Result.succ("获取板块", list);
    }

    //添加palte
    @RequiresPermissions("palte:add")
    @PostMapping("/add")
    public Result add(@RequestBody Palte palte) {
        boolean b = palteService.save(palte);
        if (b) {
            return Result.succ("添加板块成功");
        }
        return Result.fail("添加板块失败");
    }

    //修改palte
    @RequiresPermissions("palte:update")
    @PutMapping("/update")
    public Result update(@RequestBody Palte palte) {
        boolean b = palteService.updateById(palte);
        if (b) {
            return Result.succ("修改板块成功");
        }
        return Result.fail("修改板块失败");
    }

    //删除palte
    @NotNull
    @RequiresPermissions("palte:delete")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return Result.fail("参数不能为空");
        }
        boolean b = palteService.removeById(id);
        if (b) {
            return Result.succ("删除板块成功");
        }
        return Result.fail("删除板块失败");
    }
}
