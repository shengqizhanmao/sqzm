package com.lin.sqzmHtgl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.common.Result;
import com.lin.common.pojo.Announcement;
import com.lin.common.service.AnnouncementService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-25
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Resource
    private TransactionDefinition transactionDefinition;


    @RequiresPermissions("announcement:add")
    @GetMapping("/get/{palteId}")
    public Result getByPalteId(@PathVariable("palteId") String palteId) {
        return announcementService.listAnnouncementVo(palteId);
    }

    @RequiresPermissions("announcement:add")
    @PostMapping("/add")
    public Result add(@RequestBody Announcement announcement) {
        return announcementService.saveAnnouncement(announcement);
    }

    @RequiresPermissions("announcement:update")
    @PutMapping("/update")
    public Result update(@RequestBody Announcement announcement) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        boolean b = announcementService.updateById(announcement);
        if (b) {
            LambdaQueryWrapper<Announcement> announcementLambdaQueryWrapper = new LambdaQueryWrapper<>();
            announcementLambdaQueryWrapper.eq(Announcement::getPalteId, announcement.getPalteId())
                    .eq(Announcement::getModularsId, announcement.getModularsId());
            List<Announcement> list = announcementService.list(announcementLambdaQueryWrapper);
            int size = list.size();
            if (size != 1) {
                dataSourceTransactionManager.rollback(transactionStatus);//回滚事务
                return Result.fail("修改失败,模块已有公告");
            }
            dataSourceTransactionManager.commit(transactionStatus);//提交事务
            return Result.succ("修改成功");
        }
        return Result.fail("修改失败");
    }

    @RequiresPermissions("announcement:delete")
    @DeleteMapping("/delete/{announcementId}")
    public Result delete(@PathVariable("announcementId") String announcementId) {
        boolean b = announcementService.removeById(announcementId);
        if (b) {
            return Result.succ("删除成功");
        }
        return Result.fail("删除失败");
    }


}
