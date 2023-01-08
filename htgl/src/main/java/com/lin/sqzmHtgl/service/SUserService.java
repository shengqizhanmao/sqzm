package com.lin.sqzmHtgl.service;

import com.lin.sqzmHtgl.common.Result;
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
    Result addSUser(SUser sUser);
    SUserTokenVo findUserByToken(String token);
}
