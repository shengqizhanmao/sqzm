package com.lin.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.SMenuMapper;
import com.lin.common.pojo.SMenu;
import com.lin.common.pojo.Vo.SUserTokenVo;
import com.lin.common.service.SMenuService;
import com.lin.common.service.SUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Service
public class SMenuServiceImpl extends ServiceImpl<SMenuMapper, SMenu> implements SMenuService {
    @Autowired
    SMenuMapper sMenuMapper;
    @Autowired
    SUserService sUserService;
    public Result getSMenuBySUserId(String token){
        SUserTokenVo sUserByToken = sUserService.findSUserByToken(token);
        String userId = sUserByToken.getId();
        List<SMenu> listSMenuBySUserId = sMenuMapper.getListSMenuBySUserId(userId);
        return Result.succ("查询菜单成功",listSMenuBySUserId);
    }
}
