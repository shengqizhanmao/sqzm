package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.CategoryMapper;
import com.lin.common.pojo.Category;
import com.lin.common.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Override
    public Result getByPalteId(String palteId) {
        List<Category> byPalteIdMethod = getByPalteIdMethod(palteId);
        return Result.succ("获取分类成功", byPalteIdMethod);
    }

    public List<Category> getByPalteIdMethod(String palteId) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getPalteId, palteId);
        return categoryMapper.selectList(categoryLambdaQueryWrapper);
    }
}
