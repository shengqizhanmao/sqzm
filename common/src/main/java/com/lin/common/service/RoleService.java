package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Role;
import com.lin.common.pojo.RoleResource;


import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface RoleService extends IService<Role> {
    Result getRoleAndResource();
    Result saveRole(Role role);
    Result deleteRoleById(String id);
    Result updateRole(Role role);
    Result addRoleAndResource(String roleId,List<String> listResourceId);
    Result deleteRoleResource(RoleResource roleResource);
    Result updateRoleEnableFlag(Role role);
    List<Role> getListRoleBySUserId(String sId);
}
