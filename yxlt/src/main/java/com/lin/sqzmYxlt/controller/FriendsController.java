package com.lin.sqzmYxlt.controller;

import com.lin.common.service.FriendsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 * @apiNote 好友
 * @author linShengWei
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/friends")
public class FriendsController {


    @Resource
    private FriendsService friendsService;

}
