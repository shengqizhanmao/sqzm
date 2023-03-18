package com.lin.common.mapper.mapperX;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface BaseMapperX<T> extends BaseMapper<T> {
    default Page<T> selectPage(Long page, Long size, LambdaQueryWrapper<T> lambdaQueryWrapper) {
        Page<T> newPage = new Page<>(page, size);
        return this.selectPage(newPage, lambdaQueryWrapper);
    }

    default Page<T> selectPage(Long page, Long size) {
        return this.selectPage(page, size, null);
    }
}

