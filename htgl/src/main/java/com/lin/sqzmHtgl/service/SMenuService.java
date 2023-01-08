package com.lin.sqzmHtgl.service;

import com.lin.sqzmHtgl.common.Result;
import com.lin.sqzmHtgl.pojo.SMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface SMenuService extends IService<SMenu> {
    Result getSMenuBySUserId(String token);
}
