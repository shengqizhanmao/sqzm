package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.ResourceMapper;
import com.lin.common.pojo.Resource;
import com.lin.common.pojo.Vo.ResourceVo;
import com.lin.common.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public List<Resource> getListResourceByUserId(String id) {
        return resourceMapper.getListResourceByUserId(id);
    }

    public List<Resource> getListResourceBySUserId(String id) {
        return resourceMapper.getListResourceBySUserId(id);
    }

    @Override
    public List<Resource> getListResourceByRoleId(String id) {
        return resourceMapper.getListResourceByRoleId(id);
    }

    @NotNull
    @Override
    public Result getListResourceByTypeResources() {
        LambdaQueryWrapper<Resource> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Resource::getResourceName);
        lambdaQueryWrapper.eq(Resource::getResourceType, "资源");
        List<Resource> resources = resourceMapper.selectList(lambdaQueryWrapper);
        return Result.succ("查询资源类型成功", resources);
    }

    @Override
    public Result getListResourceVo() {
        LambdaQueryWrapper<Resource> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Resource::getResourceType, "目录");
        lambdaQueryWrapper.orderByAsc(Resource::getSortNo);
        List<Resource> resources = resourceMapper.selectList(lambdaQueryWrapper);
        List<ResourceVo> resourceVos = ListCopy(resources);
        return Result.succ("查询成功", resourceVos);
    }


    /*
    * @Param  Resource resource;资源的实体类
    * */
    @NotNull
    @Override
    public Result addResource(@NotNull Resource resource) {
        String resourceType = resource.getResourceType();
        if (StringUtils.isEmpty(resourceType)) {
            return Result.fail("添加失败,类型不能为空");
        }
        if (resourceType.equals("目录")) {
            resource.setIsLeaf("1");
            resource.setParentId("1");
        } else {
            resource.setIsLeaf("0");
        }
        resource.setIsSystemRoot("0");
        resource.setEnableFlag("1");
        int insert = resourceMapper.insert(resource);
        if (insert == 0) {
            return Result.fail("添加失败");
        }
        return Result.succ("添加成功");
    }

    @NotNull
    @Override
    public Result updateResource(@NotNull Resource resource) {
        int i = resourceMapper.updateById(resource);
        if (StringUtils.isEmpty(resource.getResourceType())) {
            return Result.fail("添加失败,类型不能为空");
        }
        if (resource.getResourceType().equals("目录")) {
            resource.setIsLeaf("1");
            resource.setParentId("1");
        } else {
            resource.setIsLeaf("0");
        }
        if (i == 0) {
            return Result.fail("修改失败,不能与之前一样");
        }
        return Result.succ("修改成功");
    }

    @Override
    public Result deleteResourceById(String id) {
        int i = resourceMapper.deleteById(id);
        if (i == 0) {
            Result.fail("删除失败,不存在");
        }
        return Result.succ("删除成功");
    }

    //Method
    @NotNull
    private ResourceVo Copy(@NotNull Resource resource) {
        ResourceVo resourceVo = new ResourceVo();
        BeanUtils.copyProperties(resource, resourceVo);
        return resourceVo;
    }

    @NotNull
    private List<ResourceVo> ListCopy(@NotNull List<Resource> resources) {
        List<ResourceVo> ResourceVos = new ArrayList<>();
        for (Resource resource : resources) {
            ResourceVo copy = Copy(resource);
            ResourceVo select = Select(copy);   //判断有无子节点
            ResourceVos.add(select);
        }
        return ResourceVos;
    }

    @NotNull
    private ResourceVo Select(@NotNull ResourceVo resourceVo) {
        if (resourceVo.getIsLeaf().equals("1")) { //判断有无子节点
            LambdaQueryWrapper<Resource> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Resource::getParentId, resourceVo.getId());
            lambdaQueryWrapper.eq(Resource::getIsSystemRoot, "0");//不能是根节点
            lambdaQueryWrapper.orderByAsc(Resource::getSortNo);
            List<Resource> resources = resourceMapper.selectList(lambdaQueryWrapper);
            resourceVo.setListResourceLeaf(resources);
        }
        return resourceVo;
    }
}
