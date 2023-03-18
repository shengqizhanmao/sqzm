package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.service.FriendsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @apiNote 好友
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/friends")
public class FriendsController {


    @Resource
    private FriendsService friendsService;

    @GetMapping("/getAll")
    public Result getAllList(@RequestParam("formUserId") String formUserId, @RequestParam("toUserId") String toUserId) {
        return friendsService.getAllList(formUserId, toUserId);
    }

    @GetMapping("/getTen")
    public Result getTen(@RequestParam("formUserId") String formUserId, @RequestParam("toUserId") String toUserId, @RequestParam("page") Long page, @RequestParam("size") Long size) {
        return friendsService.getTen(formUserId, toUserId, page, size);
    }

    @GetMapping("/getVo")
    public Result getFriendsAndUserVo(@RequestHeader("Authorization") String token) {
        return friendsService.getFriendsAndUserVo(token);
    }
}
