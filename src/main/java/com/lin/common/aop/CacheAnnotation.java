package com.lin.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lin
 * 作用:缓存redis
 * 参数:expire:过期时间,name:名称
 */
@Target({ElementType.METHOD})  //注解放置的目标位置,TYPE代表放类上,Method代表放在方法上,
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
public @interface CacheAnnotation {
    long expire() default 1 * 60 * 1000; //1分钟

    String name() default "";

    String[] names() default {};

    boolean update() default false;
}
