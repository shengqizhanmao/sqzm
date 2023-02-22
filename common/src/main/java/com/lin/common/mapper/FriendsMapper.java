package com.lin.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.common.pojo.Friends;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendsMapper extends BaseMapper<Friends> {

    List<Friends> getTenList(@Param("formUserId") String formUser,@Param("toUserId") String toUserId,@Param("one")Long one,@Param("two")Long two);
}
