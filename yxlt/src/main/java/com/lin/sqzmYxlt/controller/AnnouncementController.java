package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.service.AnnouncementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-25
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    @GetMapping("/get")
    public Result getByPalteIdAndModularsId(@RequestParam("palteId") String palteId, @RequestParam("modularsId") String modularsId) {
        return announcementService.getByPalteIdAndModularsId(palteId, modularsId);
    }

}
