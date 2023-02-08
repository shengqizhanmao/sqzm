package com.lin.sqzmHtgl.mapper;

import com.lin.sqzmHtgl.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getListRoleBySUserId(@Param("sId")String sId);
}
