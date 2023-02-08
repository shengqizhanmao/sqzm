package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;

import com.lin.common.pojo.UserRole;
import com.lin.common.pojo.param.AddSUserAndRole;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface UserRoleService extends IService<UserRole> {

    Result addSUserAndRole(AddSUserAndRole addSUserAndRole);
}
