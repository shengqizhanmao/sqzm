package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.mapper.BodyMapper;
import com.lin.common.pojo.Body;
import com.lin.common.service.BodyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-23
 */
@Service
public class BodyServiceImpl extends ServiceImpl<BodyMapper, Body> implements BodyService {

    @Resource
    private BodyMapper bodyMapper;

    @Override
    public Body getBodyByArticleId(String id) {
        LambdaQueryWrapper<Body> bodyLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bodyLambdaQueryWrapper.eq(Body::getArticleId, id);
        return bodyMapper.selectOne(bodyLambdaQueryWrapper);
    }
}
