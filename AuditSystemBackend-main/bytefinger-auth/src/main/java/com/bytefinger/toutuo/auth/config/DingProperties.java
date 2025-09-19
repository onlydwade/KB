package com.bytefinger.toutuo.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 钉钉
 *
 * @Author: chengwei
 * @Date:  2023/12/20
**/
@Data
@Configuration
@ConfigurationProperties(prefix = "dingtalk")
public class DingProperties {

    private String appkey;

    private String appsecret;

    private String corpid;

    private String tokenURL;

    private String loginURL;

    private String userURL;
}
