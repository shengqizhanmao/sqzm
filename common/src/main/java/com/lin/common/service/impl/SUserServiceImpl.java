package com.lin.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.RedisStatus;
import com.lin.common.Result;
import com.lin.common.mapper.RoleMapper;
import com.lin.common.mapper.SUserMapper;
import com.lin.common.pojo.Role;
import com.lin.common.pojo.SMenu;
import com.lin.common.pojo.SUser;
import com.lin.common.pojo.UserRole;
import com.lin.common.pojo.Vo.SUserTokenVo;
import com.lin.common.pojo.Vo.SUserVo;
import com.lin.common.service.RoleService;
import com.lin.common.service.SMenuService;
import com.lin.common.service.SUserService;
import com.lin.common.service.UserRoleService;
import com.lin.common.utils.JWTUtils;
import com.lin.common.utils.Md5Utils;
import com.lin.common.utils.PagesHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.*;

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
public class SUserServiceImpl extends ServiceImpl<SUserMapper, SUser> implements SUserService {

    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private SUserMapper sUserMapper;
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private SMenuService sMenuService;

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
        String sUserJson = redisTemplate.opsForValue().get(RedisStatus.TOKEN_SUser + token);
        if (StringUtils.isBlank(sUserJson)) {
            return null;
        }
        //解析token
        try {
            Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
            if (stringObjectMap.size() == 0) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return JSON.parseObject(sUserJson, SUserTokenVo.class);
    }

    @Override
    public SUser getSUserByUsername(String username) {
        LambdaQueryWrapper<SUser> sUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sUserLambdaQueryWrapper.eq(SUser::getUsername, username);
        return sUserMapper.selectOne(sUserLambdaQueryWrapper);
    }

    /**
     获取系统用户的列表,分页
     @Param Long size,分页的大小
     @Param Long page,第N页
     **/
    @Override
    public Result get(Long size, Long page) {
        if (size <= 0L) {
            return Result.fail("参数错误,个数不能为0");
        }
        if (page <= 0L) {
            return Result.fail("参数错误,页数不能为0");
        }
        try {
            Page<SUser> sUserPage = sUserMapper.selectPage(page, size);//获取数据
            long total = sUserPage.getTotal();//获取全部共n条
            List<SUser> sUserList = sUserPage.getRecords();//获取查询数据
            List<SUserVo> sUserVos = ListCopy(sUserList);
            Map<String, Object> sUserListHashMap = PagesHashMap.getPagesHashMap(total, "sUserList", sUserVos);
            return Result.succ("查询角色成功", sUserListHashMap);
        } catch (Exception e) {
            return Result.succ(500,"查询角色失败", e);
        }
    }

    @Resource
    RoleMapper roleMapper;

    /**
     * @param  sUserSId 系统用户Id
     */
    @Override
    public Result getSUserRoleBySUserId(String sUserSId) {
        List<Role> listRoleBySUserId = roleService.getListRoleBySUserId(sUserSId);
        return Result.succ("查询角色成功", listRoleBySUserId);
    }


    /**
     * @param String sUserSId ;系统用户的id
     * */
    @Override
    public Result getSUserSMenuBySUserId(String sUserSId) {
        return sMenuService.getSMenuBySUserId2(sUserSId);
    }

    /**
    * @param SUser sUser ;系统用户的实体类
    * */
    @Override
    public Result addSUser(@NotNull SUser sUser) {
        String sUsername = sUser.getUsername();
        if (StringUtils.isEmpty(sUsername)) {
            return Result.fail("不能缺少登录名");
        }
        LambdaQueryWrapper<SUser> sUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sUserLambdaQueryWrapper.eq(SUser::getUsername, sUsername);
        SUser sUser2 = sUserMapper.selectOne(sUserLambdaQueryWrapper);
        if (sUser2 != null) {
            return Result.fail(402, "用户已经存在");
        }
        String password = sUser.getPassword();
        if (StringUtils.isEmpty(password)) {
            password = "123456";
        }
        String salt = Md5Utils.CretaeMd5();
        String password2 = Md5Utils.md5Encryption(password, salt);
        sUser.setEnableFlag("1");
        sUser.setSalt(salt);
        sUser.setPassword(password2);
        sUser.setCreateDate(new Date());
        try {
            int insert = sUserMapper.insert(sUser);
            if (insert == 0) {
                return Result.fail(401, "添加用户失败");
            }
            return Result.succ("添加成功");
        } catch (DataAccessException e) {
            log.error("出现错误："+e);
            return Result.fail(505, "添加用户失败");
        }
    }

    @Override
    public Result addSUserRole(List<String> listRoleId, String sId) {
        return userRoleService.addSUserRole(listRoleId, sId);
    }

    @Override
    public Result addSUserSMenu(String sId, List<String> sMenuSIdList) {
        return sMenuService.addSUserAndSMenu(sId, sMenuSIdList);
    }

    //修改用户
    @Override
    public Result updateSUser(SUser sUser) {
        SUser sUser1 = sUserMapper.selectById(sUser.getsId());
        if (sUser1==null){
            return Result.fail("用户不存在");
        }
        if (!StringUtils.isEmpty(sUser.getEmail())) {
            sUser1.setEmail(sUser.getEmail());//邮箱
        }
        if (!StringUtils.isEmpty(sUser.getNickname())) {
            sUser1.setNickname(sUser.getNickname());//昵称
        }
        if (!StringUtils.isBlank(sUser.getRealname())){
            sUser1.setRealname(sUser.getRealname());//真实姓名
        }
        if (!StringUtils.isEmpty(sUser.getGender())) {
            sUser1.setGender(sUser.getGender());//性别
        }
        if (!StringUtils.isBlank(sUser.getPassword())) {//密码和盐
            String salt = Md5Utils.CretaeMd5();
            String password2 = Md5Utils.md5Encryption(sUser.getPassword(), salt);
            sUser1.setPassword(password2);
            sUser1.setSalt(salt);
        }
        try {
            int update = sUserMapper.updateById(sUser1);
            return Result.succ("修改成功", update);
        } catch (Exception e) {
            log.info("修改失败,原因是:" + e);
            return Result.fail("修改失败,原因是:" + e);
        }
    }

    @Override
    public Result updateEnableFlag(String sId, String enableFlag) {
        SUser sUser = sUserMapper.selectById(sId);
        sUser.setEnableFlag(enableFlag);
        int update = sUserMapper.updateById(sUser);
        if (update == 0) {
            if (enableFlag.equals("1")) {
                return Result.fail("启动系统用户失败");
            }
            return Result.fail("禁用系统用户失败");
        }
        if (enableFlag.equals("1")) {
            return Result.succ("启动系统用户成功");
        }
        return Result.succ("禁用系统用户成功");
    }

    /*删除系统用户
    *
    * @param String sId;系统用户的id
    * */
    @Override
    public Result deleteSUser(String sId) {
        //获取事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            //先删除系统用户拥有的角色
            LambdaUpdateWrapper<UserRole> userRoleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userRoleLambdaUpdateWrapper.eq(UserRole::getUserId, sId);
            userRoleService.remove(userRoleLambdaUpdateWrapper);
            //再删除系统用户拥有的系统目录
            sMenuService.deleteSUserMenu(sId);
            //最后删除用户
            int i = sUserMapper.deleteById(sId);
            if (i == 0) {
                dataSourceTransactionManager.rollback(transactionStatus);//回滚事务
                return Result.fail("删除失败,用户不存在");
            }
            dataSourceTransactionManager.commit(transactionStatus);
            return Result.succ("删除成功");
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);//回滚事务
            return Result.fail("删除失败,原因是:" + e);
        }
    }

    @Override
    public Result deleteSUserRole(String sUserSId, String roleId) {
        try {
            LambdaUpdateWrapper<UserRole> userRoleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userRoleLambdaUpdateWrapper.eq(UserRole::getUserId, sUserSId).eq(UserRole::getRoleId, roleId);
            boolean remove = userRoleService.remove(userRoleLambdaUpdateWrapper);
            if (!remove) {
                return Result.fail("删除失败,系统用户拥有的角色");
            }
            return Result.succ("删除系统用户拥有的角色成功");
        } catch (Exception e) {
            return Result.fail("删除系统用户拥有的角色失败,原因是:" + e);
        }
    }

    @Override
    public Result deleteSUserSMenu(String sUserSId, String sMenuId) {
        return sMenuService.deleteSUserMenu(sUserSId, sMenuId);
    }

    //Method
    @NotNull
    private List<SUserVo> ListCopy(@NotNull List<SUser> sUsers) {
        List<SUserVo> sUserVos = new ArrayList<>();
        for (SUser sUser : sUsers) {
            SUserVo sUserVo = new SUserVo();
            BeanUtils.copyProperties(sUser, sUserVo);
            sUserVos.add(sUserVo);
        }
        return sUserVos;
    }
}
