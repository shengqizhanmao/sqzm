package com.lin.sqzmYxlt.controller;

import com.lin.common.Result;
import com.lin.common.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-22
 */
@RestController
@RequestMapping("/author")
public class AuthorController {

    @Resource
    private AuthorService authorService;

    @GetMapping("/isAuthor")
    public Result isAuthor(@RequestHeader("Authorization") String token) {
        return authorService.isAuthor(token);
    }

    @PostMapping("apply")
    public Result apply(@RequestHeader("Authorization") String token) {
        return authorService.apply(token);
    }
}
