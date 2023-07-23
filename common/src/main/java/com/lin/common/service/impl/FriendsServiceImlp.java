package com.lin.common.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.FriendsMapper;
import com.lin.common.pojo.Friends;
import com.lin.common.pojo.FriendsUser;
import com.lin.common.pojo.User;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.pojo.Vo2.FriendsAndUserVo;
import com.lin.common.service.FriendsService;
import com.lin.common.service.FriendsUserService;
import com.lin.common.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FriendsServiceImlp extends ServiceImpl<FriendsMapper, Friends> implements FriendsService {

    @Resource
    private FriendsMapper friendsMapper;

    @Resource
    private UserService userService;

    @Resource
    private FriendsUserService friendsUserService;

    @Override
    public Result getAllList(String formUserId, String toUserId) {
        if (StringUtils.isBlank(formUserId)) {
            return Result.fail("参数不能为空");
        }
        if (StringUtils.isBlank(toUserId)) {
            return Result.fail("参数不能为空");
        }
        List<Friends> friendsList = getListMethod(formUserId, toUserId);
        List<Friends> friendsList2 = getListMethod(toUserId, formUserId);
        friendsList.addAll(friendsList2);
        friendsList.sort(Comparator.comparing(Friends::getCreatedDate));
        return Result.succ("获取全部聊天记录成功", friendsList);
    }

    @Override
    public Result getTen(String formUserId, String toUserId, Long page, Long size) {
        if (StringUtils.isBlank(formUserId)) {
            return Result.fail("参数不能为空");
        }
        if (StringUtils.isBlank(toUserId)) {
            return Result.fail("参数不能为空");
        }
        if (page == 0 || null == page) {
            return Result.fail("参数不能为空");
        }
        if (size == 0 || null == page) {
            return Result.fail("参数不能为空");
        }
        List<Friends> tenList = friendsMapper.getTenList(formUserId, toUserId, (page - 1) * size, size);
        return Result.succ("获取聊天记录成功", tenList);
    }

    @Override
    public Result getFriendsAndUserVo(String token) {
        try {
            UserTokenVo userByToken = userService.findUserByToken(token);
            List<FriendsUser> listFriendsUser = friendsUserService.getListMethod(userByToken.getId());
            List<FriendsAndUserVo> friendsAndUserVos = copyListFriendsAndUserVo(listFriendsUser);
            return Result.succ("获取成功", friendsAndUserVos);
        } catch (Exception e) {
            log.error("FriendsServiceImlp的getFriendsAndUserVo出现错误" + e);
            return Result.fail(500, "系统出现错误,请反馈给问题" + e);
        }
    }

    public List<Friends> getListMethod(String formUserId, String toUserId) {
        LambdaQueryWrapper<Friends> friendsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        friendsLambdaQueryWrapper.eq(Friends::getFormUserId, formUserId).eq(Friends::getToUserId, toUserId)
                .orderByAsc(Friends::getCreatedDate);
        return friendsMapper.selectList(friendsLambdaQueryWrapper);
    }

    @Override
    public boolean deleteByUserId(String id, TransactionStatus transaction) {
        try {
            LambdaQueryWrapper<Friends> friendsLambdaQueryWrapper = new LambdaQueryWrapper<>();
            friendsLambdaQueryWrapper.eq(Friends::getFormUserId,id);
            friendsMapper.delete(friendsLambdaQueryWrapper);
            LambdaQueryWrapper<Friends> friendsLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            friendsLambdaQueryWrapper2.eq(Friends::getToUserId,id);
            friendsMapper.delete(friendsLambdaQueryWrapper2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public FriendsAndUserVo copyFriendsAndUserVo(User user, Friends friends) {
        FriendsAndUserVo friendsAndUserVo = new FriendsAndUserVo();
        friendsAndUserVo.setId(friends.getId());
        friendsAndUserVo.setToUserId(user.getId());
        friendsAndUserVo.setAvatar(user.getAvatar());
        friendsAndUserVo.setUsername(user.getUsername());
        friendsAndUserVo.setNickname(user.getNickname());
        friendsAndUserVo.setMsg(friends.getMsg());
        friendsAndUserVo.setCreateDate(friends.getCreatedDate());
        return friendsAndUserVo;
    }

    public List<FriendsAndUserVo> copyListFriendsAndUserVo(List<FriendsUser> listFriendsUser) {
        List<FriendsAndUserVo> friendsAndUserVos = new ArrayList<>();
        for (FriendsUser friendsUser : listFriendsUser) {
            String formUserId = friendsUser.getFormUserId();
            String toUserId = friendsUser.getToUserId();
            List<Friends> tenList = friendsMapper.getTenList(formUserId, toUserId, 0L, 1L);
            if (tenList.size() == 0) {
                continue;
            }
            Friends friends = tenList.get(0);
            User user = userService.getById(toUserId);
            FriendsAndUserVo friendsAndUserVo = copyFriendsAndUserVo(user, friends);
            friendsAndUserVos.add(friendsAndUserVo);
        }
        return friendsAndUserVos;
    }
}
