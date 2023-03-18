package com.lin.sqzmHtgl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lin
 */
@MapperScan("com.lin.common.mapper")
@SpringBootApplication(scanBasePackages = {"com.lin.sqzmHtgl", "com.lin.common"})
public class SqzmHtglApplication {
    public static void main(String[] args) {
        SpringApplication.run(SqzmHtglApplication.class, args);
    }
}
