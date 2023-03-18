package com.lin.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.mapper.PalteMapper;
import com.lin.common.pojo.Palte;
import com.lin.common.service.ModularsService;
import com.lin.common.service.PalteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-09
 */
@Service
public class PalteServiceImpl extends ServiceImpl<PalteMapper, Palte> implements PalteService {

    @Resource
    private PalteMapper palteMapper;

    @Resource
    private ModularsService modularsService;


}
