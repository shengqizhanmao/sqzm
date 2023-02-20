package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.pojo.Palte;
import com.lin.common.service.ModularsService;
import com.lin.common.service.PalteService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @apiNote 模块
 * @author linShengWei
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/modulars")
public class ModularsController {

    @Resource
    private ModularsService modularsService;

    @GetMapping("/get")
    public Result getModularsByPalteId(@Param("palteId")String palteId){
        return modularsService.getModularsByPalteId(palteId);
    }
}
