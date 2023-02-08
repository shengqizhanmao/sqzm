package com.lin.sqzmHtgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.common.Result;
import com.lin.sqzmHtgl.controller.param.AddSUserAndRole;
import com.lin.sqzmHtgl.pojo.RoleResource;
import com.lin.sqzmHtgl.pojo.UserRole;
import com.lin.sqzmHtgl.mapper.UserRoleMapper;
import com.lin.sqzmHtgl.service.RoleResourceService;
import com.lin.sqzmHtgl.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    UserRoleService userRoleService;

    @Override
    public Result addSUserAndRole(AddSUserAndRole addSUserAndRole) {
        List<String> listRoleId = addSUserAndRole.getListRoleId();
        String sId = addSUserAndRole.getsId();
        if (StringUtils.isEmpty(sId)) {
            return Result.fail("系统用户id参数不能为空");
        }
        try {
            LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            userRoleLambdaQueryWrapper2.eq(UserRole::getUserId, sId);
            userRoleService.remove(userRoleLambdaQueryWrapper2);
        }catch (Exception e) {
            e.printStackTrace();
            Result.fail(500,"删除用户拥有角色失败");
        }
        if (listRoleId.size()==0){
            return Result.succ("修改用户拥有角色连接成功");
        }
        for (String roleId : listRoleId) {
            if (StringUtils.isEmpty(roleId)) {
                return Result.fail("资源id参数不能为空");
            }
            UserRole userRole = new UserRole();
            userRole.setUserId(sId);
            userRole.setRoleId(roleId);
            userRole.setEnableFlag("1");
            LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper=new LambdaQueryWrapper<>();
            userRoleLambdaQueryWrapper.eq(UserRole::getUserId,sId);
            userRoleLambdaQueryWrapper.eq(UserRole::getRoleId,roleId);
            try{
                UserRole one = userRoleService.getOne(userRoleLambdaQueryWrapper);
                if (one!=null){
                    Result.fail("用户拥有角色已经存在");
                }
            }catch (Exception e){
                log.error(e.toString());
                Result.fail("用户拥有角色已经存在");
            }
            try {
                userRoleService.save(userRole);
            } catch (Exception e) {
                e.printStackTrace();
                Result.fail("修改用户拥有角色失败");
            }
        }
        return Result.succ("修改用户与角色连接成功");
    }
}
