package com.bytefinger.toutuo.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OSS配置类
 *
 * @Author: Jone Ying
 * @Date:  2020/12/2
**/
@Data
@Configuration
@ConfigurationProperties(prefix = "sso")
public class SsoProperties {

    private String clientId;

    private String clientSecret;

    private String callBackUrl;

    private String tokenUrl;

    private String checkTokenUrl;

    private String userInfoUrl;

    private String targetUrl;

    private String loginErrorUrl;

}
