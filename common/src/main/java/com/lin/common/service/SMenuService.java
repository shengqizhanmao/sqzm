package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.SMenu;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface SMenuService extends IService<SMenu> {
    Result getSMenuBySUserId(String token);

    Result getSMenuBySUserId2(String SId);

    Result addSUserAndSMenu(String sId, List<String> sMenuSIdList);

    Result deleteSUserMenu(String sUserSId, String sMenuId);

    Result deleteSUserMenu(String sUserSId);

    Result getSMenu(Long size, Long page);

    Result remove(String sId);
}
