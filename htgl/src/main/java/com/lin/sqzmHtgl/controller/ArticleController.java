package com.lin.sqzmHtgl.controller;


import com.lin.common.Result;
import com.lin.common.pojo.Article;
import com.lin.common.pojo.Body;
import com.lin.common.service.ArticleService;
import com.lin.common.service.BodyService;
import com.lin.sqzmHtgl.controller.param.UpdateArticle;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


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
    @RequiresPermissions("article:add")
    @PostMapping("/img")
    public Result img(@RequestParam("file") MultipartFile file) {
        return articleService.img(file);
    }

    @RequiresPermissions("article:get")
    //获取帖子列表
    @GetMapping("/get/{modularsId}/{status}/{size}/{page}")
    public Result get(@PathVariable("modularsId") String modularsId, @PathVariable("status") String status, @PathVariable("size") Long size, @PathVariable("page") Long page) {
        return articleService.getArticleListPageByModularsIdStatus(modularsId, status, size, page);
    }
    
    @RequiresPermissions("article:get")
    //获取帖子内容
    @GetMapping("/getBody/{id}")
    public Result get(@PathVariable("id") String id) {
        Body body = bodyService.getBodyByArticleId(id);
        return Result.succ("获取帖子内容成功", body);
    }

    @RequiresPermissions("article:update")
    //修改帖子的状态,1为审核成功,0为正在审核,-1为审核失败
    @PutMapping("/update/{id}/{status}")
    public Result update(@PathVariable("id") String id, @PathVariable("status") String status) {
        return articleService.updateStatus(id, status);
    }

    @RequiresPermissions("article:update")
    //修改帖子的状态,1为审核成功,0为正在审核,-1为审核失败
    @PutMapping("/update")
    public Result update2(@RequestBody UpdateArticle updateArticle) {
        Article article = new Article();
        BeanUtils.copyProperties(updateArticle,article);
        article.setId(updateArticle.getId());
        Body body = new Body();
        BeanUtils.copyProperties(updateArticle,body);
        body.setArticleId(updateArticle.getId());
        List<String> tagsIdList = updateArticle.getTagsList();
        return articleService.updateArticle(article, null, body, tagsIdList);
    }


   private Article copyMethod(UpdateArticle updateArticle){
       Article article = articleService.getById(updateArticle.getId());
       article.setSummary(updateArticle.getSummary());
       article.setTitle(updateArticle.getTitle());
       article.setCategoryId(updateArticle.getCategoryId());
       article.setPlateId(updateArticle.getPalteId());
       article.setPlateId(updateArticle.getModularsId());
       article.setFrontCover(updateArticle.getFrontCover());
       return article;
   }
}
