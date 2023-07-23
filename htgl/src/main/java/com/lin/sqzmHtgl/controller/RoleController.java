package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.Role;
import com.lin.common.service.RoleService;
import com.lin.sqzmHtgl.controller.param.AddRoleAndResource;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    @Resource
    private RoleService roleService;

    @RequiresPermissions("role:get")
    @GetMapping("/get")
    public Result get() {
        try {
            List<Role> list = roleService.list();
            return Result.succ("查询角色成功", list);
        } catch (Exception e) {
            return Result.fail(500, "查询角色失败");
        }
    }

    @RequiresPermissions("role:get")
    @GetMapping("/getRoleAndResource")
    public Result getRoleAndResource() {
        return roleService.getRoleAndResource();
    }

    @RequiresPermissions("role:add")
    @PostMapping("/add")
    public Result add(@RequestBody Role role) {
        if (role == null) {
            return Result.fail("参数不能为空");
        }
        return roleService.saveRole(role);
    }

    @RequiresPermissions("role:add")
    @PostMapping("/addRoleAndResource")
    public Result add( @RequestBody AddRoleAndResource addRoleAndResource) {
        List<String> listResourceId = addRoleAndResource.getListResourceId();
        String roleId = addRoleAndResource.getRoleId();
        return roleService.addRoleAndResource(roleId, listResourceId);
    }

    @RequiresPermissions("role:update")
    @PutMapping("/update")
    public Result update( @RequestBody Role role) {
        if (role == null) {
            return Result.fail("参数不能为空");
        }
        return roleService.updateRole(role);
    }

    @RequiresPermissions("role:update")
    @PutMapping("/updateEnable/{id}/{enableFlag}")
    public Result updateEnable(@PathVariable("id") String id, @PathVariable("enableFlag") String enableFlag) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(enableFlag)) {
            return Result.fail("参数不能为空");
        }
        return roleService.updateRoleEnableFlag(id, enableFlag);
    }

    @RequiresPermissions("role:delete")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return Result.fail("参数不能为空");
        }
        return roleService.deleteRoleById(id);
    }

    @RequiresPermissions("role:delete")
    @DeleteMapping("/delete/{roleId}/{resourceId}")
    public Result deleteRoleAndResource(@PathVariable("roleId") String roleId, @PathVariable("resourceId") String resourceId) {
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(resourceId)) {
            return Result.fail("参数不能为空");
        }
        return roleService.deleteRoleResource(roleId, resourceId);
    }
}
