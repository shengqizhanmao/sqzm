package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.pojo.Body;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-23
 */
public interface BodyService extends IService<Body> {
    Body getBodyByArticleId(String id);
}
