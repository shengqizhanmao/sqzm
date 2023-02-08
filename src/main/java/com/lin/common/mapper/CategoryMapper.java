package com.lin.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.common.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
@Repository
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
