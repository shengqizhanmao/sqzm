package com.lin.common.mapper;

import com.lin.common.mapper.mapperX.BaseMapperX;
import com.lin.common.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-22
 */
@Mapper
public interface ArticleMapper extends BaseMapperX<Article> {

}
