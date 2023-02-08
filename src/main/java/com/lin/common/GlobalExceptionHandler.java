package com.lin.common;

/**
 * @author lin
 */

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler(value = AccessDeniedException.class)
//    public Result handler(AccessDeniedException e) {
//        return Result.fail(200001,"权限不足");
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    public Result handler(MethodArgumentNotValidException e) {
//        log.info("实体校验异常：----------------{}", e.getMessage());
//        return Result.fail(200002,"实体效验异常");
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = IllegalArgumentException.class)
//    public Result handler(IllegalArgumentException e) {
//        log.error("Assert异常：----------------{}", e.getMessage());
//        return Result.fail(200003,"Assert异常");
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NullPointerException.class)
    public Result handler(NullPointerException e) {
        return Result.fail(403,"权限不够,请联系管理员");
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public Result handler(AuthenticationException e) {
        return Result.fail(403, "token过期了,请重新登录");
    }

    @ExceptionHandler(value =   UnknownAccountException.class)
    public Result handler(UnknownAccountException e) {
        return Result.fail(403, "token过期了,请重新登录");
    }
    @ExceptionHandler(value = DisabledAccountException.class)
    public Result handler(DisabledAccountException e) {
        return Result.fail(402, "帐号被禁用");
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public Result handler(ExpiredJwtException e) {
        return Result.fail(403,"jwt过期,请重新登录");
    }


    @ExceptionHandler(value = AuthorizationException.class)
    public Result handler(AuthorizationException e) {
        return Result.fail(403,"权限不够,请联系管理员");
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result messageExceptionHandler(HttpMessageNotReadableException e) {
        return Result.fail(403,"http请求参数转换异常");
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result handler(MissingServletRequestParameterException e) {
        return Result.fail(402,"参数格式错误");
    }

}