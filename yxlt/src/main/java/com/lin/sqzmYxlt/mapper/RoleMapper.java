package com.lin.sqzmYxlt.mapper;

import com.lin.sqzmYxlt.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
@Repository
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}
