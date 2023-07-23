package com.lin.common.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.RedisStatus;
import com.lin.common.Result;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
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
    private CommentService commentService;

    @Resource
    MinioUtils minioUtils;

    @Override
    public Result img(MultipartFile file) {
        //获取上传文件的文件名
        String fileName = file.getOriginalFilename();
        //获取上传文件的类型是否为图片
        String contentType = file.getContentType();
        System.out.println(contentType);
        if (Objects.equals(contentType, "BMP")){
            return Result.fail("上传图片失败,非图片,允许类型为BMP、TIFF、GIF、PNG、JPEG、JPG");
        }
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
            return Result.fail(501, "上传图片失败");
        } finally {
            minioUtils.MinioUtilsUpdateDefault();
        }
    }

    @Override
    public Result getArticle(String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        if (userByToken==null) {
            return Result.fail("请重新登录");
        }
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
        if(userByToken==null){
            return Result.fail("请重新登录");
        }
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
        if(userByToken==null){
            return Result.fail("请重新登录");
        }
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


    /*
    * @param String palteId； 板块id
    * @param String modularsId； 模块区id
    * @param String sort；    排序方法
    * @param Long pages；    当前页
    * @param Long pagesSize； 页大小
    * */
    @Override
    public Result getArticleByPalteIdAndSort(String palteId, String modularsId, String sort, Long pages, Long pagesSize) {
        if (StringUtils.isBlank(palteId)||StringUtils.isBlank(sort)||StringUtils.isBlank(modularsId)) {
            return Result.fail("参数不能为空");
        }
        if(pages<=0L || pagesSize<=0L){
            return Result.fail("参数错误");
        }
        List<Article> listArticle = getListArticle(palteId, modularsId, sort, pages, pagesSize);
        List<ArticleNoContentVo> articleNoContentVos = copy2Method(listArticle);
        return Result.succ("查询成功", articleNoContentVos);
    }

    @Override
    public Result getArticleByCateory(String palteId, String modularsId, String categoryId, Long page, Long pageSize) {
        if (StringUtils.isBlank(palteId)||StringUtils.isBlank(modularsId)||StringUtils.isBlank(categoryId)) {
            return Result.fail("参数不能为空");
        }
        if(page<=0L || pageSize<=0L){
            return Result.fail("参数错误");
        }
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getPlateId, palteId);
        articleLambdaQueryWrapper.eq(Article::getModularsId, modularsId);
        articleLambdaQueryWrapper.eq(Article::getStatus, "1");
        articleLambdaQueryWrapper.eq(Article::getCategoryId, categoryId);
        Page<Article> articlePage = articleMapper.selectPage(page,pageSize, articleLambdaQueryWrapper);
        List<Article> records = articlePage.getRecords();
        List<ArticleNoContentVo> articleNoContentVos = copy2Method(records);
        return Result.succ("查询成功", articleNoContentVos);
    }



    //获取文章，根据收藏
    @Override
    public Result getArticleByUserCollect(String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        if(userByToken==null){
            return Result.fail("请重新登录");
        }
        String id = userByToken.getId();
        //获取收藏的文章id
        LambdaQueryWrapper<ArticleLikesCollection> articleLikesCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLikesCollectionLambdaQueryWrapper.eq(ArticleLikesCollection::getUserId, id)
                .eq(ArticleLikesCollection::getCollection, "1");
        List<ArticleLikesCollection> list = articleLikesCollectionService.list(articleLikesCollectionLambdaQueryWrapper);
        //获取文章列表,根据文章id列表
        List<String> articleIdList = new ArrayList<>();
        for (ArticleLikesCollection articleLikesCollection : list) {
            articleIdList.add(articleLikesCollection.getArticleId());
        }
        List<Article> articles = articleMapper.selectBatchIds(articleIdList);
        //Vo
        List<ArticleNoContentVo> articleNoContentVos = copy2Method(articles);
        return Result.succ("查询成功", articleNoContentVos);
    }

    //获取文章的详细内容
    @Override
    public Result getArticleDetail(String articleId, String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        Article article = articleMapper.selectById(articleId);
        ArticleContentVo articleContentVo = copy2Method(article);
        String s = redisTemplate.opsForValue().get(RedisStatus.ARTCILE_USER_VIEW_TOKEN + token + ":" + articleId);
        //未登录不增加view,登录之后查询相同文章,再一次查询间隔1分钟不增加view
        if (userByToken==null || s!=null){
            article.setViewCounts(article.getViewCounts());
        }else{
            redisTemplate.opsForValue().set(RedisStatus.ARTCILE_USER_VIEW_TOKEN+token+":"+articleId,"1",1, TimeUnit.MINUTES);
            article.setViewCounts(article.getViewCounts() + 1L);
        }
        articleMapper.updateById(article);
        return Result.succ("获取成功", articleContentVo);
    }

    //添加帖子
    @Override
    public Result saveArticle(Article article, String token, Body body, List<String> tagsIdList) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        UserTokenVo userByToken = userService.findUserByToken(token);
        if(userByToken==null){
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("请重新登录");
        }
        //添加作者id,
        article.setUserId(userByToken.getId());
        int insert = articleMapper.insert(article);
        if (insert == 0) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("插入错误");
        }
        //添加文章id
        body.setArticleId(article.getId());
        boolean save = bodyService.save(body);
        if (!save) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("插入错误");
        }
        //添加内容id
        article.setBodyId(body.getId());
        int i = articleMapper.updateById(article);
        if (i == 0) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("插入错误");
        }
        //添加文章标签
        List<ArticleTags> articleTagsList = copyMethod(tagsIdList, article.getId());
        boolean b2 = articleTagsService.saveBatch(articleTagsList);
        if (!b2) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("插入错误");
        }
        dataSourceTransactionManager.commit(transactionStatus);
        return Result.succ("提交成功,请等待审核");
    }

    //用户修改帖子
    @Override
    public Result updateArticle(Article article, String token, Body body, List<String> tagsIdList) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        UserTokenVo userByToken = userService.findUserByToken(token);
        if(userByToken==null){
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("请重新登录");
        }
        //获取原来的文章
        article.setUserId(userByToken.getId());
        Article articleSource = articleMapper.selectById(article.getId());
        BeanUtils.copyProperties(article, articleSource);
        //需要重新审核
        article.setStatus("0");
        //修改
        int insert = articleMapper.updateById(article);
        if (insert == 0) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("修改错误");
        }
        //修改文章的内容
        Body bodyByArticleId = bodyService.getBodyByArticleId(article.getId());
        bodyByArticleId.setContent(body.getContent());
        bodyByArticleId.setContentHtml(body.getContentHtml());
        boolean b = bodyService.updateById(bodyByArticleId);
        if (!b) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("修改错误");
        }
        //修改文章标签,先全部删除文章标签,再添加文章标签
        List<ArticleTags> listArticleTagsByArticleId = articleTagsService.getListArticleTagsByArticleId(article.getId());
        for (ArticleTags articleTags : listArticleTagsByArticleId) {
            boolean b1 = articleTagsService.removeById(articleTags.getId());
            if (!b1) {
                dataSourceTransactionManager.rollback(transactionStatus);
                return Result.fail("删除错误");
            }
        }
        //修改文章标签
        List<ArticleTags> articleTagsList = copyMethod(tagsIdList, article.getId());
        boolean b2 = articleTagsService.saveBatch(articleTagsList);
        if (!b2) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("插入错误");
        }
        dataSourceTransactionManager.commit(transactionStatus);
        return Result.succ("提交成功,请等待审核");
    }

    //管理员修改帖子



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
        //事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        //登录用户
        UserTokenVo userByToken = userService.findUserByToken(token);
        if(userByToken==null){
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("请重新登录");
        }
        //删除帖子
        Article article = articleMapper.selectById(id);
        if (!userByToken.getId().equals(article.getUserId())) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("删除失败,不是作者本人");
        }
        int i = articleMapper.deleteById(id);
        if (i == 0) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("出现错误");
        }
        //帖子内容
        Body body = bodyService.getBodyByArticleId(id);

        boolean b = bodyService.removeById(body);
        if (!b) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("出现错误");
        }
        dataSourceTransactionManager.commit(transactionStatus);
        return Result.succ("删除成功");
    }

    @Override
    public boolean deleteByUserId(String userId) {
        //事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        boolean b = deleteByUserId(userId, transactionStatus);
        if (b){
            dataSourceTransactionManager.commit(transactionStatus);
        }
        return b;
    }


    @Override
    public boolean deleteByUserId(String userId, TransactionStatus transactionStatus) {
        //验证用户是否存在。
        User byId = userService.getById(userId);
        if(byId==null){
            return false;
        }
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getUserId,userId);
        List<Article> articleList = articleMapper.selectList(articleLambdaQueryWrapper);
        if (articleList.size()==0){
            return true;
        }
        return deleteByArticleListMethod(articleList, transactionStatus);
    }

//方法区
    public List<Article> getListArticle(String userId) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper
                .eq(Article::getUserId, userId)
                .orderByDesc(Article::getCreateDate);
        return articleMapper.selectList(articleLambdaQueryWrapper);
    }
    private List<Article> getListArticle(String userId, String palteId) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper
                .eq(Article::getPlateId, palteId)
                .eq(Article::getUserId, userId)
                .orderByDesc(Article::getCreateDate);
        return articleMapper.selectList(articleLambdaQueryWrapper);
    }
    private List<Article> getListArticle(String palteId, String modularsId, String sort, Long pages, Long pagesSize) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getPlateId, palteId);
        articleLambdaQueryWrapper.eq(Article::getStatus, "1");
        //如果等于0,就查询全部模块
        articleLambdaQueryWrapper.eq(!modularsId.equals("0"),Article::getModularsId, modularsId);
        //0,创建日期排序
        articleLambdaQueryWrapper.orderByDesc(sort.equals("0"),Article::getCreateDate);
        //1,点赞排序
        articleLambdaQueryWrapper.orderByDesc(sort.equals("1"),Article::getLikesCounts);
        //2,评论人数
        articleLambdaQueryWrapper.orderByDesc(sort.equals("2"),Article::getCommentCounts);
        //3,收藏人数
        articleLambdaQueryWrapper.orderByDesc(sort.equals("3"),Article::getCollectionCounts);
        Page<Article> articleIPage = new Page<>(pages, pagesSize);
        Page<Article> articlePage = articleMapper.selectPage(articleIPage, articleLambdaQueryWrapper);
        return articlePage.getRecords();
    }
    private List<String> getTagsNameList(String articleId) {
        List<ArticleTags> articleTagsList = articleTagsService.getListArticleTagsByArticleId(articleId);
        List<String> tagsNameList = new ArrayList<>();
        for (ArticleTags articleTags : articleTagsList) {
            Tags tags = tagsService.getById(articleTags.getTagsId());
            String name = tags.getName();
            tagsNameList.add(name);
        }
        return tagsNameList;
    }
    private List<Tags> getTags(List<ArticleTags> listArticleTagsByArticleId) {
        List<String> tagsIdList=new ArrayList<>();
        for (ArticleTags articleTags : listArticleTagsByArticleId) {
            tagsIdList.add(articleTags.getTagsId());
        }
        if (tagsIdList.size()==0){
            return null;
        }
        return tagsService.listByIds(tagsIdList);
    }
    private ArticleContentVo copy2Method(Article article) {
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
        BeanUtils.copyProperties(article, articleContentVo);
        //帖子内容
        Body bodyByArticleId = bodyService.getBodyByArticleId(article.getId());
        articleContentVo.setBody(bodyByArticleId);
        return articleContentVo;
    }
    private List<ArticleNoContentVo> copy2Method(List<Article> listArticle) {
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
//            articleNoContentVo.setTagsList(tags);
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
    private List<ArticleVo> copyMethod(List<Article> listArticle) {
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article article : listArticle) {
            //文章
            ArticleVo articleVo = copyMethod(article);
            articleVos.add(articleVo);
        }
        return articleVos;
    }
    private ArticleVo copyMethod(Article article) {
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

    private List<ArticleTags> copyMethod(List<String> tagsIdList,String articleId){
        List<ArticleTags> articleTagsList = new ArrayList<>();
        for (String tagsId : tagsIdList) {
            ArticleTags articleTags = new ArticleTags();
            articleTags.setArticleId(articleId);
            articleTags.setTagsId(tagsId);
            articleTagsList.add(articleTags);
        }
        return articleTagsList;
    }

    private boolean deleteByArticleListMethod(List<Article> articleList,TransactionStatus transactionStatus) {
        // 帖子列表
        List<String> articleIdList = new ArrayList<>();
        // 帖子详细
        List<String> bodyIdList =new ArrayList<>();
        // 该帖子的评论
        List<String> commentIdList =new ArrayList<>();
        // 帖子的点赞和收藏的中间表
        List<String> artilceLikesCollectionIdList=new ArrayList<>();
        // 帖子的标签的中间表
        List<String> artilceTagsIdList=new ArrayList<>();
        for (Article article:articleList){
            String articleId = article.getId();
            articleIdList.add(articleId);
            Body body = bodyService.getBodyByArticleId(articleId);
            bodyIdList.add(body.getId());
            List<Comment> commentsByArticleId = commentService.getCommentsByArticleId(articleId);
            for (Comment comment:commentsByArticleId) {
                commentIdList.add(comment.getId());
            }
            List<ArticleLikesCollection> byArticleId = articleLikesCollectionService.getByArticleId(articleId);
            for (ArticleLikesCollection articleLikesCollection:byArticleId) {
                artilceLikesCollectionIdList.add(articleLikesCollection.getId());
            }
            List<ArticleTags> listArticleTagsByArticleId = articleTagsService.getListArticleTagsByArticleId(articleId);
            for (ArticleTags articleTags:listArticleTagsByArticleId) {
                artilceTagsIdList.add(articleTags.getId());
            }
        }
        try {
            boolean b = bodyService.removeBatchByIds(bodyIdList);
            boolean b1 = commentService.removeBatchByIds(commentIdList);
            boolean b2 = articleTagsService.removeBatchByIds(artilceTagsIdList);
            boolean b3 = articleLikesCollectionService.removeBatchByIds(artilceLikesCollectionIdList);
            if (!(b && b1 && b2 && b3)) {
                return false;
            }
            articleMapper.deleteBatchIds(articleIdList);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
