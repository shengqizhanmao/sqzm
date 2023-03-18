package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.User;
import com.lin.common.pojo.Vo.UserTokenVo;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface UserService extends IService<User> {
    Result Login(String username, String password, String code, String codeDate);

    Result getEmailCode(String email);

    Result getUpdateEmailCode(String email);

    Result LoginEmail(String email, String code);

    Result listPage(Long size, Long page);

    @Nullable
    UserTokenVo findUserByToken(String token);

    @Nullable
    User getUserByUsername(String username);

    Result addUser(User user);

    Result updateUser(User user);

    Result deleteUser(String id);

    Result upAvatar(MultipartFile file, String id);

    Result updateEmail(String id, String email, String code);

    Result updateEnableFlag(String id, String enableFlag);

    Result updatePassword(String id, String password, String newPassword);

    Result getNewCookie(String token);

    List<User> getListUserByUsernameOrNickname(String usernameOrNickname);


}
