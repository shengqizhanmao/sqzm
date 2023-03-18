package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.service.TagsService;
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
 * @apiNote 标签
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/tags")
public class TagsController {
    @Resource
    private TagsService tagsService;

    @GetMapping("/getByPalteId")
    public Result getByPalteId(@RequestParam("palteId") String palteId) {
        return tagsService.getByPalteId(palteId);
    }
}
