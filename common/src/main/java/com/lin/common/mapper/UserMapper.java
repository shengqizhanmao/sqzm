package com.lin.common.mapper;


import com.lin.common.mapper.mapperX.BaseMapperX;
import com.lin.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapperX<User> {

    List<User> selectListUserByUsernameOrNickname(@Param("usernameOrNickname") String usernameOrNickname);
}
