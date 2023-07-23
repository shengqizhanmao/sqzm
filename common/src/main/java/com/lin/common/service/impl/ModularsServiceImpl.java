package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.ModularsMapper;
import com.lin.common.pojo.Modulars;
import com.lin.common.service.ModularsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-09
 */
@Service
public class ModularsServiceImpl extends ServiceImpl<ModularsMapper, Modulars> implements ModularsService {

    @Resource
    private ModularsMapper modularsMapper;

    @NotNull
    @Override
    public Result getModularsByPalteId(String palteId) {
        try {
            List<Modulars> modulars = getModularsByPalteIdMethod(palteId);
            return Result.succ("查询成功", modulars);
        } catch (Exception e) {
            log.error("ModularsServiceImpl:getModularsByPalteId:" + palteId + ",出现错误:" + e);
            return Result.fail(500, "服务器出现错误,请联系管理员");
        }
    }

    public List<Modulars> getModularsByPalteIdMethod(String palteId) {
        LambdaQueryWrapper<Modulars> modularsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        modularsLambdaQueryWrapper.eq(Modulars::getPalteId, palteId).orderByAsc(Modulars::getSort);
        return modularsMapper.selectList(modularsLambdaQueryWrapper);
    }
}
