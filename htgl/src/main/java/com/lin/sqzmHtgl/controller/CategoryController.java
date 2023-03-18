package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.Category;
import com.lin.common.service.CategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @apiNote 分类
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    CategoryService categoryService;

    @RequiresPermissions("category:get")
    @GetMapping("/get/{palteId}")
    public Result getByPalteId(@PathVariable("palteId") String palteId) {
        return categoryService.getByPalteId(palteId);
    }

    @RequiresPermissions("category:add")
    @PostMapping("/add")
    public Result add(@RequestBody Category category) {
        boolean save = categoryService.save(category);
        if (save) {
            return Result.succ("添加成功");
        }
        return Result.fail("添加失败");
    }

    @RequiresPermissions("category:update")
    @PutMapping("/update")
    public Result update(@RequestBody Category category) {
        boolean b = categoryService.updateById(category);
        if (b) {
            return Result.succ("修改成功");
        }
        return Result.fail("修改失败");
    }

    @RequiresPermissions("category:delete")
    @DeleteMapping("/delete/{categoryId}")
    public Result delete(@PathVariable("categoryId") String categoryId) {
        boolean b = categoryService.removeById(categoryId);
        if (b) {
            return Result.succ("删除成功");
        }
        return Result.fail("删除失败");
    }


}
