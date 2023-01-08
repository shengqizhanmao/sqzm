package com.lin.sqzmHtgl.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.sqzmHtgl.common.Result;
import com.lin.sqzmHtgl.pojo.SUser;
import com.lin.sqzmHtgl.mapper.SUserMapper;
import com.lin.sqzmHtgl.pojo.Vo.SUserTokenVo;
import com.lin.sqzmHtgl.service.SUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.sqzmHtgl.utils.JWTUtils;
import com.lin.sqzmHtgl.utils.Md5Utils;
import com.lin.sqzmHtgl.utils.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Service
public class SUserServiceImpl extends ServiceImpl<SUserMapper, SUser> implements SUserService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    SUserMapper sUserMapper;
    //获取用户,根据Token
    public SUserTokenVo findSUserByToken(String token) {
        /*
         * 1.token合法性校验,是否为空,解析是否成功,redis是否存在
         * 2.如果校验失败,返回错误
         * 3.如果成功,返回对应的结果Login
         * */
        if (StringUtils.isBlank(token)) {
            return null;
        }
        //解析token
        try{
            Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
            if (stringObjectMap == null) {
                return null;
            }
        }catch (Exception e){
            return null;
        }
        //获取redis是否存在
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)) {
            return null;
        }
        SUserTokenVo sUser = JSON.parseObject(userJson, SUserTokenVo.class);
        return sUser;
    }

    @Override
    public SUser getSUserByUsername(String username) {
        LambdaQueryWrapper<SUser> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(SUser::getUsername,username);
        SUser sUser = sUserMapper.selectOne(userLambdaQueryWrapper);
        return sUser;
    }
    //添加用户
    public Result addSUser(SUser sUser){
        String username = sUser.getUsername();
        if(StringUtils.isEmpty(username)){
            return Result.fail("不能缺少登录名");
        }
        LambdaQueryWrapper<SUser> sUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sUserLambdaQueryWrapper.eq(SUser::getUsername,username);
        SUser sUser2 = sUserMapper.selectOne(sUserLambdaQueryWrapper);
        if (sUser2!=null){
            return Result.fail(401,"用户已经存在");
        }
        String password = sUser.getPassword();
        if(StringUtils.isEmpty(password)){
            password="123456";
        }
        String salt = Md5Utils.CretaeMd5();
        String password2 = Md5Utils.md5Encryption(password, salt);
        sUser.setStatus("1");
        sUser.setSalt(salt);
        sUser.setPassword(password2);
        sUser.setsId(SnowFlakeUtil.getId());
        sUser.setCreateDate(new Date());
        int insert = sUserMapper.insert(sUser);
        if (insert==0){
            return Result.fail(401,"添加用户失败");
        }
        return Result.succ("添加成功");
    }
    public SUserTokenVo findUserByToken(String token) {
        /*
         * 1.token合法性校验,是否为空,解析是否成功,redis是否存在
         * 2.如果校验失败,返回错误
         * 3.如果成功,返回对应的结果Login
         * */
        if (StringUtils.isBlank(token)) {
            return null;
        }
        //解析token
        try{
            Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
            if (stringObjectMap == null) {
                return null;
            }
        }catch (Exception e){
            return null;
        }
        //获取redis是否存在
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)) {
            return null;
        }
        SUserTokenVo sUser = JSON.parseObject(userJson, SUserTokenVo.class);
        return sUser;
    }
}
