package com.lin.sqzmHtgl.mapper;

import com.lin.sqzmHtgl.pojo.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Mapper
@Repository
public interface ResourceMapper extends BaseMapper<Resource> {
    List<Resource> getListResourceBySUserId(@Param("id")String id);
    List<Resource> getListResourceByRoleId(@Param("id")String id);
}
