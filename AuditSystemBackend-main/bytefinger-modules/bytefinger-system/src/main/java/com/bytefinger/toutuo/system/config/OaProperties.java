package com.bytefinger.toutuo.system.config;

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
@ConfigurationProperties(prefix = "oa.notify")
public class OaProperties {

    private String todoUrl;

    private String detailUrl;

}
