package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.SUser;
import com.lin.common.pojo.Vo.SUserTokenVo;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface SUserService extends IService<SUser> {
    @Nullable
    SUserTokenVo findSUserByToken(String token);
    SUser getSUserByUsername(String username);
    Result getSUserAndRole();
    Result addSUser(SUser sUser);
    Result updateSUser(SUser sUser);
    Result updateEnableFlag(SUser sUser);
    Result deleteSUser(String id);

}
