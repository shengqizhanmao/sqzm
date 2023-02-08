package com.lin.sqzmYxlt.mapper;

import com.lin.sqzmYxlt.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
@Mapper
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

}
