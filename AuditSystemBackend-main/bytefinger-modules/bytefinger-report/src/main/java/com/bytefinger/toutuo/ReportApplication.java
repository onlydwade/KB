package com.bytefinger.toutuo;

import com.bytefinger.common.security.annotation.EnableCustomConfig;
import com.bytefinger.common.security.annotation.EnableRyFeignClients;
import com.bytefinger.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@EnableAsync
public class ReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class, args);
    }
}
