package com.lin.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.RedisStatus;
import com.lin.common.Result;
import com.lin.common.ResultCode;
import com.lin.common.mapper.UserMapper;
import com.lin.common.pojo.User;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.pojo.Vo.UserVo;
import com.lin.common.service.UserService;
import com.lin.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource(name = "minioUtils")
    private MinioUtils minioUtils;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private SendEmailUtil emailUtil;

    @NotNull
    public Result Login(String username, String password, String code, String codeDate) {
        if (StringUtils.isEmpty(username)) {
            return Result.fail(401, "登录失败: 用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            return Result.fail(401, "登录失败: 密码不能为空");
        }
        if (StringUtils.isEmpty(code)) {
            return Result.fail(401, "登录失败: 验证码不能为空");
        }
        String codeRedis = redisTemplate.opsForValue().get(RedisStatus.USER_CODE + codeDate);
        if (StringUtils.isEmpty(codeRedis)) {
            return Result.fail(401, "登录失败: 验证码已经过期了");
        }
        //获取当前登录对象
        User user = null;
        user = getUserByUsername(username);
        //用户是否存在
        if (user == null) {
            return Result.fail("登录失败,用户不存在");
        }
        code = code.toLowerCase();      //变小写
        codeRedis = codeRedis.toLowerCase(); //变小写
        if (!code.equals(codeRedis)) {
            return Result.fail(401, "验证码错误");
        }
        //进行加密
        String salt = user.getSalt();
        String password2 = Md5Utils.md5Encryption(password, salt);
        //密码是否正确
        if (!password2.equals(user.getPassword())) {
            return Result.fail("登录失败,密码错误");
        }
        if (user.getEnableFlag().equals("-1")) {
            return Result.fail(402, "帐号被禁用");
        }
        String token = JWTUtils.createToken(user.getUsername());
        UserTokenVo userTokenVo = getUserTokenVoByUser(user);
        redisTemplate.opsForValue().set(RedisStatus.TOKEN_USER + token, JSON.toJSONString(userTokenVo), 1, TimeUnit.DAYS);
        return Result.succ("登录成功", token);
    }

    @Override
    public Result listPage(Long size, Long page) {
        //分页
        if (size <= 0L) {
            return Result.fail("参数错误,个数不能小于0");
        }
        if (page <= 0L) {
            return Result.fail("参数错误,页数不能小于0");
        }
        Page<User> userPage = userMapper.selectPage(page, size);
        long total = userPage.getTotal();
        List<User> list = userPage.getRecords();
        //Vo
        List<UserVo> list1 = new ArrayList<>();
        for (User user : list) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            list1.add(userVo);
        }
        //map
        Map<String, Object> userListHashMap = PagesHashMap.getPagesHashMap(total, "userList", list1);
        return Result.succ("查询用户成功", userListHashMap);
    }

    @NotNull
    public Result getEmailCode(String email) {
        if (StringUtils.isBlank(email)) {
            return Result.fail(401, "邮箱不能为空");
        }
        if (!email.matches("^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$")) {
            return Result.fail(401, "邮箱格式错误");
        }
        String code = (String) redisTemplate.opsForValue().get(RedisStatus.USER_EMAIL_CODE + email);
        if (!StringUtils.isBlank(code)) {
            log.info("code" + code);
            return Result.fail(401, "请等待60秒");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        if (user == null) {
            return Result.fail(401, "邮箱不存在");
        }
        String checkCode = String.valueOf(new Random().nextInt(999999));
        String title = "SQZM游戏论坛登录验证码";
        String content = "您正在登录SQZM游戏论坛，本次验证码为:" + checkCode + ",有效期1分钟,如非本人操作，请忽略！谢谢！60秒才能发下次验证码";
        emailUtil.sendMessage(user.getEmail(), title, content);
        redisTemplate.opsForValue().set(RedisStatus.USER_EMAIL_CODE + email, checkCode, 1, TimeUnit.MINUTES);
        return Result.succ("发送邮箱成功");
    }

    public Result getUpdateEmailCode(String email) {
        if (StringUtils.isBlank(email)) {
            return Result.fail(401, "邮箱不能为空");
        }
        if (!email.matches("^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$")) {
            return Result.fail(401, "邮箱格式错误");
        }
        String code = (String) redisTemplate.opsForValue().get(RedisStatus.USER_UPDATE_EMAIL_CODE + email);
        if (!StringUtils.isBlank(code)) {
            log.info("code" + code);
            return Result.fail(401, "请等待60秒");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        if (user != null) {
            return Result.fail(401, "邮箱已经存在");
        }
        String checkCode = String.valueOf(new Random().nextInt(999999));
        String title = "SQZM游戏论坛修改邮箱验证码";
        String content = "您正在修改SQZM游戏论坛的邮箱，本次验证码为:" + checkCode + ",有效期1分钟,如非本人操作，请忽略！谢谢！60秒才能发下次验证码";
        emailUtil.sendMessage(email, title, content);
        redisTemplate.opsForValue().set(RedisStatus.USER_UPDATE_EMAIL_CODE + email, checkCode, 1, TimeUnit.MINUTES);
        return Result.succ("发送邮箱成功");
    }

    @NotNull
    public Result LoginEmail(String email, String code) {
        if (StringUtils.isBlank(email)) {
            return Result.fail(401, "邮箱不能为空");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        if (user == null) {
            return Result.fail(401, "邮箱不存在");
        }
        if (StringUtils.isBlank(code)) {
            return Result.fail(401, "验证码不能为空");
        }
        String EmailCode = (String) redisTemplate.opsForValue().get(RedisStatus.USER_EMAIL_CODE + email);
        if (StringUtils.isBlank(EmailCode)) {
            return Result.fail(401, "验证码已过期，请重新发送验证码");
        }
        if (!EmailCode.equals(code)) {
            return Result.fail(401, "验证码错误，请重新输入");
        }
        String token = JWTUtils.createToken(user.getId());
        redisTemplate.opsForValue().set(RedisStatus.TOKEN_USER + token, JSON.toJSONString(user), 30, TimeUnit.DAYS);
        return Result.succ("登录成功", token);
    }

    @Nullable
    @Override
    public UserTokenVo findUserByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        //获取redis是否存在
        String userJson = (String) redisTemplate.opsForValue().get(RedisStatus.TOKEN_USER + token);
        if (StringUtils.isBlank(userJson)) {
            return null;
        }
        //解析token
        try {
            Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
            if (stringObjectMap.size() == 0) {
                return null;
            }
        } catch (Exception e) {
            log.error("UserService的findUserByToken出现错误,问题为:" + e);
            return null;
        }
        UserTokenVo user = JSON.parseObject(userJson, UserTokenVo.class);
        return user;
    }

    @Nullable
    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        try {
            User user = userMapper.selectOne(userLambdaQueryWrapper);
            return user;
        } catch (Exception e) {
            log.error("登录根据用户名获取用户信息失败,原因是:" + e);
            return null;
        }
    }

    @Override
    public Result getNewCookie(String token) {
        if (StringUtils.isBlank(token)) {
            return Result.fail(401, "出现错误,token为空了");
        }
        UserTokenVo userByToken = findUserByToken(token);
        if (userByToken == null) {
            log.error("服务器出现错误,错误在userServicelmpl.getNewCookie");
            return Result.fail(500, "服务器出现错误");
        }
        String id = userByToken.getId();
        User user = userMapper.selectById(id);
        if (user == null) {
            log.error("服务器出现错误,错误在userServicelmpl.getNewCookie");
            return Result.fail(500, "服务器出现错误");
        }
        redisTemplate.delete(RedisStatus.TOKEN_USER + token);
        UserTokenVo userTokenVo = getUserTokenVoByUser(user);
        redisTemplate.opsForValue().set(RedisStatus.TOKEN_USER + token, JSON.toJSONString(userTokenVo), 1, TimeUnit.DAYS);
        return Result.succ("获取修改后用户信息成功", userTokenVo);
    }

    private UserTokenVo getUserTokenVoByUser(User user) {
        UserTokenVo userTokenVo = new UserTokenVo();
        userTokenVo.setId(user.getId());
        userTokenVo.setUserName(user.getUsername());
        userTokenVo.setAvatar(user.getAvatar());
        userTokenVo.setEmail(user.getEmail());
        userTokenVo.setNickName(user.getNickname());
        userTokenVo.setGender(user.getGender());
        userTokenVo.setEnableFlag(user.getEnableFlag());
        return userTokenVo;
    }

    @NotNull
    @Override
    public Result addUser(User user) {
        String password = user.getPassword();
        String username = user.getUsername();
        if (StringUtils.isEmpty(username)) {
            return Result.fail("登陆名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            password = "123456";
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        try {
            User user1 = userMapper.selectOne(lambdaQueryWrapper);
            if (user1 != null) {
                return Result.fail("添加失败,用户已存在");
            }
        } catch (Exception e) {
            log.error("普通用户添加失败,原因是" + e);
            return Result.fail("添加失败");
        }
        String salt = Md5Utils.CretaeMd5();
        String password2 = Md5Utils.md5Encryption(password, salt);
        user.setCreateDate(new Date());
        user.setEnableFlag("1");
        user.setSalt(salt);
        user.setPassword(password2);
        try {
            int insert = userMapper.insert(user);
            if (insert == 0) {
                return Result.fail("添加用户失败");
            }
            return Result.succ("添加用户成功");
        } catch (Exception e) {
            log.error("普通用户添加失败,原因是" + e);
            return Result.fail("添加失败");
        }
    }

    @NotNull
    @Override
    public Result updateUser(@NotNull User user) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId, user.getId());
        System.out.println(user);
        if (!StringUtils.isBlank(user.getNickname())) {
            lambdaUpdateWrapper.set(User::getNickname, user.getNickname());
        }
        if (!StringUtils.isBlank(user.getEmail())) {
            lambdaUpdateWrapper.set(User::getEmail, user.getEmail());
        }
        if (!StringUtils.isBlank(user.getGender())) {
            lambdaUpdateWrapper.set(User::getGender, user.getGender());
            System.out.println("修改了性别");
        }
        if (!StringUtils.isBlank(user.getPassword())) {
            String salt = Md5Utils.CretaeMd5();
            String password2 = Md5Utils.md5Encryption(user.getPassword(), salt);
            lambdaUpdateWrapper.set(User::getSalt, salt).set(User::getPassword, password2);
        }
        try {
            int update = userMapper.update(user, lambdaUpdateWrapper);
            if (update == 0) {
                Result.fail("用户修改失败");
            }
            return Result.succ("用户修改成功");
        } catch (Exception e) {
            log.error("用户修改失败,原因是" + e);
            return Result.fail("用户修改失败");
        }
    }

    @Override
    public Result updateEmail(String id, String email, String code) {
        if (StringUtils.isBlank(id)) {
            return Result.fail(401, "出现错误,id为空了");
        }
        if (StringUtils.isBlank(email)) {
            return Result.fail(401, "邮箱不能为空");
        }
        if (StringUtils.isBlank(code)) {
            return Result.fail(401, "验证码不能为空");
        }
        String EmailCode = (String) redisTemplate.opsForValue().get(RedisStatus.USER_UPDATE_EMAIL_CODE + email);
        if (StringUtils.isBlank(EmailCode)) {
            return Result.fail(401, "验证码已过期，请重新发送验证码");
        }
        if (!EmailCode.equals(code)) {
            return Result.fail(401, "验证码错误，请重新输入");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.fail("修改邮箱失败,用户不存在");
        }
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper.eq(User::getId, id).set(User::getEmail, email);
        user.setEmail(email);
        try {
            int update = userMapper.update(user, userLambdaUpdateWrapper);
            if (update == 0) {
                return Result.fail("修改邮箱失败");
            }
            return Result.succ("修改邮箱成功");
        } catch (Exception e) {
            log.error("修改邮箱失败,原因是" + e);
            return Result.fail(500, "修改邮箱失败");
        }
    }

    //修改用户头像
    public Result upAvatar(@NotNull MultipartFile file, String id) {
        //获取上传文件的文件名
        String fileName = file.getOriginalFilename();
        // 为了避免文件名重复，使用UUID重命名文件，将横杠去掉
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newFileName = uuid + fileName;
        minioUtils.MinioUtilsUpdate("user");
        try {
            String s = minioUtils.putObject(file.getInputStream(), newFileName, file.getContentType());
            // 返回文件名
            String fileName2 = s + "/" + newFileName;
            User user = userMapper.selectById(id);
            String avatar = user.getAvatar();
            user.setAvatar(fileName2);
            //进行修改头像
            try {
                userMapper.updateById(user);
                minioUtils.removeObject(avatar.split("/")[avatar.split("/").length - 1]);
            } catch (Exception e) {
                System.out.println("修改头像错误:" + e);
            }
            return Result.succ("修改头像成功");
        } catch (Exception e) {
            log.error("上传图片失败，原因是" + e);
            return Result.fail(ResultCode.IMAGE_UPLOAD_FAIL, "上传图片失败");
        } finally {
            minioUtils.MinioUtilsUpdateDefault();
        }
    }

    @Override
    public Result updateEnableFlag(String id, String enableFlag) {
        User user = userMapper.selectById(id);
        user.setEnableFlag(enableFlag);
        int i = userMapper.updateById(user);
        if (i == 0) {
            if (enableFlag.equals("1")) {
                return Result.fail("启动用户失败");
            }
            return Result.fail("禁用用户失败");
        }
        if (enableFlag.equals("1")) {
            return Result.succ("启动用户成功");
        }
        return Result.succ("禁用用户成功");
    }

    @Override
    public Result updatePassword(String id, String password, String newPassword) {
        if (StringUtils.isBlank(id)) {
            return Result.fail(401, "出现错误,id为空了");
        }
        if (StringUtils.isBlank(password)) {
            return Result.fail(401, "密码不能为空");
        }
        if (StringUtils.isBlank(newPassword)) {
            return Result.fail(401, "新密码不能为空");
        }
        if (password.equals(newPassword)) {
            return Result.fail(401, "新密码不能与旧密码相同");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.fail("修改密码失败,用户不存在");
        }
        String salt = user.getSalt();
        String oldPassword = Md5Utils.md5Encryption(password, salt);
        if (!oldPassword.equals(user.getPassword())) {
            return Result.fail("修改密码失败,旧密码不对");
        }
        String newSalt = Md5Utils.CretaeMd5();
        String newPassword2 = Md5Utils.md5Encryption(newPassword, newSalt);
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper.eq(User::getId, id).set(User::getPassword, newPassword2).set(User::getSalt, newSalt);
        try {
            int update = userMapper.update(user, userLambdaUpdateWrapper);
            if (update == 0) {
                return Result.fail(500, "修改密码失败");
            }
            return Result.succ("修改密码成功");
        } catch (Exception e) {
            log.error("修改密码失败,原因是:" + e);
            return Result.fail(500, "修改密码失败");
        }
    }


    @NotNull
    @Override
    public Result deleteUser(String id) {
        if (StringUtils.isBlank(id)) {
            return Result.fail("id参数不能为空");
        }
        try {
            User user1 = userMapper.selectById(id);
            if (user1 == null) {
                return Result.fail("用户删除失败,用户不存在");
            }
        } catch (Exception e) {
            log.error("用户删除失败,原因是" + e);
            return Result.fail("用户删除失败");
        }
        try {
            int i = userMapper.deleteById(id);
            if (i == 0) {
                return Result.fail("删除失败");
            }
            return Result.succ("删除成功");
        } catch (Exception e) {
            log.error("用户删除失败,原因是" + e);
            return Result.fail("用户删除失败");
        }
    }

    @Override
    public List<User> getListUserByUsernameOrNickname(String usernameOrNickname) {
        return userMapper.selectListUserByUsernameOrNickname(usernameOrNickname);
    }
}
