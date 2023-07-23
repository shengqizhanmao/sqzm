package com.lin.sqzmYxlt.controller;


import com.lin.common.Result;
import com.lin.common.pojo.Article;
import com.lin.common.pojo.Body;
import com.lin.common.service.ArticleService;
import com.lin.sqzmYxlt.controller.param.AddArticle;
import com.lin.sqzmYxlt.controller.param.GetArticle;
import org.jetbrains.annotations.Nullable;
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

    @PostMapping("/img")
    public Result img(@RequestParam("file") MultipartFile file) {
        return articleService.img(file);
    }

    //作者添加申请帖子,申请成功才能被用户查看
    @PostMapping("/apply")
    public Result apply(@RequestBody AddArticle addArticle, @RequestHeader("Authorization") String token) {
        Article article = copyArticleMethod(addArticle);
        Body body = copyBodyMethod(addArticle);
        List<String> tagsIdList = addArticle.getTagsList();
        return articleService.saveArticle(article, token, body, tagsIdList);
    }

    //作者获取自己的帖子
    @GetMapping("/get")
    public Result get(@RequestHeader("Authorization") String token) {
        return articleService.getArticle(token);
    }

    @GetMapping("/get/{articleId}")
    public Result getById(@PathVariable("articleId") String articleId, @RequestHeader("Authorization") String token) {
        return articleService.getArticleByArticleId(articleId, token);
    }

    //作者获取自己的详细帖子内容
    @GetMapping("/getArticleByPalteId")
    public Result getArticleByPalteId(@RequestParam("palteId") String palteId, @RequestHeader("Authorization") String token) {
        return articleService.getArticleByPalteId(palteId, token);
    }

    //用户获取收藏的帖子
    @GetMapping("/getArticleByUserCollect")
    public Result getArticleByUserCollect(@RequestHeader("Authorization") String token) {
        return articleService.getArticleByUserCollect(token);
    }


    //作者删除自己的帖子
    @PostMapping("/deleteByArticleId")
    public Result deleteByArticleId(@RequestBody String articleId, @RequestHeader("Authorization") String token) {
        return articleService.deleteByArticleId(articleId, token);
    }

    //作者修改自己的帖子
    @PostMapping("/update")
    public Result update(@RequestBody AddArticle addArticle, @RequestHeader("Authorization") String token) {
        Article article = copyArticleMethod(addArticle);
        article.setId(addArticle.getId());
        Body body = copyBodyMethod(addArticle);
        body.setArticleId(addArticle.getId());
        List<String> tagsIdList = addArticle.getTagsList();
        return articleService.updateArticle(article, token, body, tagsIdList);
    }

    //普通用户获取帖子的基本内容，根据排序
    @PostMapping("/getBySortAndPalteAndModulersId")
    public Result getBySortAndPalteAndModulersId(@RequestBody GetArticle getArticle) {
        String sort = getArticle.getSort();
        String palteId = getArticle.getPalteId();
        String modularsId = getArticle.getModularsId();
        Long pages = getArticle.getPages();
        Long pagesSize = getArticle.getPagesSize();
        return articleService.getArticleByPalteIdAndSort(palteId, modularsId, sort, pages, pagesSize);
    }
    //普通用户获取帖子的基本内容，根据分类
    @GetMapping("/getArticleByCategory/{palteId}/{modularsId}/{categoryId}/{page}/{pageSize}")
    public Result getArticleByPalteId(@PathVariable("palteId")String palteId,
                                      @PathVariable("modularsId")String modularsId,
                                      @PathVariable("categoryId")String categoryId,
                                      @PathVariable("page")Long page,
                                      @PathVariable("pageSize")Long pageSize) {
        return articleService.getArticleByCateory(palteId,modularsId,categoryId,page,pageSize);
    }

    //普通用户获取帖子的详细内容
    @GetMapping("/getArticleDetail")
    public Result getArticleDetail(@RequestParam("articleId") String articleId,@RequestHeader(value = "Authorization", required = false) String token) {
        return articleService.getArticleDetail(articleId,token);
    }

    public Article copyArticleMethod(AddArticle addArticle) {
        Article article = new Article();
        article.setCategoryId(addArticle.getCategoryId());
        article.setCreateDate(new Date());
        article.setTitle(addArticle.getTitle());
        article.setPlateId(addArticle.getPalteId());
        article.setSummary(addArticle.getSummary());
        article.setFrontCover(addArticle.getFrontCover());
        article.setModularsId(addArticle.getModularsId());
        article.setStatus("0");
        article.setCollectionCounts(0L);
        article.setLikesCounts(0L);
        article.setViewCounts(0L);
        article.setCommentCounts(0L);
        return article;
    }

    public Body copyBodyMethod(AddArticle addArticle) {
        Body body = new Body();
        body.setContentHtml(addArticle.getContentHtml());
        body.setContent(addArticle.getContent());
        return body;
    }
}
