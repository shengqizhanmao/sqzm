package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Comment;
import org.springframework.transaction.TransactionStatus;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-27
 */
public interface CommentService extends IService<Comment> {

    Result getCommentsVoByArticleId(String articleId);

    List<Comment> getCommentsByArticleId(String articleId);
    List<Comment> getCommentsByUserId(String id);

    Result create(String articleId, String content, String toUserId, String parentId, String token);

    Result delete(String id, String token);

    boolean deleteByUserId(String id, TransactionStatus transaction);
}
