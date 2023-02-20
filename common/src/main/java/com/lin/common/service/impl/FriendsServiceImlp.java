package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.FriendsMapper;
import com.lin.common.pojo.Friends;
import com.lin.common.service.FriendsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FriendsServiceImlp  extends ServiceImpl<FriendsMapper, Friends> implements FriendsService {

    @Resource
    private FriendsMapper friendsMapper;
    @Override
    public List<Friends> getFriendsByToUserIdAndFormUserId(String toUserId,String formUserId) {
        LambdaQueryWrapper<Friends> friendsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        friendsLambdaQueryWrapper.eq(Friends::getToUserId,toUserId).eq(Friends::getFormUserId,formUserId).orderByAsc(Friends::getCreatedDate);
        List<Friends> friendsList = friendsMapper.selectList(friendsLambdaQueryWrapper);
        return friendsList;
    }
}
