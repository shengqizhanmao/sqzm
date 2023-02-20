package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Modulars;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-09
 */
public interface ModularsService extends IService<Modulars> {

    Result getModularsByPalteId(String palteId);
}
