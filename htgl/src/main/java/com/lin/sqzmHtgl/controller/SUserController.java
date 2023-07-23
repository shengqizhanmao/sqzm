package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.SUser;
import com.lin.common.pojo.Vo.SUserTokenVo;
import com.lin.common.service.SUserService;
import com.lin.sqzmHtgl.controller.param.AddSUserRole;
import com.lin.sqzmHtgl.controller.param.AddSUserSMenu;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/sUser")
public class SUserController {

    @Resource
    private SUserService sUserService;

    //获取系统用户,根据token
    @RequiresAuthentication
    @GetMapping("/getSUserByToken")
    public Result getSUserByToken(@RequestHeader("Authorization") String token) {
        SUserTokenVo sUserByToken = sUserService.findSUserByToken(token);
        return Result.succ("获取sUser信息成功", sUserByToken);
    }

    /**
        @Param Long size,分页的大小
        @Param Long page,第N页
     **/
    //获取系统用户
    @RequiresPermissions("sUser:get")
    @GetMapping("/get/{size}/{page}")
    //获取系统用户和角色的列表,分页
    public Result get(@PathVariable("size") Long size, @PathVariable("page") Long page) {
        return sUserService.get(size, page);
    }

    //获取系统用户拥有的角色
    @RequiresPermissions("sUser:get")
    @GetMapping("/getSUserRole/{sUserSId}")
    //获取系统用户和角色的列表,分页
    public Result getSUserAndRole(@PathVariable("sUserSId") String sUserSId) {
        return sUserService.getSUserRoleBySUserId(sUserSId);
    }

    //获取该系统用户拥有的系统目录列表
    @RequiresPermissions("sUser:get")
    @GetMapping("/getSUserSMenu/{sUserSId}")
    public Result getSMenuBySUserId(@PathVariable("sUserSId") String sUserSId) {
        return sUserService.getSUserSMenuBySUserId(sUserSId);
    }

    //添加系统用户
    @RequiresPermissions("sUser:add")
    @PostMapping("/add")
    public Result add(@RequestBody SUser sUser) {
        return sUserService.addSUser(sUser);
    }

    //添加系统用户拥有的角色
    @RequiresPermissions("sUser:add")
    @PostMapping("/addSUserRole")
    public Result addSUserRole(@RequestBody AddSUserRole addSUserRole) {
        List<String> listRoleId = addSUserRole.getListRoleId();
        String sId = addSUserRole.getsId();
        return sUserService.addSUserRole(listRoleId, sId);
    }

    //添加系统用户拥有的系统目录
    @RequiresPermissions("sUser:add")
    @PostMapping("/addSUserSMenu")
    public Result addSUserSMenu(@RequestBody AddSUserSMenu addSUserSMenu) {
        String sId = addSUserSMenu.getsId();
        List<String> SMenuSIdList = addSUserSMenu.getListSMenuId();
        if (StringUtils.isEmpty(sId)) {
            return Result.fail("参数不能为空");
        }
        return sUserService.addSUserSMenu(sId, SMenuSIdList);
    }

    //修改系统用户
    @RequiresPermissions("sUser:update")
    @PutMapping("/update")
    public Result update(@RequestBody SUser sUser) {
        return sUserService.updateSUser(sUser);
    }

    //修改系统用户的状态
    @RequiresPermissions("sUser:update")
    @PutMapping("/updateEnableFlag/{sId}/{enableFlag}")
    public Result UpdateEnableFlag(@PathVariable("sId") String sId, @PathVariable("enableFlag") String enableFlag) {
        if (StringUtils.isEmpty(sId)) {
            return Result.fail(403, "id参数不能为空");
        }
        if (StringUtils.isEmpty(enableFlag)) {
            return Result.fail(403, "enableFlag参数不能为空");
        }
        return sUserService.updateEnableFlag(sId, enableFlag);
    }

    //删除系统用户
    @RequiresPermissions("sUser:delete")
    @DeleteMapping("/delete/{sId}")
    public Result delete(@PathVariable("sId") String sId) {
        if (StringUtils.isEmpty(sId)) {
            return Result.fail(403, "参数不能为空");
        }
        return sUserService.deleteSUser(sId);
    }

    //删除系统用户拥有的角色
    @RequiresPermissions("sUser:delete")
    @DeleteMapping("/deleteSUserRole/{sUserSId}/{roleId}")
    public Result deleteSUserRole(@PathVariable("sUserSId") String sUserSId, @PathVariable("roleId") String roleId) {
        if (StringUtils.isEmpty(sUserSId) || StringUtils.isEmpty(roleId)) {
            return Result.fail(403, "参数不能为空");
        }
        return sUserService.deleteSUserRole(sUserSId, roleId);
    }

    //删除系统用户拥有的系统目录
    @RequiresPermissions("sUser:delete")
    @DeleteMapping("/deleteSUserSMenu/{sUserSId}/{sMenuId}")
    public Result deleteSUserSMenu(@PathVariable("sUserSId") String sUserSId, @PathVariable("sMenuId") String sMenuId) {
        if (StringUtils.isEmpty(sUserSId) || StringUtils.isEmpty(sMenuId)) {
            return Result.fail("参数不能为空");
        }
        return sUserService.deleteSUserSMenu(sUserSId, sMenuId);
    }
}
