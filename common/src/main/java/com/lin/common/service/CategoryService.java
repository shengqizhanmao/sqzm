package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Category;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
public interface CategoryService extends IService<Category> {

    Result getByPalteId(String palteId);
}
