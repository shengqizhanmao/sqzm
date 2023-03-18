package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.FriendsUser;

import java.util.List;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-19
 */
public interface FriendsUserService extends IService<FriendsUser> {


    Result getListFriendsUserVoByUserId(String userId);

    Result getListSearchAddByUsernameOrNickname(String usernameOrNickname, String token);

    Result add(String formUserId, String toUserId);

    Result getFriendsUserApplyList(String userId);

    Result updateStatus(String formUserId, String toUserId, String status);

    Result getListDetete(String userId);

    Result delete(String formUserId, String toUserId);

    List<FriendsUser> getListMethod(String formUserId);
}
