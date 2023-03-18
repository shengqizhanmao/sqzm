package com.lin.common.mapper;

import com.lin.common.mapper.mapperX.BaseMapperX;
import com.lin.common.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
public interface MenuMapper extends BaseMapperX<Menu> {

}
