package com.lin.common.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.ResultCode;
import com.lin.common.mapper.ArticleMapper;
import com.lin.common.pojo.*;
import com.lin.common.pojo.Vo.ArticleContentVo;
import com.lin.common.pojo.Vo.ArticleNoContentVo;
import com.lin.common.pojo.Vo.ArticleVo;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.service.*;
import com.lin.common.utils.MinioUtils;
import com.lin.common.utils.PagesHashMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-22
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleTagsService articleTagsService;
    @Resource
    private BodyService bodyService;
    @Resource
    private UserService userService;
    @Resource
    private TagsService tagsService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private PalteService palteService;
    @Resource
    private ModularsService modularsService;

    @Resource
    private ArticleLikesCollectionService articleLikesCollectionService;

    @Resource
    MinioUtils minioUtils;

    @Override
    public Result img(MultipartFile file) {
        //获取上传文件的文件名
        String fileName = file.getOriginalFilename();
        // 为了避免文件名重复，使用UUID重命名文件，将横杠去掉
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newFileName = uuid + fileName;
        minioUtils.MinioUtilsUpdate("article");
        try {
            String s = minioUtils.putObject(file.getInputStream(), newFileName, file.getContentType());
            // 返回文件名
            String fileName2 = s + "/" + newFileName;
            //进行修改头像
            return Result.succ("获取图片成功", fileName2);
        } catch (Exception e) {
            log.error("上传图片失败，原因是" + e);
            return Result.fail(ResultCode.IMAGE_UPLOAD_FAIL, "上传图片失败");
        } finally {
            minioUtils.MinioUtilsUpdateDefault();
        }
    }

    @Override
    public Result getArticle(String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        List<Article> listArticle = getListArticle(userByToken.getId());
        try {
            List<ArticleVo> articleVos = copyMethod(listArticle);
            return Result.succ("获取自己的文章成功", articleVos);
        } catch (Exception e) {
            return Result.fail(500, "出现错误,请反馈" + e);
        }
    }

    @Override
    public Result getArticleByArticleId(String articleId, String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        Article article = articleMapper.selectById(articleId);
        if (!article.getUserId().equals(userByToken.getId())) {
            return Result.fail("无权限不能查询");//非作者查询自己的文章
        }
        ArticleVo articleVo = copyMethod(article);
        return Result.succ("查询成功", articleVo);
    }

    @Override
    public Result getArticleByPalteId(String palteId, String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        List<Article> listArticle = getListArticle(userByToken.getId(), palteId);
        try {
            List<ArticleVo> articleVos = copyMethod(listArticle);
            return Result.succ("获取自己的文章成功", articleVos);
        } catch (Exception e) {
            return Result.fail(500, "出现错误,请反馈" + e);
        }
    }

    @Override
    public Result getArticleListPageByModularsIdStatus(String modularsId, String status, Long size, Long page) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getModularsId, modularsId)
                .eq(Article::getStatus, status);
        Page<Article> articlePage = articleMapper.selectPage(page, size, articleLambdaQueryWrapper);
        long total = articlePage.getTotal();
        List<Article> articleList = articlePage.getRecords();
        List<ArticleNoContentVo> articleNoContentVos = copy2Method(articleList);
        Map<String, Object> articleListMap = PagesHashMap.getPagesHashMap(total, "articleList", articleNoContentVos);
        return Result.succ("获取成功", articleListMap);
    }

    @Override
    public Result getArticleByPalteIdAndSort(String palteId, String modularsId, String sort, Long pages, Long pagesSize) {
        if (StringUtils.isBlank(palteId)) {
            return Result.fail("参数不能为空");
        }
        if (StringUtils.isBlank(sort)) {
            return Result.fail("参数不能为空");
        }
        if (StringUtils.isBlank(modularsId)) {
            return Result.fail("参数不能为空");
        }
        List<Article> listArticle = getListArticle(palteId, modularsId, sort, pages, pagesSize);
        List<ArticleNoContentVo> articleNoContentVos = copy2Method(listArticle);
        return Result.succ("查询成功", articleNoContentVos);
    }

    @Override
    public Result getArticleByUserCollect(String palteId, String token) {
        if (StringUtils.isBlank(palteId)) {
            return Result.fail("参数不能为空");
        }
        UserTokenVo userByToken = userService.findUserByToken(token);
        String id = userByToken.getId();
        LambdaQueryWrapper<ArticleLikesCollection> articleLikesCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLikesCollectionLambdaQueryWrapper.eq(ArticleLikesCollection::getUserId, id)
                .eq(ArticleLikesCollection::getCollection, "1");
        List<ArticleLikesCollection> list = articleLikesCollectionService.list(articleLikesCollectionLambdaQueryWrapper);
        List<Article> articles = new ArrayList<>();
        for (ArticleLikesCollection articleLikesCollection : list) {
            String articleId = articleLikesCollection.getArticleId();
            LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleLambdaQueryWrapper.eq(Article::getId, articleId)
                    .eq(Article::getPlateId, palteId);
            Article article = articleMapper.selectOne(articleLambdaQueryWrapper);
            if (article != null) {
                articles.add(article);
            }
        }
        List<ArticleNoContentVo> articleNoContentVos = copy2Method(articles);
        return Result.succ("查询成功", articleNoContentVos);
    }

    @Override
    public Result getArticleDetail(String articleId) {
        Article article = articleMapper.selectById(articleId);
        ArticleContentVo articleContentVo = copy2Method(article);
        article.setViewCounts(article.getViewCounts() + 1L);
        articleMapper.updateById(article);
        return Result.succ("获取成功", articleContentVo);
    }

    @Override
    public Result saveArticle(Article article, String token, Body body, List<String> tagsList) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        article.setUserId(userByToken.getId());
        int insert = articleMapper.insert(article);
        if (insert == 0) {
            return Result.fail("插入错误");
        }
        System.out.println(article.getId());
        body.setArticleId(article.getId());
        boolean save = bodyService.save(body);
        if (!save) {
            return Result.fail("插入错误");
        }
        article.setBodyId(body.getId());
        int i = articleMapper.updateById(article);
        if (i == 0) {
            return Result.fail("插入错误");
        }
        for (String tagsId : tagsList) {
            ArticleTags articleTags = new ArticleTags();
            articleTags.setArticleId(article.getId());
            articleTags.setTagsId(tagsId);
            boolean save1 = articleTagsService.save(articleTags);
            if (!save1) {
                return Result.fail("插入错误");
            }
        }
        return Result.succ("提交成功,请等待审核");
    }

    @Override
    public Result updateArticle(Article article, String token, Body body, List<String> tagsList) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        article.setUserId(userByToken.getId());
        Article articleSource = articleMapper.selectById(article.getId());
        BeanUtils.copyProperties(article, articleSource);
        article.setStatus("0");
        int insert = articleMapper.updateById(article);
        if (insert == 0) {
            return Result.fail("修改错误");
        }
        Body bodyByArticleId = bodyService.getBodyByArticleId(article.getId());
        bodyByArticleId.setContent(body.getContent());
        bodyByArticleId.setContentHtml(body.getContentHtml());
        boolean b = bodyService.updateById(bodyByArticleId);
        if (!b) {
            return Result.fail("修改错误");
        }

        List<ArticleTags> listArticleTagsByArticleId = articleTagsService.getListArticleTagsByArticleId(article.getId());
        for (ArticleTags articleTags : listArticleTagsByArticleId) {
            boolean b1 = articleTagsService.removeById(articleTags.getId());
            if (!b1) {
                return Result.fail("删除错误");
            }
        }
        for (String tagsId : tagsList) {
            ArticleTags articleTags = new ArticleTags();
            articleTags.setArticleId(article.getId());
            articleTags.setTagsId(tagsId);
            boolean save1 = articleTagsService.save(articleTags);
            if (!save1) {
                return Result.fail("插入错误");
            }
        }
        return Result.succ("提交成功,请等待审核");
    }

    @Override
    public Result updateStatus(String id, String status) {
        Article article = articleMapper.selectById(id);
        if (article.getStatus().equals(status)) {
            return Result.fail("已经修改过了,请重新刷新");
        }
        if (status.equals("1")) {
            article.setCreateDate(new Date());
        }
        article.setStatus(status);
        int i = articleMapper.updateById(article);
        if (i == 0) {
            return Result.fail("修改失败");
        }
        return Result.succ("修改成功");
    }

    @Override
    public Result deleteByArticleId(String id, String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        Article article = articleMapper.selectById(id);
        if (!userByToken.getId().equals(article.getUserId())) {
            return Result.fail("删除失败,不是作者本人");
        }
        int i = articleMapper.deleteById(id);
        if (i == 0) {
            return Result.fail("出现错误");
        }
        Body body = bodyService.getBodyByArticleId(id);
        boolean b = bodyService.removeById(body);
        if (!b) {
            return Result.fail("出现错误");
        }
        return Result.succ("删除成功");
    }

//方法区

    public List<Article> getListArticle(String userId) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getUserId, userId).orderByDesc(Article::getCreateDate);
        return articleMapper.selectList(articleLambdaQueryWrapper);
    }

    public List<Article> getListArticle(String userId, String palteId) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getPlateId, palteId).eq(Article::getUserId, userId).orderByDesc(Article::getCreateDate);
        return articleMapper.selectList(articleLambdaQueryWrapper);
    }

    public List<Article> getListArticle(String palteId, String modularsId, String sort, Long pages, Long pagesSize) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getPlateId, palteId);
        articleLambdaQueryWrapper.eq(Article::getStatus, "1");
        if (!modularsId.equals("0")) {    //如果等于0,就查询全部模块
            articleLambdaQueryWrapper.eq(Article::getModularsId, modularsId);
        }
        if (sort.equals("0")) {
            articleLambdaQueryWrapper.orderByDesc(Article::getCreateDate);
        }
        if (sort.equals("1")) {
            articleLambdaQueryWrapper.orderByDesc(Article::getLikesCounts);
        }
        if (sort.equals("2")) {
            articleLambdaQueryWrapper.orderByDesc(Article::getCommentCounts);
        }
        if (sort.equals("3")) {
            articleLambdaQueryWrapper.orderByDesc(Article::getCollectionCounts);
        }
        Page<Article> articleIPage = new Page<>(pages, pagesSize);
        Page<Article> articlePage = articleMapper.selectPage(articleIPage, articleLambdaQueryWrapper);
        List<Article> records = articlePage.getRecords();
        return records;
    }

    public List<String> getTagsNameList(String articleId) {
        List<ArticleTags> articleTagsList = articleTagsService.getListArticleTagsByArticleId(articleId);
        List<String> tagsNameList = new ArrayList<>();
        for (ArticleTags articleTags : articleTagsList) {
            Tags tags = tagsService.getById(articleTags.getTagsId());
            String name = tags.getName();
            tagsNameList.add(name);
        }
        return tagsNameList;
    }

    public List<Tags> getTags(List<ArticleTags> listArticleTagsByArticleId) {
        List<Tags> tagsList = new ArrayList<>();
        for (ArticleTags articleTags : listArticleTagsByArticleId) {
            Tags tags = tagsService.getById(articleTags.getTagsId());
            tagsList.add(tags);
        }
        return tagsList;
    }

    public ArticleContentVo copy2Method(Article article) {
        ArticleContentVo articleContentVo = new ArticleContentVo();
        //分类
        Category category = categoryService.getById(article.getCategoryId());
        articleContentVo.setCategory(category);
        //标签
        List<ArticleTags> listArticleTagsByArticleId = articleTagsService.getListArticleTagsByArticleId(article.getId());
        List<Tags> tags = getTags(listArticleTagsByArticleId);
        articleContentVo.setTagsList(tags);
        //作者昵称和头像
        User user = userService.getById(article.getUserId());
        articleContentVo.setUserId(article.getUserId());
        articleContentVo.setAvatar(user.getAvatar());
        articleContentVo.setNickname(user.getNickname());
        //帖子
        articleContentVo.setId(article.getId());
        articleContentVo.setFrontCover(article.getFrontCover());
        articleContentVo.setSummary(article.getSummary());
        articleContentVo.setTitle(article.getTitle());
        //点赞,评论,收藏,观看人数
        articleContentVo.setCommentCounts(article.getCommentCounts());
        articleContentVo.setViewCounts(article.getViewCounts());
        articleContentVo.setCollectionCounts(article.getCollectionCounts());
        articleContentVo.setLikesCounts(article.getLikesCounts());
        articleContentVo.setCreateDate(article.getCreateDate());
        //帖子内容
        Body bodyByArticleId = bodyService.getBodyByArticleId(article.getId());
        articleContentVo.setBody(bodyByArticleId);
        return articleContentVo;
    }

    public List<ArticleNoContentVo> copy2Method(List<Article> listArticle) {
        List<ArticleNoContentVo> articleNoContentVos = new ArrayList<>();
        for (Article article : listArticle) {
            ArticleNoContentVo articleNoContentVo = new ArticleNoContentVo();
            //分类
            Category category = categoryService.getById(article.getCategoryId());
            articleNoContentVo.setCategory(category);
            //标签
            List<ArticleTags> listArticleTagsByArticleId = articleTagsService.getListArticleTagsByArticleId(article.getId());
            List<Tags> tags = getTags(listArticleTagsByArticleId);
            articleNoContentVo.setTagsList(tags);
            //作者昵称和头像
            User user = userService.getById(article.getUserId());
            articleNoContentVo.setUserId(article.getUserId());
            articleNoContentVo.setAvatar(user.getAvatar());
            articleNoContentVo.setNickname(user.getNickname());
            //其他一样的赋值
            BeanUtils.copyProperties(article, articleNoContentVo);
            articleNoContentVos.add(articleNoContentVo);
        }
        return articleNoContentVos;
    }

    public List<ArticleVo> copyMethod(List<Article> listArticle) {
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article article : listArticle) {
            //文章
            ArticleVo articleVo = copyMethod(article);
            articleVos.add(articleVo);
        }
        return articleVos;
    }

    public ArticleVo copyMethod(Article article) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        //作者
        User byId = userService.getById(article.getUserId());
        articleVo.setNickname(byId.getNickname());
        articleVo.setUserId(byId.getId());
        //内容
        Body body = bodyService.getBodyByArticleId(article.getId());
        articleVo.setContent(body.getContent());
        articleVo.setContentHtml(body.getContentHtml());
        //标签
        List<String> tagsNameList = getTagsNameList(article.getId());
        articleVo.setTagsNameList(tagsNameList);
        //板块
        Palte palte = palteService.getById(article.getPlateId());
        articleVo.setPalteName(palte.getName());
        //模块
        Modulars modulars = modularsService.getById(article.getModularsId());
        articleVo.setModularsName(modulars.getName());
        //分类
        Category category = categoryService.getById(article.getCategoryId());
        articleVo.setCategoryName(category.getName());
        return articleVo;
    }

    public Article copyArticleMethod(Article article, Article articleSource) {
        articleSource.setCategoryId(article.getCategoryId());
        articleSource.setCreateDate(new Date());
        articleSource.setTitle(article.getTitle());
        articleSource.setPlateId(article.getPlateId());
        articleSource.setSummary(article.getSummary());
        articleSource.setFrontCover(article.getFrontCover());
        articleSource.setModularsId(article.getModularsId());
        return articleSource;
    }

}
