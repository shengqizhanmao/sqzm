package com.lin.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.mapper.CategoryMapper;
import com.lin.common.pojo.Category;
import com.lin.common.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
