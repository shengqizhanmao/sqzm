package com.lin.sqzmYxlt.service.impl;

import com.lin.sqzmYxlt.pojo.Resource;
import com.lin.sqzmYxlt.mapper.ResourceMapper;
import com.lin.sqzmYxlt.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    ResourceMapper resourceMapper;
    @Override
    public List<Resource> getListResourceByUserId(String id){
        List<Resource> listResourceByUserId = resourceMapper.getListResourceByUserId(id);
        return listResourceByUserId;
    }
}
