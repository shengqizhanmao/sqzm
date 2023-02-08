package com.lin.sqzmYxlt.service;

import com.lin.sqzmYxlt.pojo.Resource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
public interface ResourceService extends IService<Resource> {

    List<Resource> getListResourceByUserId(String id);
}
