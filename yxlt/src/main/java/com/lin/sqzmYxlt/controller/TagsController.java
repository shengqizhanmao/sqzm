package com.lin.sqzmYxlt.controller;

import com.lin.common.service.TagsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @apiNote 标签
 * @author linShengWei
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/tags")
public class TagsController {
    @Resource
    private TagsService tagsService;
}
