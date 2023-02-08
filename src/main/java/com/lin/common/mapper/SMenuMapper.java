package com.lin.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.common.pojo.SMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Mapper
@Repository
public interface SMenuMapper extends BaseMapper<SMenu> {

    List<SMenu> getListSMenuBySUserId(@Param("userId") String userId);
}
