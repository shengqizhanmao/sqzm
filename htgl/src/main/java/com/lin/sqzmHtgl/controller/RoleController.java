package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.Role;
import com.lin.common.pojo.RoleResource;
import com.lin.common.service.RoleService;
import com.lin.sqzmHtgl.controller.param.AddRoleAndResource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @NotNull
    @RequiresPermissions("role:get")
    @GetMapping("/get")
    public Result get(){
        try{
            List<Role> list = roleService.list();
            return Result.succ("查询角色成功",list);
        }catch (Exception e){
            return Result.fail(500,"查询角色失败");
        }
    }

    @RequiresPermissions("role:get")
    @GetMapping("/getRoleAndResource")
    public Result getRoleAndResource(){
        return roleService.getRoleAndResource();
    }
    @RequiresPermissions("role:add")
    @PostMapping("/add")
    public Result add(@Nullable @RequestBody Role role){
        if (role==null){
            return Result.fail("参数不能为空");
        }
        return roleService.saveRole(role);
    }
    @RequiresPermissions("role:update")
    @PostMapping("/update")
    public Result update(@Nullable @RequestBody Role role){
        if (role==null){
            return Result.fail("参数不能为空");
        }
        return roleService.updateRole(role);
    }
    @RequiresPermissions("role:update")
    @PostMapping("/updateEnable")
    public Result updateEnable(@Nullable @RequestBody Role role){
        if (role==null){
            return Result.fail("参数不能为空");
        }
        return roleService.updateRoleEnableFlag(role);
    }
    @RequiresPermissions("role:delete")
    @PostMapping("/delete")
    public Result delete(@Nullable @RequestBody Role role){
        if (role==null){
            return Result.fail("id不能为空");
        }
        return roleService.deleteRoleById(role.getId());
    }
    @RequiresPermissions("role:add")
    @PostMapping("/addRoleAndResource")
    public Result add(@NotNull @RequestBody AddRoleAndResource addRoleAndResource){
        List<String> listResourceId = addRoleAndResource.getListResourceId();
        String roleId = addRoleAndResource.getRoleId();
        return roleService.addRoleAndResource(roleId,listResourceId);
    }

    @RequiresPermissions("role:delete")
    @PostMapping("/deleteRoleAndResource")
    public Result deleteRoleAndResource(@RequestBody RoleResource roleResource){
        return roleService.deleteRoleResource(roleResource);
    }
}
