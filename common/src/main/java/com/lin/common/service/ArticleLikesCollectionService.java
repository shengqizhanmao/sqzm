package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.ArticleLikesCollection;
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
public interface ArticleLikesCollectionService extends IService<ArticleLikesCollection> {




    Result getByArticle(String articleId, String token);

    public List<ArticleLikesCollection> getByArticleId(String articleId);

    Result updateByArticleId(String articleId, String likes, String collection, String token);

    boolean deleteByUserId(String id, TransactionStatus transaction);
}
