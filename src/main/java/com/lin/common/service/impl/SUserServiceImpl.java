package com.lin.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.RedisStatus;
import com.lin.common.Result;
import com.lin.common.utils.JWTUtils;
import com.lin.common.utils.Md5Utils;
import com.lin.common.utils.SnowFlakeUtil;
import com.lin.common.mapper.SUserMapper;
import com.lin.common.pojo.Role;
import com.lin.common.pojo.SUser;
import com.lin.common.pojo.Vo.SUserAndRoleVo;
import com.lin.common.pojo.Vo.SUserTokenVo;
import com.lin.common.service.RoleService;
import com.lin.common.service.SUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Slf4j
@Service
public class SUserServiceImpl extends ServiceImpl<SUserMapper, SUser> implements SUserService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Resource
    SUserMapper sUserMapper;
    @Resource
    RoleService roleService;
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
        String sUserJson = redisTemplate.opsForValue().get(RedisStatus.TOKEN + token);
        if (StringUtils.isBlank(sUserJson)) {
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
        SUserTokenVo sUser = JSON.parseObject(sUserJson, SUserTokenVo.class);
        return sUser;
    }

    @Override
    public SUser getSUserByUsername(String sUsername) {
        LambdaQueryWrapper<SUser> sUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sUserLambdaQueryWrapper.eq(SUser::getUsername,sUsername);
        SUser sUser = sUserMapper.selectOne(sUserLambdaQueryWrapper);
        return sUser;
    }

    //根据SUserId获取Role
    @Override
    public Result getSUserAndRole() {
        List<SUser> listRole = sUserMapper.selectList(null);
        List<SUserAndRoleVo> sUserAndRoleVos = ListCopy(listRole);
        return Result.succ("查询角色成功",sUserAndRoleVos);
    }

    //添加用户
    public Result addSUser(SUser sUser){
        String sUsername = sUser.getUsername();
        if(StringUtils.isEmpty(sUsername)){
            return Result.fail("不能缺少登录名");
        }
        LambdaQueryWrapper<SUser> sUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sUserLambdaQueryWrapper.eq(SUser::getUsername,sUsername);
        SUser sUser2 = sUserMapper.selectOne(sUserLambdaQueryWrapper);
        if (sUser2!=null){
            return Result.fail(402,"用户已经存在");
        }
        String password = sUser.getPassword();
        if(StringUtils.isEmpty(password)){
            password="123456";
        }
        String salt = Md5Utils.CretaeMd5();
        String password2 = Md5Utils.md5Encryption(password, salt);
        sUser.setEnableFlag("1");
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
    //修改用户
    public Result updateSUser(SUser sUser){
        SUser sUser1 = sUserMapper.selectById(sUser.getsId());
        if(!StringUtils.isEmpty(sUser.getEmail())){
            sUser1.setEmail(sUser.getEmail());//邮箱
        }
        if(!StringUtils.isEmpty(sUser.getNickname())){
            sUser1.setNickname(sUser.getNickname());//昵称
        }
        if(!StringUtils.isEmpty(sUser.getGender())){
            sUser1.setGender(sUser.getGender());//性别
        }
        try {
            int update = sUserMapper.updateById(sUser1);
            return Result.succ("修改成功",update);
        }catch (Exception e){
            log.info("修改失败,原因是:"+e);
            return Result.fail("修改失败,原因是:"+e);
        }
    }
    public Result updateEnableFlag(SUser sUser) {
        String enableFlag = sUser.getEnableFlag();
        String id = sUser.getsId();
        if(StringUtils.isEmpty(id)){
            return Result.fail(403,"id参数不能为空");
        }
        if (StringUtils.isEmpty(enableFlag)){
            return Result.fail(403,"enableFlag参数不能为空");
        }
        LambdaUpdateWrapper<SUser> lambdaUpdateWrapper =new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SUser::getsId,id);
        lambdaUpdateWrapper.set(SUser::getEnableFlag,enableFlag);
        int update = sUserMapper.update(null, lambdaUpdateWrapper);
        if (update==0) {
            if(enableFlag.equals("1")){
                return Result.fail("启动用户失败");
            }
            return Result.fail( "禁用用户失败");
        }
        if(enableFlag.equals("1")){
            return Result.succ("启动用户成功");
        }
        return Result.succ("禁用用户成功");
    }
    //删除用户
    public Result deleteSUser(String id){
        try {
            int i = sUserMapper.deleteById(id);
            if (i==0){
                return Result.fail("删除失败,用户不存在");
            }
            return Result.succ("删除成功");
        }catch (Exception e){
            return Result.fail("删除失败,原因是:"+e);
        }
    }

    //Method
    private SUserAndRoleVo Copy(SUser sUser){
        SUserAndRoleVo sUserAndRoleVo = new SUserAndRoleVo();
        BeanUtils.copyProperties(sUser,sUserAndRoleVo);
        List<Role> listRoleBySUserId = roleService.getListRoleBySUserId(sUser.getsId());
        sUserAndRoleVo.setListRoleName(listRoleBySUserId);
        return sUserAndRoleVo;
    }
    private List<SUserAndRoleVo> ListCopy(List<SUser> sUsers){
        List<SUserAndRoleVo> sUserAndRoleVos = new ArrayList<>();
        for(SUser sUser:sUsers){
            SUserAndRoleVo copy = Copy(sUser);
            sUserAndRoleVos.add(copy);
        }
        return sUserAndRoleVos;
    }
}
