package com.lin.sqzmHtgl.service.impl;

import com.lin.sqzmHtgl.pojo.User;
import com.lin.sqzmHtgl.mapper.UserMapper;
import com.lin.sqzmHtgl.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
