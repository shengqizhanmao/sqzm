package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.pojo.Resource;
import com.lin.common.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result get() {
        return resourceService.getListResourceVo();
    }

    @RequiresPermissions("resource:get")
    @GetMapping("/getListResourceByTypeResources")
    public Result getListResourceByTypeResources() {
        return resourceService.getListResourceByTypeResources();
    }

    @RequiresPermissions("resource:add")
    @PostMapping("/add")
    public Result add(@RequestBody Resource resource) {
        return resourceService.addResource(resource);
    }

    @RequiresPermissions("resource:update")
    @PutMapping("/update")
    public Result update(@RequestBody Resource resource) {
        return resourceService.updateResource(resource);
    }

    @RequiresPermissions("resource:delete")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return Result.fail("参数不能为空");
        }
        return resourceService.deleteResourceById(id);
    }

}
