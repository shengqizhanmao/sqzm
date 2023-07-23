package com.lin.common.aop;


import com.alibaba.fastjson.JSON;
import com.lin.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Set;

/**
 * @author lin
 */
@Slf4j
@Component
@Aspect         //切面
public class CacheAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.lin.common.aop.CacheAnnotation)")    //定义切点
    public void pt() {
    }

    //环绕通知
    @Around("pt()")
    public Object around(@NotNull ProceedingJoinPoint joinPoint) {
        try {
            Signature signature = joinPoint.getSignature();
            //类名
            String className = joinPoint.getTarget().getClass().getSimpleName();
            //方法名
            String methodName = signature.getName();
            //参数类型列表，创建
            Class[] parameterTypes = new Class[joinPoint.getArgs().length];
            //参数列表
            Object[] args = joinPoint.getArgs();
            //参数
            String params = "";
            //循环将参数转换为String放入params中，并将参数类型放入parameterTypes
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes[i] = null;
                }
            }
            //如果参数不能为空，则进行加密
            if (StringUtils.isNoneEmpty(params)) {
                params = DigestUtils.md5Hex(params);
            }
            //获取方法
            Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            //获取方法的cacheAnnotation类
            CacheAnnotation cacheAnnotation = method.getAnnotation(CacheAnnotation.class);
            //获取过期时间
            long expire = cacheAnnotation.expire();
            //获取缓存名称
            String name = cacheAnnotation.name();
            //获取缓存列表
            String[] names = cacheAnnotation.names();
            //是否是修改
            boolean update = cacheAnnotation.update();
            //修改则删除全部缓存
            if (update) {
                Object proceed = joinPoint.proceed();
                for (String n : names) {
                    Set<String> keys = redisTemplate.keys(n + "::*");
                    for (String key : keys) {
                        redisTemplate.delete(key);
                    }
                    log.info("删除缓存...,{}", n);
                }
                if (StringUtils.isEmpty(name)) {
                    Set<String> keys = redisTemplate.keys(name + "::*");
                    for (String key : keys) {
                        redisTemplate.delete(key);
                        log.info("删除缓存...,{}", key);
                    }
                    log.info("删除缓存...,{}", name);
                }
                log.info("删除缓存...,{},{}", className, methodName);
                return proceed;
            }
            //设置缓存名称,获取缓存
            String redisKey = name + ":" + className + ":" + methodName + ":" + params;
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            //判断缓存是否存在
            if (StringUtils.isNoneEmpty(redisValue)) {
                log.info("走了缓存...,{},{}", className, methodName);
                return JSON.parseObject(redisValue, Result.class);
            }
            Object proceed = joinPoint.proceed();
            redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("存入缓存---,{},{}", className, methodName);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("缓存出现错误"+throwable);
            return Result.fail(500, "系统错误");
        }
    }
}
