package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.MenuMapper;
import com.lin.common.pojo.Menu;
import com.lin.common.service.MenuService;
import com.lin.common.utils.PagesHashMap;
import org.springframework.stereotype.Service;

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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Resource
    private MenuMapper menuMapper;
    @Override
    public Result getMenuPage(Long page, Long size) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.orderByAsc(Menu::getSort);
        Page<Menu> menuPage = menuMapper.selectPage(page, size, menuLambdaQueryWrapper);
        Map<String, Object> listMenu = PagesHashMap.getPagesHashMap(menuPage, "listMenu");
        return Result.succ("查询用户分页列表成功",listMenu);
    }
}
