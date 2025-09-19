package com.bytefinger.toutuo.system;

import com.bytefinger.common.security.annotation.EnableCustomConfig;
import com.bytefinger.common.security.annotation.EnableRyFeignClients;
import com.bytefinger.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统模块
 *
 * @author patrick
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
