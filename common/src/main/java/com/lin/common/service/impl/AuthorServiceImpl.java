package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.AuthorMapper;
import com.lin.common.pojo.Author;
import com.lin.common.pojo.User;
import com.lin.common.pojo.Vo.AuthorVo;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.service.AuthorService;
import com.lin.common.service.UserService;
import com.lin.common.utils.PagesHashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
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
        if (author == null) {
            return Result.succ("不是作者", false);
        }
        if (author.getStatus().equals("-1")) {
            return Result.succ("申请作者已被拒绝,请重新申请", false);
        }
        if (author.getStatus().equals("0")) {
            return Result.succ("申请作者中", false);
        }
        return Result.succ("是作者", true);
    }

    @Override
    public Result apply(String token) {
        UserTokenVo userByToken = userService.findUserByToken(token);
        Author author = getMethod(userByToken.getId());
        if (author == null) {
            int i = applyMethod(userByToken.getId());
            if (i == 0) {
                return Result.fail("申请失败,出现问题");
            }
            return Result.succ("申请成功,请等待审核");
        }
        if (author.getStatus().equals("-1")) {
            int i = updateMethod(author, "0");
            if (i == 0) {
                return Result.fail("申请失败,出现问题");
            }
            return Result.succ("申请成功,请等待审核");
        }
        if (author.getStatus().equals("0")) {
            return Result.succ("已经申请中,请等待审核");
        }
        return Result.succ("已经是作者了,不用申请");
    }

    @Override
    public Result add(String userId, String status) {
        Author author = getMethod(userId);
        if (author != null) {
            return Result.fail("添加失败,已存在");
        }
        Author author1 = new Author();
        author1.setStatus(status);
        author1.setUserId(userId);
        int insert = authorMapper.insert(author1);
        if (insert != 1) {
            return Result.fail(500, "添加失败,出现错误");
        }
        return Result.succ("添加成功");
    }
    //

    @Override
    public Result getAuthorVoList(String status, Long size, Long page) {
        //发页
        LambdaQueryWrapper<Author> authorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        authorLambdaQueryWrapper.eq(Author::getStatus, status);
        Page<Author> authorPage = authorMapper.selectPage(page, size, authorLambdaQueryWrapper);
        long total = authorPage.getTotal();
        List<Author> authors = authorPage.getRecords();
        //vo
        List<AuthorVo> authorVos = copy(authors);
        Map<String, Object> authorListMap = PagesHashMap.getPagesHashMap(total, "authorList", authorVos);
        return Result.succ("查询作者成功", authorListMap);
    }

    @Override
    public boolean deleteByUserId(String id, TransactionStatus transaction) {
        try{
            LambdaQueryWrapper<Author> authorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            authorLambdaQueryWrapper.eq(Author::getUserId,id);
            authorMapper.delete(authorLambdaQueryWrapper);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public List<AuthorVo> copy(List<Author> authors) {
        List<AuthorVo> authorVos = new ArrayList<>();
        for (Author author : authors) {
            AuthorVo authorVo = new AuthorVo();
            authorVo.setId(author.getId());
            authorVo.setStatus(author.getStatus());
            authorVo.setUserId(author.getUserId());
            //查询用户
            User byId = userService.getById(author.getUserId());
            authorVo.setEmail(byId.getEmail());
            authorVo.setNickname(byId.getNickname());
            authorVo.setUsername(byId.getUsername());
            authorVos.add(authorVo);
        }
        return authorVos;
    }

    public int applyMethod(String userId) {
        Author author = new Author();
        author.setStatus("0");
        author.setUserId(userId);
        return authorMapper.insert(author);
    }

    public Author getMethod(String userId) {
        LambdaQueryWrapper<Author> authorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        authorLambdaQueryWrapper.eq(Author::getUserId, userId);
        return authorMapper.selectOne(authorLambdaQueryWrapper);
    }

    public Author getMethod(String userId, String Status) {
        LambdaQueryWrapper<Author> authorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        authorLambdaQueryWrapper.eq(Author::getUserId, userId);
        authorLambdaQueryWrapper.eq(Author::getStatus, Status);
        return authorMapper.selectOne(authorLambdaQueryWrapper);
    }

    public int updateMethod(Author author, String Status) {
        author.setStatus(Status);
        return authorMapper.updateById(author);
    }
}
