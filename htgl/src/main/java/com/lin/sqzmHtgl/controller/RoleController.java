package com.lin.sqzmHtgl.controller;

import com.lin.sqzmHtgl.common.Result;
import com.lin.sqzmHtgl.controller.param.AddRoleAndResource;
import com.lin.sqzmHtgl.pojo.Role;
import com.lin.sqzmHtgl.pojo.RoleResource;
import com.lin.sqzmHtgl.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户角色表 前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @RequiresPermissions("role:get")
    @GetMapping("/get")
    public Result get(){
        return roleService.getRoleAndResource();
    }

    @RequiresPermissions("role:add")
    @PostMapping("/add")
    public Result add(@RequestBody Role role){
        if (role==null){
            return Result.fail("参数不能为空");
        }
        return roleService.saveRole(role);
    }
    @RequiresPermissions("role:update")
    @PostMapping("/update")
    public Result update(@RequestBody Role role){
        if (role==null){
            return Result.fail("参数不能为空");
        }
        return roleService.updateRole(role);
    }
    @RequiresPermissions("role:delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody Role role){
        if (role==null){
            return Result.fail("id不能为空");
        }
        return roleService.deleteRoleById(role.getId());
    }
    @RequiresPermissions("role:add")
    @PostMapping("/addRoleAndResource")
    public Result add(@RequestBody AddRoleAndResource addRoleAndResource){
        return roleService.addRoleAndResource(addRoleAndResource);
    }
    @RequiresPermissions("role:delete")
    @PostMapping("/deleteRoleAndResource")
    public Result deleteRoleAndResource(@RequestBody RoleResource roleResource){
        return roleService.deleteRoleResource(roleResource);
    }
}
