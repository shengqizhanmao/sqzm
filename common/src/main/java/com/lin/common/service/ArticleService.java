package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Article;
import com.lin.common.pojo.Body;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-22
 */
public interface ArticleService extends IService<Article> {

    Result img(MultipartFile file);

    Result saveArticle(Article article, String token, Body body, List<String> tagsIdList);

    Result getArticle(String token);

    Result getArticleByArticleId(String articleId, String token);

    Result getArticleByPalteId(String palteId, String token);

    Result deleteByArticleId(String id, String token);

    Result updateArticle(Article article, String token, Body body, List<String> tagsIdList);

    Result getArticleByPalteIdAndSort(String palteId, String modularsId, String sort, Long pages, Long pagesSize);

    Result getArticleDetail(String articleId, String token);

    Result getArticleByUserCollect(String token);

    Result getArticleListPageByModularsIdStatus(String modularsId, String status, Long size, Long page);


    Result updateStatus(String id, String status);

    boolean deleteByUserId(String userId);

    boolean deleteByUserId(String userId, TransactionStatus transactionStatus);


    Result getArticleByCateory(String palteId, String modularsId, String categoryId, Long page, Long pageSize);
}
