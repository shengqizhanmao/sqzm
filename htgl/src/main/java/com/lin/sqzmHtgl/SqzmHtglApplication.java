package com.lin.sqzmHtgl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lin
 */
@SpringBootApplication(scanBasePackages = {"com.lin.sqzmHtgl","com.lin.common"})
public class SqzmHtglApplication {
    public static void main(String[] args) {
        SpringApplication.run(SqzmHtglApplication.class, args);
    }
}
