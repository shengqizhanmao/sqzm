package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.SMenuMapper;
import com.lin.common.mapper.SUserMenuMapper;
import com.lin.common.pojo.SMenu;
import com.lin.common.pojo.SUserMenu;
import com.lin.common.pojo.Vo.SUserTokenVo;
import com.lin.common.service.SMenuService;
import com.lin.common.service.SUserService;
import com.lin.common.utils.PagesHashMap;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Service
public class SMenuServiceImpl extends ServiceImpl<SMenuMapper, SMenu> implements SMenuService {
    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;
    @Resource
    private SMenuMapper sMenuMapper;

    @Resource
    private SUserMenuMapper sUserMenuMapper;

    @Resource
    private SUserService sUserService;

    @Override
    public Result getSMenuBySUserId(String token) {
        SUserTokenVo sUserByToken = sUserService.findSUserByToken(token);
        if (sUserByToken == null) {
            return Result.fail(500, "出现错误");
        }
        String userId = sUserByToken.getId();
        List<SMenu> listSMenuBySUserId = sMenuMapper.getListSMenuBySUserId(userId);
        return Result.succ("查询菜单成功", listSMenuBySUserId);
    }

    public Result getSMenuBySUserId2(String SId) {
        List<SMenu> listSMenuBySUserId = sMenuMapper.getListSMenuBySUserId(SId);
        return Result.succ("查询菜单成功", listSMenuBySUserId);
    }

    @Override
    public Result getSMenu(Long size, Long page) {
        if (size <= 0L) {
            return Result.fail("参数错误,个数不能为0");
        }
        if (page <= 0L) {
            return Result.fail("参数错误,页数不能为0");
        }

        LambdaQueryWrapper<SMenu> sMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sMenuLambdaQueryWrapper.orderByAsc(SMenu::getSort);
        Page<SMenu> sMenuPage = sMenuMapper.selectPage(page, size, sMenuLambdaQueryWrapper);
        Map<String, Object> sMenusListMap = PagesHashMap.getPagesHashMap(sMenuPage, "sMenuList");
        return Result.succ("获取系统菜单", sMenusListMap);
    }

    @Override
    public Result addSUserAndSMenu(String sId, List<String> sMenuSIdList) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            LambdaQueryWrapper<SUserMenu> sUserMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sUserMenuLambdaQueryWrapper.eq(SUserMenu::getUserId, sId);
            sUserMenuMapper.delete(sUserMenuLambdaQueryWrapper);
            for (String s : sMenuSIdList) {
                SUserMenu sUserSMenu = new SUserMenu();
                sUserSMenu.setUserId(sId);
                sUserSMenu.setMenuId(s);
                sUserMenuMapper.insert(sUserSMenu);
            }
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            log.error("SMenuServiceImpl的addSUserAndSMenu方法出现错误:" + e);
            return Result.fail("失败原因:" + e);
        }
        dataSourceTransactionManager.commit(transactionStatus);
        return Result.succ("添加成功");
    }

    @Override
    public Result remove(String sId) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        //先删除系统用户拥有的系统目录
        LambdaUpdateWrapper<SUserMenu> sUserMenuLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sUserMenuLambdaUpdateWrapper.eq(SUserMenu::getMenuId, sId);
        sUserMenuMapper.delete(sUserMenuLambdaUpdateWrapper);
        //删除系统目录
        int i = sMenuMapper.deleteById(sId);
        if (i == 0) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return Result.fail("删除失败,用户不存在");
        }
        dataSourceTransactionManager.commit(transactionStatus);
        return Result.succ("删除成功");
    }

    @Override
    public Result deleteSUserMenu(String sUserSId, String sMenuId) {
        try {
            LambdaQueryWrapper<SUserMenu> sUserMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sUserMenuLambdaQueryWrapper.eq(SUserMenu::getUserId, sUserSId).eq(SUserMenu::getMenuId, sMenuId);
            sUserMenuMapper.delete(sUserMenuLambdaQueryWrapper);
        } catch (Exception e) {
            log.error("SMenuServiceImpl的deleteSUserMenu方法出现错误:" + e);
            return Result.fail("失败原因:" + e);
        }
        return Result.succ("删除成功");
    }

    @Override
    public Result deleteSUserMenu(String sUserSId) {
        try {
            LambdaQueryWrapper<SUserMenu> sUserMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sUserMenuLambdaQueryWrapper.eq(SUserMenu::getUserId, sUserSId);
            sUserMenuMapper.delete(sUserMenuLambdaQueryWrapper);
        } catch (Exception e) {
            log.error("SMenuServiceImpl的deleteSUserMenu方法2出现错误:" + e);
            return Result.fail("失败原因:" + e);
        }
        return Result.succ("删除成功");
    }
}
