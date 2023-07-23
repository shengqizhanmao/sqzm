package com.lin.sqzmHtgl.controller;

import com.lin.common.Result;
import com.lin.common.service.SUserService;
import com.lin.common.service.impl.SUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SUserControllerTest {

    @Resource
    SUserController sUserController;

    @Resource
    SUserService sUserService;

    @Test
    void get() {
        Result result = sUserService.get(10L, 1L);
        System.out.println(result);
    }
}