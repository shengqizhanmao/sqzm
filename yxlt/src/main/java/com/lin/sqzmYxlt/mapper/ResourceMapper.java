package com.lin.sqzmYxlt.mapper;

import com.lin.sqzmYxlt.pojo.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
@Repository
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> getListResourceByUserId(String id);
}
