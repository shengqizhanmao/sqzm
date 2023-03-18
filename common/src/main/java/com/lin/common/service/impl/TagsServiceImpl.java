package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.TagsMapper;
import com.lin.common.pojo.Tags;
import com.lin.common.service.TagsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService {

    @Resource
    TagsMapper tagsMapper;

    @Override
    public Result getByPalteId(String palteId) {
        List<Tags> byPalteIdMethod = getByPalteIdMethod(palteId);
        return Result.succ("查询标签成功", byPalteIdMethod);
    }

    public List<Tags> getByPalteIdMethod(String patleId) {
        LambdaQueryWrapper<Tags> tagsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagsLambdaQueryWrapper.eq(Tags::getPalteId, patleId);
        return tagsMapper.selectList(tagsLambdaQueryWrapper);
    }
}
