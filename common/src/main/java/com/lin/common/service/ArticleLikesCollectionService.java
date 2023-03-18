package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.ArticleLikesCollection;

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

    Result updateByArticleId(String articleId, String likes, String collection, String token);
}
