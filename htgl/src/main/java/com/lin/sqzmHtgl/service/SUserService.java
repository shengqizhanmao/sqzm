package com.lin.sqzmHtgl.service;

import com.lin.common.Result;
import com.lin.sqzmHtgl.pojo.SUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.sqzmHtgl.pojo.Vo.SUserTokenVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface SUserService extends IService<SUser> {
    SUserTokenVo findSUserByToken(String token);
    SUser getSUserByUsername(String username);
    Result getSUserAndRole();
    Result addSUser(SUser sUser);
    Result updateSUser(SUser sUser);
    Result updateEnableFlag(SUser sUser);
    Result deleteSUser(String id);

}
