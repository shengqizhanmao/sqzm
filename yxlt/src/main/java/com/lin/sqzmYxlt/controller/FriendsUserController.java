package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.pojo.FriendsUser;
import com.lin.common.service.FriendsUserService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-19
 */
@RestController
@RequestMapping("/friendsUser")
public class FriendsUserController {
    @Resource
    private FriendsUserService friendsUserService;

    @GetMapping("/getList")
    public Result getListByUserId(@RequestParam("userId") String userId){
       return friendsUserService.getListFriendsUserVoByUserId(userId);
    }

    @GetMapping("/getListSearch")
    public Result getListSearchAddByUsernameOrNickname(@RequestParam("usernameOrNickname") String usernameOrNickname,@RequestHeader("Authorization") String token){
//        模糊查询 username or nickname
        return friendsUserService.getListSearchAddByUsernameOrNickname(usernameOrNickname,token);
    }

    @PostMapping("add")
    public Result add(@RequestBody FriendsUser friendsUser){
        String toUserId = friendsUser.getToUserId();
        String formUserId = friendsUser.getFormUserId();
        return friendsUserService.add(formUserId, toUserId);
    }
    @GetMapping("/getApplyList")
    public Result getFriendsUserApplyList(@RequestParam("userId") String userId){
        return friendsUserService.getFriendsUserApplyList(userId);
    }
    @PostMapping("/updateStatus")
    public Result updateStatus(@RequestBody FriendsUser friendsUser){
        String toUserId = friendsUser.getToUserId();
        String formUserId = friendsUser.getFormUserId();
        String status = friendsUser.getStatus();
        return friendsUserService.updateStatus(formUserId, toUserId,status);
    }

    @GetMapping("/getListDelete")
    public Result getListDelete(@RequestParam("userId") String userId){
        return friendsUserService.getListDetete(userId);
    }
    @PostMapping("/delete")
    public Result delete(@RequestBody FriendsUser friendsUser){
        String toUserId = friendsUser.getToUserId();
        String formUserId = friendsUser.getFormUserId();
        return friendsUserService.delete(formUserId,toUserId);
    }
}
