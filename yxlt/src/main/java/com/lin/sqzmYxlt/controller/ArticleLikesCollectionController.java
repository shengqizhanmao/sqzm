package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.service.ArticleLikesCollectionService;
import com.lin.sqzmYxlt.controller.param.UpdateArticleLikesCollection;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/articleLikesCollection")
public class ArticleLikesCollectionController {


    @Resource
    private ArticleLikesCollectionService articleLikesCollectionService;

    @GetMapping("/getByArticleId")
    public Result getByArticleId(@RequestParam("articleId") String articleId, @RequestHeader("Authorization") String token) {
        return articleLikesCollectionService.getByArticle(articleId, token);
    }

    @PostMapping("/updateByArticleId")
    public Result updateByArticleId(@RequestBody UpdateArticleLikesCollection updateArticleLikesCollection, @RequestHeader("Authorization") String token) {
        String collection = updateArticleLikesCollection.getCollection();
        String likes = updateArticleLikesCollection.getLikes();
        String articleId = updateArticleLikesCollection.getArticleId();
        return articleLikesCollectionService.updateByArticleId(articleId, likes, collection, token);
    }
}
