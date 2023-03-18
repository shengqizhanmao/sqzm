package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.service.CommentService;
import com.lin.sqzmYxlt.controller.param.CommentCreateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @apiNote 评论
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    //获取评论
    @GetMapping("/getByArticleId")
    public Result comments(@RequestParam("articleId") String articleId) {
        return commentService.getCommentsByArticleId(articleId);
    }

    //进行评论
    @PostMapping("/create")
    public Result create(@RequestBody CommentCreateParam commentCreateParam, @RequestHeader("Authorization") String token) {
        String articleId = commentCreateParam.getArticleId();
        String content = commentCreateParam.getContent();
        String toUserId = commentCreateParam.getToUserId();
        String parentId = commentCreateParam.getParentId();
        return commentService.create(articleId, content, toUserId, parentId, token);
    }
}
