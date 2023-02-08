package com.lin.sqzmYxlt.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.lin.common.RedisStatus;
import com.lin.common.Result;
import com.lin.sqzmYxlt.mapper.UserMapper;
import com.lin.sqzmYxlt.pojo.User;
import com.lin.sqzmYxlt.pojo.Vo.UserTokenVo;
import com.lin.sqzmYxlt.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.utils.JWTUtils;
import com.lin.common.utils.Md5Utils;
import com.lin.common.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    RedisTemplate<String,String> redisTemplate;

    @Resource
    UserMapper userMapper;
    @Override
    public UserTokenVo findUserByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        //获取redis是否存在
        String userJson = redisTemplate.opsForValue().get(RedisStatus.TOKEN + token);
        if (StringUtils.isBlank(userJson)) {
            return null;
        }
        //解析token
        try{
            Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
            if (stringObjectMap.size() == 0) {
                return null;
            }
        }catch (Exception e){
            return null;
        }
        UserTokenVo user = JSON.parseObject(userJson, UserTokenVo.class);
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername,username);
        try{
            User user = userMapper.selectOne(userLambdaQueryWrapper);
            return user;
        }catch (Exception e){
            log.error("登录根据用户名获取用户信息失败,原因是:"+e);
            return null;
        }
    }

    @Override
    public Result addUser(User user) {
        String username = user.getUsername();
        if(StringUtils.isEmpty(username)){
            return Result.fail("不能缺少登录名");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername,username);
        User user2 = userMapper.selectOne(userLambdaQueryWrapper);
        if (user2!=null){
            return Result.fail(402,"用户已经存在");
        }
        String password = user.getPassword();
        if(StringUtils.isEmpty(password)){
            password="123456";
        }
        String salt = Md5Utils.CretaeMd5();
        String password2 = Md5Utils.md5Encryption(password, salt);
        user.setEnableFlag("1");
        user.setSalt(salt);
        user.setPassword(password2);
        user.setId(SnowFlakeUtil.getId());
        user.setCreateDate(new Date());
        int insert = userMapper.insert(user);
        if (insert==0){
            return Result.fail(401,"添加用户失败");
        }
        return Result.succ("添加成功");
    }
}
