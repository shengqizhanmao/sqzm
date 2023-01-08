package com.lin.sqzmHtgl.controller;

import com.lin.sqzmHtgl.common.Result;
import com.lin.sqzmHtgl.pojo.Resource;
import com.lin.sqzmHtgl.service.ResourceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    ResourceService resourceService;

    @RequiresPermissions("resource:get")
    @GetMapping("/get")
    public Result get(){
        return resourceService.getListResourceVo();
    }

    @RequiresPermissions("resource:get")
    @GetMapping("/getListResourceByTypeResources")
    public Result getListResourceByTypeResources(){
        return resourceService.getListResourceByTypeResources();
    }

    @RequiresPermissions("resource:add")
    @PostMapping("/add")
    public Result add(@RequestBody Resource resource){
        return resourceService.addResource(resource);
    }
    @RequiresPermissions("resource:update")
    @PostMapping("/update")
    public Result update(@RequestBody Resource resource){
        return resourceService.updateResource(resource);
    }
    @RequiresPermissions("resource:delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody Resource resource){
        return resourceService.deleteResourceById(resource.getId());
    }

}
