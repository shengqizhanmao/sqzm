package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/getByPalteId")
    public Result getByPalteId(@RequestParam("palteId") String palteId) {
        return categoryService.getByPalteId(palteId);
    }

}
