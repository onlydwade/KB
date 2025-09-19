package com.bytefinger.toutuo;

import com.bytefinger.common.security.annotation.EnableCustomConfig;
import com.bytefinger.common.security.annotation.EnableRyFeignClients;
import com.bytefinger.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 业务模块
 *
 * @author patrick
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
public class BizApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizApplication.class, args);
    }

}
