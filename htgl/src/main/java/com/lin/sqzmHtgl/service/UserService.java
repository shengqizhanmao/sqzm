package com.lin.sqzmHtgl.service;

import com.lin.common.Result;
import com.lin.sqzmHtgl.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
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
    Result addUser(User user);
    Result updateUser(User user);
    Result deleteUser(String id);
    Result upAvatar(MultipartFile file, String id);
}
