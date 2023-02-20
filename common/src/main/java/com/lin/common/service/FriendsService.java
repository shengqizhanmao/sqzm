package com.lin.common.service;

import com.lin.common.pojo.Friends;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
public interface FriendsService extends IService<Friends> {

    List<Friends> getFriendsByToUserIdAndFormUserId(String toUserId, String formUserId);
}
