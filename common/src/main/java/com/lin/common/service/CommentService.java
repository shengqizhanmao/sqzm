package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Comment;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-27
 */
public interface CommentService extends IService<Comment> {

    Result getCommentsByArticleId(String articleId);

    Result create(String articleId, String content, String toUserId, String parentId, String token);
}
