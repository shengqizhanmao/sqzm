package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.User;
import com.lin.common.pojo.Vo.UserTokenVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface UserService extends IService<User> {
    UserTokenVo findUserByToken(String token);
    User getUserByUsername(String username);
    Result addUser(User user);
    Result updateUser(User user);
    Result deleteUser(String id);
    Result upAvatar(MultipartFile file, String id);

}
