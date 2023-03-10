package com.lin.sqzmHtgl.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lin.common.Result;
import com.lin.common.pojo.SUser;
import com.lin.common.pojo.UserRole;
import com.lin.common.pojo.Vo.SUserTokenVo;
import com.lin.common.pojo.Vo.SUserVo;
import com.lin.common.service.SUserService;
import com.lin.common.service.UserRoleService;
import com.lin.sqzmHtgl.controller.param.AddSUserAndRole;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/sUser")
public class SUserController {

    @Resource
    SUserService sUserService;
    @Resource
    UserRoleService userRoleService;

    @NotNull
    @RequiresAuthentication
    @GetMapping("/getSUserByToken")
    public Result getSUserByToken(
            @RequestHeader("Authorization") String token
    ) {
        SUserTokenVo sUserByToken = sUserService.findSUserByToken(token);
        return Result.succ("获取sUser信息成功",sUserByToken);
    }
    @NotNull
    @RequiresPermissions("sUser:get")
    @GetMapping("/get")
    public Result get(){
            List<SUser> list = sUserService.list();
            List<SUserVo> list1=new ArrayList<>();
            for (SUser sUser:list) {
                SUserVo sUserVo= new SUserVo();
                BeanUtils.copyProperties(sUser, sUserVo);
                list1.add(sUserVo);
            }
            return Result.succ("查询用户成功",list1);
    }
    @RequiresPermissions("sUser:get")
    @GetMapping("/getSUserAndRole")
    public Result getSUserAndRole(){
        return sUserService.getSUserAndRole();
    }

    @RequiresPermissions("sUser:add")
    @PostMapping("/add")
    public Result add(@RequestBody SUser sUser){
            return sUserService.addSUser(sUser);
        }

    @RequiresPermissions("sUser:add")
    @PostMapping("/addSUserAndRole")
    public Result addSUserAndRole(@NotNull @RequestBody AddSUserAndRole addSUserAndRole){
        List<String> listRoleId = addSUserAndRole.getListRoleId();
        String sId = addSUserAndRole.getsId();
        return userRoleService.addSUserAndRole(listRoleId,sId);
    }

        @RequiresPermissions("sUser:update")
        @PostMapping("/update")
        public Result update(@RequestBody SUser sUser){
            return sUserService.updateSUser(sUser);
        }

        @RequiresPermissions("sUser:update")
        @PostMapping("/updateEnableFlag")
        public Result UpdateEnableFlag(@RequestBody SUser sUser){
            return sUserService.updateEnableFlag(sUser);
        }
        @RequiresPermissions("sUser:delete")
        @PostMapping("/delete")
        public Result delete(@RequestBody SUser sUser) {
            try {
                LambdaUpdateWrapper<UserRole> lambdaUpdateWrapper = new LambdaUpdateWrapper();
                lambdaUpdateWrapper.eq(UserRole::getUserId, sUser.getsId());
                userRoleService.remove(lambdaUpdateWrapper);
            } catch (Exception e) {
                return Result.fail(500, "删除失败");
            }
            return sUserService.deleteSUser(sUser.getsId());
        }
}
