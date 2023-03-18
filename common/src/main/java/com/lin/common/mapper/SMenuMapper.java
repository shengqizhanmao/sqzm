package com.lin.common.mapper;

import com.lin.common.mapper.mapperX.BaseMapperX;
import com.lin.common.pojo.SMenu;
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
@Repository
public interface SMenuMapper extends BaseMapperX<SMenu> {

    List<SMenu> getListSMenuBySUserId(@Param("userId") String userId);

}
