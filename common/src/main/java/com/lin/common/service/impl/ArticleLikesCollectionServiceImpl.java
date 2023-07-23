package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.ArticleLikesCollectionMapper;
import com.lin.common.pojo.Article;
import com.lin.common.pojo.ArticleLikesCollection;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.service.ArticleLikesCollectionService;
import com.lin.common.service.ArticleService;
import com.lin.common.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-27
 */
@Service
public class ArticleLikesCollectionServiceImpl extends ServiceImpl<ArticleLikesCollectionMapper, ArticleLikesCollection> implements ArticleLikesCollectionService {


    @Resource
    private ArticleLikesCollectionMapper articleLikesCollectionMapper;

    @Resource
    private ArticleService articleService;
    @Resource
    private UserService userService;

    @Override
    public Result getByArticle(String articleId, String token) {
        //获取登录UserId,通过articleId和UserId查询是否,
        UserTokenVo userByToken = userService.findUserByToken(token);
        if (userByToken == null) {
            return Result.fail("请重新登录");
        }
        ArticleLikesCollection articleLikesCollection = getByArticleMethod(articleId, userByToken.getId());
        return Result.succ("获取成功", articleLikesCollection);
    }

    @Override
    public List<ArticleLikesCollection> getByArticleId(String articleId){
        LambdaQueryWrapper<ArticleLikesCollection> articleLikesCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLikesCollectionLambdaQueryWrapper.eq(ArticleLikesCollection::getArticleId, articleId);
        return articleLikesCollectionMapper.selectList(articleLikesCollectionLambdaQueryWrapper);
    }

    @Override
    public Result updateByArticleId(String articleId, String likes, String collection, String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        if (userByToken == null) {
            return Result.fail("请重新登录");
        }
        String id = userByToken.getId();
        ArticleLikesCollection articleLikesCollection = getByArticleMethod(articleId, id);
        Article article = articleService.getById(articleId);
        //当没有记录时,创建一个新记录
        if (articleLikesCollection == null) {
            ArticleLikesCollection articleLikesCollection1 = new ArticleLikesCollection();
            articleLikesCollection1.setArticleId(articleId);
            articleLikesCollection1.setCollection(collection);
            articleLikesCollection1.setUserId(id);
            articleLikesCollection1.setLikes(likes);
            if (likes.equals("1")) {
                article.setLikesCounts(article.getLikesCounts() + 1L);
            }
            if (collection.equals("1")) {
                article.setCollectionCounts(article.getCollectionCounts() + 1L);
            }
            articleService.updateById(article);
            articleLikesCollectionMapper.insert(articleLikesCollection1);
            return Result.succ("修改成功");
        }

        //如果收藏数相同,则说明修改的是点赞,
        if (articleLikesCollection.getCollection().equals(collection)) {
            //当点赞为1,则表示点赞true,文章点赞数+!
            if (likes.equals("1")) {
                article.setLikesCounts(article.getLikesCounts() + 1L);
                articleService.updateById(article);
                Update(articleLikesCollection, likes, collection);
                return Result.succ("修改成功");
            }
            article.setLikesCounts(article.getLikesCounts() - 1L);
            articleService.updateById(article);
            Update(articleLikesCollection, likes, collection);
            return Result.succ("修改成功");
        }
        if (collection.equals("1")) {
            article.setCollectionCounts(article.getCollectionCounts() + 1L);
            articleService.updateById(article);
            Update(articleLikesCollection, likes, collection);
            return Result.succ("修改成功");
        }
        article.setCollectionCounts(article.getCollectionCounts() - 1L);
        articleService.updateById(article);
        Update(articleLikesCollection, likes, collection);
        return Result.succ("修改成功");
    }

    @Override
    public boolean deleteByUserId(String id, TransactionStatus transaction) {
        try {
            LambdaQueryWrapper<ArticleLikesCollection> articleLikesCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleLikesCollectionLambdaQueryWrapper.eq(ArticleLikesCollection::getUserId,id);
            articleLikesCollectionMapper.delete(articleLikesCollectionLambdaQueryWrapper);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void Update(ArticleLikesCollection articleLikesCollection, String likes, String collection) {
        articleLikesCollection.setCollection(collection);
        articleLikesCollection.setLikes(likes);
        articleLikesCollectionMapper.updateById(articleLikesCollection);
    }

    public ArticleLikesCollection getByArticleMethod(String articleId, String id) {
        //获取登录UserId,通过articleId和UserId查询是否,
        LambdaQueryWrapper<ArticleLikesCollection> articleLikesCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLikesCollectionLambdaQueryWrapper.eq(ArticleLikesCollection::getArticleId, articleId)
                .eq(ArticleLikesCollection::getUserId, id);
        return articleLikesCollectionMapper.selectOne(articleLikesCollectionLambdaQueryWrapper);
    }
}
