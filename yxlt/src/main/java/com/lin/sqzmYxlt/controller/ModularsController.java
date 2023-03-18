package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.service.ModularsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @apiNote 模块
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/modulars")
public class ModularsController {

    @Resource
    private ModularsService modularsService;

    @GetMapping("/get")
    public Result getModularsByPalteId(@Param("palteId") String palteId) {
        return modularsService.getModularsByPalteId(palteId);
    }
}
