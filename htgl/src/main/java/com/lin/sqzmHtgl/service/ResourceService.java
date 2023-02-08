package com.lin.sqzmHtgl.service;

import com.lin.common.Result;
import com.lin.sqzmHtgl.pojo.Resource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
public interface ResourceService extends IService<Resource> {
    List<Resource> getListResourceBySUserId(String id);
    List<Resource> getListResourceByRoleId(String id);
    Result getListResourceVo();
    Result getListResourceByTypeResources();
    Result addResource(Resource resource);
    Result updateResource(Resource resource);
    Result deleteResourceById(String id);
}
