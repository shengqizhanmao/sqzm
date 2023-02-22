package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.common.Result;
import com.lin.common.pojo.Author;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.service.UserService;
import com.lin.common.mapper.AuthorMapper;
import com.lin.common.service.AuthorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-22
 */
@Service
public class AuthorServiceImpl extends ServiceImpl<AuthorMapper, Author> implements AuthorService {


    @Resource
    private UserService userService;
    @Resource
    private AuthorMapper authorMapper;

    @Override
    public Result isAuthor(String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        Author author = getMethod(userByToken.getId());
        if (author==null){
            return Result.succ("不是作者",false);
        }
        if (author.getStatus().equals("-1")){
            return Result.succ("申请作者已被拒绝,请重新申请",false);
        }
        if (author.getStatus().equals("0")){
            return Result.succ("申请作者中",false);
        }
        return Result.succ("是作者",true);
    }

    @Override
    public Result apply(String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        Author author = getMethod(userByToken.getId());
        if (author==null){
            int i = applyMethod(userByToken.getId());
            if (i==0){
                return Result.fail("申请失败,出现问题");
            }
            return Result.succ("申请成功,请等待审核");
        }
        if(author.getStatus().equals("-1")){
            int i = updateMethod(author, "0");
            if (i==0){
                return Result.fail(401,"申请失败,出现问题");
            }
            return Result.succ("申请成功,请等待审核");
        }
        if (author.getStatus().equals("0")){
            return Result.succ("已经申请中,请等待审核");
        }
        return Result.succ("已经是作者了,不用申请");
    }

    @Override
    public Result add(String userId,String status) {
        Author author = getMethod(userId);
        if (author==null){
            return Result.succ("审核失败,该用户未申请",false);
        }
        if (author.getStatus().equals("0")){
            if (status.equals("1")){
                updateMethod(author,status);
                return Result.succ("审核成功,该用户成为作者",true);
            }
            if (status.equals("-1")){
                updateMethod(author,status);
                return Result.succ("审核成功,该用户被审核为拒绝",true);
            }
            return Result.fail("审核失败,参数不能为0");
        }
        if(author.getStatus().equals("1")){
            return Result.fail("审核失败,该用户已成为作者",false);
        }
        return Result.fail("审核失败,该用户已被审核为拒绝",false);
    }

    public int applyMethod(String userId){
        Author author = new Author();
        author.setStatus("0");
        author.setUserId(userId);
        return authorMapper.insert(author);
    }

    public Author getMethod(String userId){
        LambdaQueryWrapper<Author> authorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        authorLambdaQueryWrapper.eq(Author::getUserId,userId);
        return authorMapper.selectOne(authorLambdaQueryWrapper);
    }
    public Author getMethod(String userId,String Status){
        LambdaQueryWrapper<Author> authorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        authorLambdaQueryWrapper.eq(Author::getUserId,userId);
        authorLambdaQueryWrapper.eq(Author::getStatus,Status);
        return authorMapper.selectOne(authorLambdaQueryWrapper);
    }
    public int updateMethod(Author author,String Status){
        author.setStatus(Status);
        return authorMapper.updateById(author);
    }
}
