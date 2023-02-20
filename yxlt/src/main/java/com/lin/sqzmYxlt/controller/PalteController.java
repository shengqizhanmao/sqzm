package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.aop.CacheAnnotation;
import com.lin.common.pojo.Palte;
import com.lin.common.service.PalteService;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @apiNote 板块
 * @author linShengWei
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/palte")
public class PalteController {

    @Resource
    private PalteService palteService;


    @NotNull
    @CacheAnnotation(expire = 1000*60*60,name = "yxlt:palte")
    @GetMapping("/get")
    public Result getPalte(){
        List<Palte> list = palteService.list();
        return Result.succ("查询板块成功",list);
    }
}
