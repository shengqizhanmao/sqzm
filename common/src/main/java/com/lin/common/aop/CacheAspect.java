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
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = signature.getName();
            Class[] parameterTypes = new Class[joinPoint.getArgs().length];
            Object[] args = joinPoint.getArgs();
            String params = "";
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes[i] = null;
                }
            }
            if (StringUtils.isNoneEmpty(params)) {
                params = DigestUtils.md5Hex(params);
            }
            Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            CacheAnnotation cacheAnnotation = method.getAnnotation(CacheAnnotation.class);
            //获取过期时间
            long expire = cacheAnnotation.expire();
            //获取缓存名称
            String name = cacheAnnotation.name();
            String[] names = cacheAnnotation.names();
            boolean update = cacheAnnotation.update();
            if (update) {
                Object proceed = joinPoint.proceed();
                if (names.length != 0) {
                    for (String n : names) {
                        Set<String> keys = redisTemplate.keys(n + "::*");
                        for (String key : keys) {
                            redisTemplate.delete(key);
                        }
                        log.info("删除缓存...,{}", n);
                    }
                }
                if (!name.equals("")) {
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
            log.info("存入缓存---{},{}", className, methodName);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(500, "系统错误");
    }
}
