package com.lin.sqzmYxlt.service;

import com.lin.common.Result;
import com.lin.sqzmYxlt.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.sqzmYxlt.pojo.Vo.UserTokenVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
public interface UserService extends IService<User> {

    UserTokenVo findUserByToken(String token);

    User getUserByUsername(String username);

    Result addUser(User user);
}
