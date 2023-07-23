package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.CommentMapper;
import com.lin.common.pojo.Article;
import com.lin.common.pojo.Comment;
import com.lin.common.pojo.User;
import com.lin.common.pojo.Vo.CommentVo;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.pojo.Vo.UserVo;
import com.lin.common.service.ArticleService;
import com.lin.common.service.CommentService;
import com.lin.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Override
    public Result getCommentsVoByArticleId(String articleId) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        lambdaQueryWrapper.eq(Comment::getLevel, 1);
        lambdaQueryWrapper.orderByAsc(Comment::getLayer);
        List<Comment> comments = commentMapper.selectList(lambdaQueryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.succ("查询评论成功", commentVoList);
    }

    @Override
    public List<Comment> getCommentsByArticleId(String articleId) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        return commentMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<Comment> getCommentsByUserId(String UserId) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getAuthorId, UserId);
        return commentMapper.selectList(lambdaQueryWrapper);
    }

    /*
    *@Param String articleId； 评论的帖子id
    *@Param String content； 评论的内容
    *@Param String toUserId；是否是回复，不为0，则为该评论的用户id
    *@Param String parentId； 是否为回复，不为0，则为该评论id
    *@Param String token；是否登录，登录才能评论回复
    * */
    @Override
    public Result create(String articleId, String content, String toUserId, String parentId, String token) {
        if (content.equals("")) {
            return Result.fail("评论不能为空");
        }
        UserTokenVo user = userService.findUserByToken(token);
        if (user==null){
            return Result.fail("请重新登录");
        }
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setAuthorId(user.getId());
        comment.setContent(content);
        comment.setCreateDate(new Date(System.currentTimeMillis()));
        comment.setParentId(parentId);
        comment.setLikesCounts(0L);
        //parentId为0,或者为null或者为空
        if (parentId.equals("0")) {
            comment.setLevel(1L);
        } else {
            comment.setLevel(2L);
        }
        //
        if (toUserId.equals("0") || toUserId.equals("") || StringUtils.isBlank(toUserId)) {
            comment.setToUid("0");
        } else {
            comment.setToUid(toUserId);
        }
        //获取所在层数,修改层数+1
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        commentLambdaQueryWrapper.eq(Comment::getLevel, comment.getLevel());
        commentLambdaQueryWrapper.eq(Comment::getParentId, parentId);
        Long aLong = 0L;
        try {
            aLong = commentMapper.selectCount(commentLambdaQueryWrapper);
        } catch (Exception e) {
            System.out.println(e);
        }
//        Long aLong = commentMapper.selectLayerByLevelByArticleIdByToUid(commentLambdaQueryWrapper);
        if (aLong == null) {
            aLong = 0L;
        }
        comment.setLayer(aLong + 1L);
        int insert = commentMapper.insert(comment);
        //进行评论数量加1
        Article one = articleService.getById(articleId);
        //查询文章评论的数量
        LambdaQueryWrapper<Comment> comment2LambdaQueryWrapper = new LambdaQueryWrapper<>();
        comment2LambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        Long aLong1 = commentMapper.selectCount(comment2LambdaQueryWrapper);
        //评论数量加1
        one.setCommentCounts(aLong1);
        boolean b = articleService.updateById(one);
        if (!b) {
            return Result.fail("修改评论数量失败");
        }
        if (insert == 1) {
            return Result.succ("评论成功");
        } else if (insert == 0) {
            return Result.fail("评论失败");
        } else {
            return Result.fail(406, "评论系统出现错误");
        }
    }

    @Override
    public Result delete(String id,String token){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        UserTokenVo userByToken = userService.findUserByToken(token);
        if (userByToken==null){
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("请重新登录");
        }
        Comment comment = commentMapper.selectById(id);
        String authorId = comment.getAuthorId();
        if (!authorId.equals(userByToken.getId())){
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("删除失败，不是该评论用户");
        }
        //先删除依赖这个评论的评论，也就是回复这个评论的评论都要删除
        try {
            int i = deleteChildrens(id);
            log.info(userByToken.getUserName()+":删除子评论共"+i+"条");
            //最后删除评论
            int i1 = commentMapper.deleteById(id);
            if (i1==0){
                dataSourceTransactionManager.rollback(transactionStatus);
                return Result.fail("删除失败");
            }
            dataSourceTransactionManager.commit(transactionStatus);
            return Result.succ("删除成功",i+i1);
        } catch (Exception e) {
            log.error("删除评论失败，原因是："+e);
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail(500,"删除失败");
        }
    }

    @Override
    public boolean deleteByUserId(String id, TransactionStatus transaction) {
        try {
            List<Comment> commentsByUserId = getCommentsByUserId(id);
            for (Comment comment : commentsByUserId) {
                String commentId = comment.getId();
                deleteChildrens(commentId);
                commentMapper.deleteById(commentId);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //方法区
    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVo commentVo = copy(comment);
            commentVoList.add(commentVo);
            if (commentVo.getChildrens().size() != 0 && commentVo.getLevel() == 2L) {
                List<CommentVo> childrens = commentVo.getChildrens();
                commentVoList.addAll(childrens);
            }
        }
        return commentVoList;
    }
    private List<CommentVo> findCommentsByParentId(String id) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getParentId, id);
        lambdaQueryWrapper.eq(Comment::getLevel, 2L);
        lambdaQueryWrapper.orderByAsc(Comment::getLayer);
        return copyList(commentMapper.selectList(lambdaQueryWrapper));
    }
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        String authorId = comment.getAuthorId();
        //作者
        User user = userService.getById(authorId);
        UserVo userVo = copy(user);
        commentVo.setAuthor(userVo);
        //子评论
        Long level = comment.getLevel();
        if (level == 1L) {
            String id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
            return commentVo;
        }
        String id = comment.getId();
        List<CommentVo> commentVoList = findCommentsByParentId(id);
        commentVo.setChildrens(commentVoList);
        //to User 给谁评论
        String toUid = comment.getToUid();
        User byId = userService.getById(toUid);
        UserVo getUserVoById = copy(byId);
        commentVo.setToUser(getUserVoById);
        return commentVo;
    }
    private UserVo copy(User user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    private int deleteChildrens(String id,int i) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getParentId,id);
        List<Comment> comments = commentMapper.selectList(commentLambdaQueryWrapper);
        if (comments.size()==0){
            return i;
        }
        List<String> listCommentId = new ArrayList<>();
        for (Comment c:comments) {
            listCommentId.add(c.getId());
            i=i+ deleteChildrens(c.getId(), i);
        }
        return i+commentMapper.deleteBatchIds(listCommentId);
    }
    private int deleteChildrens(String id) {
        return deleteChildrens(id,0);
    }
}
