package com.bytefinger.toutuo.biz.oa.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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
@ConfigurationProperties(prefix = "oa")
public class OaProperties {

    private String approvalUrl;

    private String approvalUpdateUrl;

    private String oaDetailUrl;

    private String todoUrl;

    private String todoDoneUrl;

    private String thUrl;

    private String customerUrl;

    private String ruleDetailUrl;

    private String deleteTodoUrl;

}
