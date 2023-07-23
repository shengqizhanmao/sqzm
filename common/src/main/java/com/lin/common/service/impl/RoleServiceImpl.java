package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.RoleMapper;
import com.lin.common.pojo.Resource;
import com.lin.common.pojo.Role;
import com.lin.common.pojo.RoleResource;
import com.lin.common.pojo.Vo2.RoleAndResourceVo;
import com.lin.common.service.ResourceService;
import com.lin.common.service.RoleResourceService;
import com.lin.common.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RoleResourceService roleResourceService;

    @NotNull
    @Override
    public Result getRoleAndResource() {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Role::getSortNo);
        List<Role> listRole = roleMapper.selectList(lambdaQueryWrapper);
        List<RoleAndResourceVo> roleAndResourceVos = ListCopy(listRole);
        return Result.succ("查询角色成功", roleAndResourceVos);
    }

    @NotNull
    @Override
    public Result saveRole(@NotNull Role role) {
        role.setEnableFlag("1");
        if (StringUtils.isEmpty(role.getRoleName())) {
            return Result.fail("添加失败,角色名称不能为空");
        }
        try {
            int insert = roleMapper.insert(role);
            if (insert == 0) {
                return Result.fail("添加失败");
            }
            return Result.succ("添加成功");
        } catch (Exception e) {
            log.error("添加角色失败,原因是" + e.toString());
            return Result.fail("添加失败");
        }
    }

    @NotNull
    @Transactional
    @Override
    public Result deleteRoleById(@Nullable String id) {
        if (id == null) {
            return Result.fail("id不能为空");
        }
        try {
            int i = roleMapper.deleteById(id);
            LambdaUpdateWrapper<RoleResource> lambdaUpdateWrapper = new LambdaUpdateWrapper<RoleResource>();
            lambdaUpdateWrapper.eq(RoleResource::getRoleId, id);
            roleResourceService.remove(lambdaUpdateWrapper);
            if (i == 0) {
                return Result.fail("删除角色失败,该角色不存在");
            }
        } catch (Exception e) {
            log.error("删除角色失败,原因是" + e);
            return Result.fail("删除失败");
        }
        return Result.succ("删除角色成功");
    }

    @NotNull
    @Override
    public Result updateRole(@NotNull Role role) {
        if (StringUtils.isEmpty(role.getId())) {
            return Result.fail("id参数不能为空");
        }
        try {
            LambdaUpdateWrapper<Role> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(Role::getId, role.getId())
                    .set(Role::getRoleName, role.getRoleName())
                    .set(Role::getDescription, role.getDescription())
                    .set(Role::getSortNo, role.getSortNo())
                    .set(Role::getLabel, role.getLabel());
            int update = roleMapper.update(role, lambdaUpdateWrapper);
            if (update == 0) {
                return Result.fail("修改角色失败");
            }
            return Result.succ("修改角色成功");
        } catch (Exception e) {
            log.error("角色修改失败,原因:" + e);
            return Result.fail("修改失败");
        }
    }

    @NotNull
    @Override
    public Result updateRoleEnableFlag(String id, String enableFlag) {
        try {
            Role role = roleMapper.selectById(id);
            role.setEnableFlag(enableFlag);
            int update = roleMapper.updateById(role);
            LambdaUpdateWrapper<RoleResource> roleResourceLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            roleResourceLambdaUpdateWrapper.eq(RoleResource::getRoleId, id);
            List<RoleResource> list = roleResourceService.list(roleResourceLambdaUpdateWrapper);
            for (RoleResource roleResource : list) {
                roleResource.setEnableFlag(enableFlag);
                roleResourceService.updateById(roleResource);
            }
            if (update == 0) {
                if (enableFlag.equals("1")) {
                    return Result.fail("启动角色失败");
                }
                return Result.fail("禁用角色失败");
            }
            if (enableFlag.equals("1")) {
                return Result.succ("启动角色成功");
            }
            return Result.succ("禁用角色成功");
        } catch (Exception e) {
            log.error("角色状态修改失败,原因:" + e);
            return Result.fail(500, "状态修改失败");
        }
    }

    /*
    * @param String roleId;角色的id
    * @param List<String> listResourceId;资源的id列表
    * */
    @NotNull
    @Override
    public Result addRoleAndResource(String roleId, @NotNull List<String> listResourceId) {
        if (StringUtils.isEmpty(roleId)) {
            return Result.fail("角色id参数不能为空");
        }
        try {
            //先继续删除
            LambdaQueryWrapper<RoleResource> roleResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleResourceLambdaQueryWrapper.eq(RoleResource::getRoleId, roleId);
            roleResourceService.remove(roleResourceLambdaQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            Result.fail("删除角色拥有的资源失败");
        }
        if (listResourceId.size() == 0) {
            return Result.succ("修改角色拥有的资源成功");
        }
        List<RoleResource> roleResources = new ArrayList<>();
        //再添加
        for (String resourceId : listResourceId) {
            if (StringUtils.isEmpty(resourceId)) {
                continue;
            }
            RoleResource roleResource = new RoleResource();
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(resourceId);
            roleResource.setEnableFlag("1");
            roleResources.add(roleResource);
        }
        roleResourceService.saveBatch(roleResources);
        return Result.succ("修改角色拥有的资源成功");
    }

    @NotNull
    @Override
    public Result deleteRoleResource(String roleId, String resourceId) {
        LambdaUpdateWrapper<RoleResource> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(RoleResource::getRoleId, roleId).eq(RoleResource::getResourceId, resourceId);
        boolean remove = roleResourceService.remove(lambdaQueryWrapper);
        if (remove) {
            return Result.succ("角色拥有的权限删除成功");
        }
        return Result.fail("角色拥有的权限删除失败");
    }



    @Override
    public List<Role> getListRoleBySUserId(String sId) {
        return roleMapper.getListRoleBySUserId(sId);
    }

    //Method
    @NotNull
    private RoleAndResourceVo Copy(@NotNull Role role) {
        RoleAndResourceVo roleAndResourceVo = new RoleAndResourceVo();
        BeanUtils.copyProperties(role, roleAndResourceVo);
        List<Resource> listResourceByRoleId = resourceService.getListResourceByRoleId(role.getId());
        roleAndResourceVo.setListResourceName(listResourceByRoleId);
        return roleAndResourceVo;
    }

    @NotNull
    private List<RoleAndResourceVo> ListCopy(@NotNull List<Role> roles) {
        List<RoleAndResourceVo> roleAndResourceVos = new ArrayList<>();
        for (Role role : roles) {
            RoleAndResourceVo copy = Copy(role);
            roleAndResourceVos.add(copy);
        }
        return roleAndResourceVos;
    }
}
