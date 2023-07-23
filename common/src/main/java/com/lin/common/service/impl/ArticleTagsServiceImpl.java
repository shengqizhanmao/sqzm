package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.mapper.ArticleTagsMapper;
import com.lin.common.pojo.ArticleTags;
import com.lin.common.service.ArticleTagsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-23
 */
@Service
public class ArticleTagsServiceImpl extends ServiceImpl<ArticleTagsMapper, ArticleTags> implements ArticleTagsService {

    @Resource
    private ArticleTagsMapper articleTagsMapper;

    @Override
    public List<ArticleTags> getListArticleTagsByArticleId(String id) {
        LambdaQueryWrapper<ArticleTags> articleTagsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagsLambdaQueryWrapper.eq(ArticleTags::getArticleId, id);
        return articleTagsMapper.selectList(articleTagsLambdaQueryWrapper);
    }

}
