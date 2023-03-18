package com.lin.sqzmHtgl.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lin.common.Result;
import com.lin.common.pojo.ArticleTags;
import com.lin.common.pojo.Tags;
import com.lin.common.service.ArticleTagsService;
import com.lin.common.service.TagsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @apiNote 标签
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/tags")
public class TagsController {
    @Resource
    private TagsService tagsService;

    @Resource
    private ArticleTagsService articleTagsService;

    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Resource
    private TransactionDefinition transactionDefinition;

    @RequiresPermissions("tags:get")
    @GetMapping("/get/{palteId}")
    public Result getByPalteId(@PathVariable("palteId") String palteId) {
        return tagsService.getByPalteId(palteId);
    }

    @RequiresPermissions("tags:add")
    @PostMapping("/add")
    public Result add(@RequestBody Tags tags) {
        boolean save = tagsService.save(tags);
        if (save) {
            return Result.succ("添加成功");
        }
        return Result.fail("添加失败");
    }

    @RequiresPermissions("tags:update")
    @PutMapping("/update")
    public Result update(@RequestBody Tags tags) {
        boolean b = tagsService.updateById(tags);
        if (b) {
            return Result.succ("修改成功");
        }
        return Result.fail("修改失败");
    }

    @RequiresPermissions("tags:delete")
    @DeleteMapping("/delete/{tagsId}")
    public Result delete(@PathVariable("tagsId") String tagsId) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            if (StringUtils.isEmpty(tagsId)) {
                dataSourceTransactionManager.rollback(transactionStatus);//回滚事务
                return Result.fail("参数不能为空");
            }
            //删除标签
            boolean b = tagsService.removeById(tagsId);
            //删除文章中的标签
            LambdaUpdateWrapper<ArticleTags> articleTagsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            articleTagsLambdaUpdateWrapper.eq(ArticleTags::getTagsId, tagsId);
            articleTagsService.remove(articleTagsLambdaUpdateWrapper);
            if (b) {
                dataSourceTransactionManager.commit(transactionStatus);
                return Result.succ("删除成功");
            }
            dataSourceTransactionManager.rollback(transactionStatus);//回滚事务
            return Result.fail("删除失败");
        } catch (TransactionException e) {
            dataSourceTransactionManager.rollback(transactionStatus);//回滚事务
            return Result.fail(500, "出现错误");
        }

    }


}
