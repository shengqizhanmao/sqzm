package com.lin.sqzmHtgl.controller;


import com.lin.common.Result;
import com.lin.common.pojo.Body;
import com.lin.common.service.ArticleService;
import com.lin.common.service.BodyService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @apiNote 文章
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private BodyService bodyService;

    //上传图片到article桶
    @PostMapping("/img")
    public Result img(@RequestParam("file") MultipartFile file) {
        return articleService.img(file);
    }

    //获取帖子列表
    @GetMapping("/get/{modularsId}/{status}/{size}/{page}")
    public Result get(@PathVariable("modularsId") String modularsId, @PathVariable("status") String status, @PathVariable("size") Long size, @PathVariable("page") Long page) {
        return articleService.getArticleListPageByModularsIdStatus(modularsId, status, size, page);
    }

    //获取帖子内容
    @GetMapping("/getBody/{id}")
    public Result get(@PathVariable("id") String id) {
        Body body = bodyService.getBodyByArticleId(id);
        return Result.succ("获取帖子内容成功", body);
    }

    //修改帖子的状态,1为审核成功,0为正在审核,-1为审核失败
    @PutMapping("/update/{id}/{status}")
    public Result update(@PathVariable("id") String id, @PathVariable("status") String status) {
        return articleService.updateStatus(id, status);
    }

}
