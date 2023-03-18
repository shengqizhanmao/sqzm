package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.pojo.ArticleTags;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-23
 */
public interface ArticleTagsService extends IService<ArticleTags> {

    List<ArticleTags> getListArticleTagsByArticleId(String id);
}
