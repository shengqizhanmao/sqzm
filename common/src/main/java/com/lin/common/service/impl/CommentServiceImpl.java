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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Override
    public Result getCommentsByArticleId(String articleId) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        lambdaQueryWrapper.eq(Comment::getLevel, 1);
        lambdaQueryWrapper.orderByAsc(Comment::getLayer);
        List<Comment> comments = commentMapper.selectList(lambdaQueryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        if (commentVoList == null) {
            return Result.fail("查询评论失败");
        }
        return Result.succ("查询评论成功", commentVoList);
    }

    @Override
    public Result create(String articleId, String content, String toUserId, String parentId, String token) {
        if (content.equals("")) {
            return Result.succ(false, 200, "评论不能为空", null);
        }
        UserTokenVo user = userService.findUserByToken(token);
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
        if (b == false) {
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


    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVo copy = copy(comment);
            commentVoList.add(copy);
            if (copy.getChildrens().size() != 0 && copy.getLevel() == 2L) {
                List<CommentVo> childrens = copy.getChildrens();
                for (CommentVo commentVo : childrens) {
                    commentVoList.add(commentVo);
                }
            }
        }
        return commentVoList;
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

    private List<CommentVo> findCommentsByParentId(String id) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Comment::getParentId, id);
        lambdaQueryWrapper.eq(Comment::getLevel, 2L);
        lambdaQueryWrapper.orderByAsc(Comment::getLayer);
        List<CommentVo> commentVos = copyList(commentMapper.selectList(lambdaQueryWrapper));
        return commentVos;
    }
}
