package com.bytefinger.toutuo.system.config;

import com.bytefinger.common.core.utils.OssUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * OSS配置注入
 *
 * @Author: Jone Ying
 * @Date:  2020/12/2
**/
@Configuration
public class OssConfig implements CommandLineRunner {

    @Resource
    private OssProperties properties;

    @Override
    public void run(String... args) {
        OssUtil.init(properties.getAccessKeyId(), properties.getAccessKeySecret(),
                properties.getEndpoint(), properties.getUrl(), properties.getPrefix(), properties.getBucketName());
    }
}
