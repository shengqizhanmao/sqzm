package com.lin.common.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.RedisStatus;
import com.lin.common.Result;
import com.lin.common.pojo.User;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface UserService extends IService<User> {
     Result Login(String username,String password,String code,String codeDate);
     Result getEmailCode(String email);
     Result getUpdateEmailCode(String email);
     Result LoginEmail(String email,String code) ;
     @Nullable
     UserTokenVo findUserByToken(String token);
     @Nullable
     User getUserByUsername(String username);
     Result addUser(User user);
     Result updateUser(User user);
     Result deleteUser(String id);
     Result upAvatar(MultipartFile file, String id);

     Result updateEmail(String id,String email, String code);

     Result updatePassword(String id, String password, String newPassword);

     Result getNewCookie(String token);

     List<User> getListUserByUsernameOrNickname(String usernameOrNickname);

}
