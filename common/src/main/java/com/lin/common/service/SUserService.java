package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.SUser;
import com.lin.common.pojo.Vo.SUserTokenVo;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface SUserService extends IService<SUser> {
    @Nullable
    SUserTokenVo findSUserByToken(String token);

    SUser getSUserByUsername(String username);

    Result get(Long size, Long page);

    Result getSUserRoleBySUserId(String sUserSId);

    Result getSUserSMenuBySUserId(String sUserSId);

    Result addSUser(SUser sUser);

    Result addSUserRole(List<String> listRoleId, String sId);

    Result addSUserSMenu(String sId, List<String> sMenuSIdList);

    Result updateSUser(SUser sUser);

    Result updateEnableFlag(String sId, String enableFlag);

    Result deleteSUser(String sId);

    Result deleteSUserRole(String sUserSId, String roleId);

    Result deleteSUserSMenu(String sUserSId, String sMenuId);


}
