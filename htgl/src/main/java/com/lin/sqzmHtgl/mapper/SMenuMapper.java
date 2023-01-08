package com.lin.sqzmHtgl.mapper;

import com.lin.sqzmHtgl.pojo.SMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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

    List<SMenu> getListSMenuBySUserId(String userId);
}
