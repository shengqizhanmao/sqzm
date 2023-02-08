package com.lin.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.common.pojo.RoleResource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 角色资源表 Mapper 接口
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Mapper
@Repository
public interface RoleResourceMapper extends BaseMapper<RoleResource> {

}
