package com.lin.common.shiro.Jwt;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lin.common.Result;
import com.lin.common.ResultCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lin
 */
@Slf4j
@Component("jwtFilter")
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 执行登录认证
     */
    @SneakyThrows
    @Override
    //这个方法叫做尝试进行登录的操作,如果token存在,那么进行提交登录,如果不存在说明可能是正在进行登录或者做其它的事情 直接放过即可
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String token = httpServletRequest.getHeader("Authorization");
            //判断token是否为空
            if (StringUtils.isBlank(token)) {
                return true;
            }
            JwtToken jwtToken = new JwtToken(token);
            Subject subject = SecurityUtils.getSubject();
            subject.login(jwtToken);
            return true;
        }catch (UnknownAccountException e){
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setStatus(ResultCode.TOKEN_OVERDUE);
            Result fail = Result.fail(ResultCode.TOKEN_OVERDUE, "登录过期,请重新登录");
            httpServletResponse.getWriter().write(JSON.toJSONString(fail, SerializerFeature.WriteDateUseDateFormat));
            return false;
        } catch (Exception e) {
            log.info("JwtFilter-isAccessAllowed的错误,错误原因:"+e);
            return false;
        }
    }
    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.SC_OK);
            return false;
        }
        return super.preHandle(request, response);
    }
}