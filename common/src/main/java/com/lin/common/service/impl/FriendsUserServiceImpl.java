package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.FriendsUserMapper;
import com.lin.common.pojo.FriendsUser;
import com.lin.common.pojo.User;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.pojo.Vo2.FriendsUsersAndUserVo;
import com.lin.common.service.FriendsUserService;
import com.lin.common.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-19
 */
@Service
public class FriendsUserServiceImpl extends ServiceImpl<FriendsUserMapper, FriendsUser> implements FriendsUserService {

    @Resource
    private FriendsUserMapper friendsUserMapper;

    @Resource
    private UserService userService;

    //获取好友信息
    @Override
    public Result getListFriendsUserVoByUserId(String userId) {
        try {
            List<FriendsUser> listFriendsUserByUserId = getListFriendsUserByUserId(userId, "1");
            if (listFriendsUserByUserId.isEmpty()) {
                return Result.succ("获取好友成功", null);
            }
            List<FriendsUsersAndUserVo> friendsUsersAndUserVos = copyFriendsUserList(listFriendsUserByUserId);
            return Result.succ("获取好友成功", friendsUsersAndUserVos);
        } catch (Exception e) {
            log.error("FriendsUserServiceImpl出现错误：" + e);
            return Result.fail("获取好友失败", e);
        }
    }

    //获取搜索用户名或者昵称的非好友
    @Override
    public Result getListSearchAddByUsernameOrNickname(String usernameOrNickname, String token) {
        if (StringUtils.isBlank(usernameOrNickname)) {
            return Result.fail("查询失败,无参数");
        }
        try {
            List<User> listUserByUsernameOrNickname = userService.getListUserByUsernameOrNickname(usernameOrNickname);
            //查询登录用户
            UserTokenVo userInfo = userService.findUserByToken(token);
            String userIdInfo = userInfo.getId();
            //删除搜索出来的登录用户,避免自己加自己
            Iterator<User> iterator = listUserByUsernameOrNickname.iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                String id = user.getId();
                if (userIdInfo.equals(id)) {
                    iterator.remove();
                }
            }
            //根据登录用户查询好友表
            List<FriendsUser> listFriendsUserByUserId = getListFriendsUserByUserId(userInfo.getId(), "1");
            //删除已添加的好友
            //使用迭代器的删除方法删除,因为迭代器删除一次要重新填充,我们以一个人添加了多少个人,就删除多少次
            for (int i = 0; i < listFriendsUserByUserId.size(); i++) {
                Iterator<FriendsUser> iteratorFriends = listFriendsUserByUserId.iterator();
                while (iteratorFriends.hasNext()) {
                    FriendsUser friendsUser = iteratorFriends.next();
                    String toUserId = friendsUser.getToUserId();
                    Iterator<User> iteratorUser = listUserByUsernameOrNickname.iterator();
                    while (iteratorUser.hasNext()) {
                        User user = iteratorUser.next();
                        String id = user.getId();
                        if (toUserId.equals(id)) {
                            iteratorUser.remove();
                        }
                    }
                }
            }
            if (listUserByUsernameOrNickname.size() == 0) {
                return Result.succ("查询成功,无用户");
            }
            return Result.succ("查询成功", listUserByUsernameOrNickname);
        } catch (Exception e) {
            log.error("FriendsUserServiceImpl:getListSearchAddByUsernameOrNickname出现错误" + e);
            return Result.fail("服务器错误,请反馈给问题:" + e);
        }
    }

    //申请好友
    @Override
    public Result add(String formUserId, String toUserId) {
        FriendsUser friendsUser = getMethod(formUserId, toUserId);
        if (friendsUser != null) {
            String status = friendsUser.getStatus();
            if (status.equals("1")) {
                Result.fail(400, "申请失败,已是你的好友了");
            }
            if (status.equals("0")) {
                Result.fail(400, "申请失败,已申请,对方未回应");
            }
            if (status.equals("-1")) {
                friendsUser.setStatus("0");
                int i = friendsUserMapper.updateById(friendsUser);
                if (i == 0) {
                    Result.fail(500, "服务器出现错误,请反馈给问题");
                }
                return Result.succ("申请好友成功");
            }
        }
        int i = addMethod(formUserId, toUserId);
        if (i == 0) {
            Result.fail(500, "服务器出现错误,请反馈给问题");
        }
        return Result.succ("申请好友成功");
    }

    //获取申请好友
    @Override
    public Result getFriendsUserApplyList(String userId) {
        try {
            List<FriendsUser> listFriendsUserByUserId = getListFriendsUserByUserId(userId, "0");
            List<FriendsUser> listFriendsUserByUserId2 = getListFriendsUserByUserId(userId, true, "0");
            if (listFriendsUserByUserId.isEmpty() && listFriendsUserByUserId2.isEmpty()) {
                return Result.succ("获取申请好友成功", null);
            }
            listFriendsUserByUserId.addAll(listFriendsUserByUserId2);
            List<FriendsUsersAndUserVo> friendsUsersAndUserVos = copyFriendsUserList(listFriendsUserByUserId);
            return Result.succ("获取申请好友成功", friendsUsersAndUserVos);
        } catch (Exception e) {
            log.error("FriendsUserServiceImpl出现错误：" + e);
            return Result.fail("获取申请好友失败", e);
        }
    }

    //同意好友和单方面删除好友
    @Override
    public Result updateStatus(String formUserId, String toUserId, String status) {
        //同意好友,status为1
        if (status.equals("1")) {
            FriendsUser formUser = getMethod(formUserId, toUserId);
            formUser.setStatus(status);
            friendsUserMapper.updateById(formUser);
            FriendsUser toUser = getMethod(toUserId, formUserId);
            if (toUser == null) {
                addMethod(toUserId, formUserId, status);
                return Result.succ("同意成功");
            }
            toUser.setStatus(status);
            friendsUserMapper.updateById(toUser);
            return Result.succ("同意成功");
        }
        //单方面删除好友
        if (status.equals("-1")) {
            FriendsUser formUser = getMethod(formUserId, toUserId);
            friendsUserMapper.deleteById(formUser);
            FriendsUser toUser = getMethod(toUserId, formUserId);
            if (toUser == null) {
                return Result.succ("删除成功");
            }
            toUser.setStatus("-1");
            friendsUserMapper.updateById(toUser);
            return Result.succ("删除成功");
        }
        return Result.fail(400, "status参数不能为0");
    }

    //获取被单方面删除好友的列表
    @Override
    public Result getListDetete(String userId) {
        List<FriendsUser> listFriendsUserByUserId = getListFriendsUserByUserId(userId, "-1");
        if (listFriendsUserByUserId.size() == 0) {
            return Result.succ("获取获取被删除好友列表成功");
        }
        List<FriendsUsersAndUserVo> friendsUsersAndUserVos = copyFriendsUserList(listFriendsUserByUserId);
        return Result.succ("获取被删除好友列表成功", friendsUsersAndUserVos);
    }

    //清除被单方面删除好友的列表

    @Override
    public Result delete(String formUserId, String toUserId) {
        FriendsUser method = getMethod(formUserId, toUserId);
        friendsUserMapper.deleteById(method);
        return Result.succ("删除成功");
    }

    @Override
    public boolean deleteByUserId(String id, TransactionStatus transaction) {
        try{
            LambdaQueryWrapper<FriendsUser> friendsUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            friendsUserLambdaQueryWrapper.eq(FriendsUser::getFormUserId,id);
            friendsUserMapper.delete(friendsUserLambdaQueryWrapper);
            LambdaQueryWrapper<FriendsUser> friendsUserLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            friendsUserLambdaQueryWrapper2.eq(FriendsUser::getToUserId,id);
            friendsUserMapper.delete(friendsUserLambdaQueryWrapper2);
            return true;
        }catch (Exception e){
            return false;
        }


    }

    public FriendsUser getMethod(String formUserId, String toUserId) {
        LambdaQueryWrapper<FriendsUser> friendsUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        friendsUserLambdaQueryWrapper.eq(FriendsUser::getFormUserId, formUserId).eq(FriendsUser::getToUserId, toUserId);
        FriendsUser friendsUser = friendsUserMapper.selectOne(friendsUserLambdaQueryWrapper);
        return friendsUser;
    }

    public int addMethod(String formUserId, String toUserId) {
        FriendsUser friendsUser = new FriendsUser();
        friendsUser.setFormUserId(formUserId);
        friendsUser.setToUserId(toUserId);
        friendsUser.setStatus("0");
        return friendsUserMapper.insert(friendsUser);
    }

    public int addMethod(String formUserId, String toUserId, String Status) {
        FriendsUser friendsUser = new FriendsUser();
        friendsUser.setFormUserId(formUserId);
        friendsUser.setToUserId(toUserId);
        friendsUser.setStatus(Status);
        return friendsUserMapper.insert(friendsUser);
    }

    public List<FriendsUser> getListFriendsUserByUserId(String userId, String status) {
        LambdaQueryWrapper<FriendsUser> friendsUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        friendsUserLambdaQueryWrapper.eq(FriendsUser::getFormUserId, userId).eq(FriendsUser::getStatus, status);
        List<FriendsUser> friendsUserList = friendsUserMapper.selectList(friendsUserLambdaQueryWrapper);
        return friendsUserList;
    }

    public List<FriendsUser> getListFriendsUserByUserId(String userId, Boolean formBecomesTo, String status) {
        LambdaQueryWrapper<FriendsUser> friendsUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (formBecomesTo) {
            friendsUserLambdaQueryWrapper.eq(FriendsUser::getToUserId, userId);
        } else {
            friendsUserLambdaQueryWrapper.eq(FriendsUser::getFormUserId, userId);
        }
        friendsUserLambdaQueryWrapper.eq(FriendsUser::getStatus, status);
        return friendsUserMapper.selectList(friendsUserLambdaQueryWrapper);
    }

    public List<FriendsUsersAndUserVo> copyFriendsUserList(List<FriendsUser> friendsUserList) {
        List<FriendsUsersAndUserVo> friendsUsersAndUserVos = new ArrayList<>();
        for (FriendsUser friendsUser : friendsUserList) {
            FriendsUsersAndUserVo friendsUsersAndUserVo = new FriendsUsersAndUserVo();
            User toUserById = userService.getById(friendsUser.getToUserId());
            User formUserById = userService.getById(friendsUser.getFormUserId());
            friendsUsersAndUserVo.setAvatar(toUserById.getAvatar());
            friendsUsersAndUserVo.setId(friendsUser.getId());
            friendsUsersAndUserVo.setNickname(toUserById.getNickname());
            friendsUsersAndUserVo.setStatus(friendsUser.getStatus());
            friendsUsersAndUserVo.setToUserId(friendsUser.getToUserId());
            friendsUsersAndUserVo.setFormUserId(friendsUser.getFormUserId());
            friendsUsersAndUserVo.setFormNickname(formUserById.getNickname());
            friendsUsersAndUserVo.setFormAvatar(formUserById.getAvatar());
            friendsUsersAndUserVos.add(friendsUsersAndUserVo);
        }
        return friendsUsersAndUserVos;
    }

    //获取好友
    public List<FriendsUser> getListMethod(String formUserId) {
        LambdaQueryWrapper<FriendsUser> friendsUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        friendsUserLambdaQueryWrapper.eq(FriendsUser::getFormUserId, formUserId).eq(FriendsUser::getStatus, "1");
        return friendsUserMapper.selectList(friendsUserLambdaQueryWrapper);
    }

}
