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
import org.springframework.web.bind.MissingRequestHeaderException;
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

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public Result handler(MissingRequestHeaderException e) {
        return Result.fail(ResultCode.FAIL, "未登录,请登录");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NullPointerException.class)
    public Result handler(NullPointerException e) {
        return Result.fail(ResultCode.PERMISSION_INSUFFICIENT, "权限不够,请联系管理员");
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public Result handler(AuthenticationException e) {
        return Result.fail(ResultCode.TOKEN_OVERDUE, "token过期了,请重新登录");
    }

    @ExceptionHandler(value = UnknownAccountException.class)
    public Result handler(UnknownAccountException e) {
        return Result.fail(ResultCode.TOKEN_OVERDUE, "token过期了,请重新登录");
    }

    @ExceptionHandler(value = DisabledAccountException.class)
    public Result handler(DisabledAccountException e) {
        return Result.fail(ResultCode.USER_DISABLE, "帐号被禁用");
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public Result handler(ExpiredJwtException e) {
        return Result.fail(ResultCode.TOKEN_OVERDUE, "jwt过期,请重新登录");
    }


    @ExceptionHandler(value = AuthorizationException.class)
    public Result handler(AuthorizationException e) {
        return Result.fail(ResultCode.PERMISSION_INSUFFICIENT, "权限不够,请联系管理员" + e);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result messageExceptionHandler(HttpMessageNotReadableException e) {
        return Result.fail(ResultCode.HTTP_REQUEST_PARAMETER_EXCEPTION, "http请求参数转换异常" + e);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result handler(MissingServletRequestParameterException e) {
        return Result.fail(ResultCode.PARAMETER_ERROR, "请求参数格式错误" + e);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        return Result.fail(ResultCode.PARAMETER_ERROR, "请求参数格式错误" + e);
    }
}