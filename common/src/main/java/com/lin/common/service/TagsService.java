package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Tags;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
public interface TagsService extends IService<Tags> {


    Result getByPalteId(String palteId);

    List<Tags> getByPalteIdMethod(String patleId);
}
