package com.lin.common.mapper;

import com.lin.common.mapper.mapperX.BaseMapperX;
import com.lin.common.pojo.Friends;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendsMapper extends BaseMapperX<Friends> {

    List<Friends> getTenList(@Param("formUserId") String formUser, @Param("toUserId") String toUserId, @Param("one") Long one, @Param("two") Long two);
}
