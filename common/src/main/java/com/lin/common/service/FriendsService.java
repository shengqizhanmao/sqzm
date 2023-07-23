package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Friends;
import org.springframework.transaction.TransactionStatus;

/**
 * <p>
 * 服务类,保存聊天记录
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
public interface FriendsService extends IService<Friends> {
    Result getAllList(String formUserId, String toUserId);

    Result getTen(String formUserId, String toUserId, Long page, Long size);

    Result getFriendsAndUserVo(String token);

    boolean deleteByUserId(String id, TransactionStatus transaction);
}
