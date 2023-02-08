package com.lin.sqzmYxlt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lin
 */
@MapperScan("com.lin.common.mapper")
@SpringBootApplication(scanBasePackages = {"com.lin.sqzmYxlt","com.lin.common"})
public class SqzmYxltApplication {
    public static void main(String[] args) {
        SpringApplication.run(SqzmYxltApplication.class, args);
    }
}
