package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Menu;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface MenuService extends IService<Menu> {

    Result getMenuPage(Long page, Long size);
}
