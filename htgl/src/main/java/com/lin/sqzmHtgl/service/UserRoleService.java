package com.lin.sqzmHtgl.service;

import com.lin.common.Result;
import com.lin.sqzmHtgl.controller.param.AddSUserAndRole;
import com.lin.sqzmHtgl.pojo.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

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
