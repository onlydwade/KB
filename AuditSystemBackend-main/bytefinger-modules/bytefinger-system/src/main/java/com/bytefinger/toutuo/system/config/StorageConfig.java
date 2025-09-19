package com.bytefinger.toutuo.system.config;


import com.bytefinger.common.core.utils.AzureUtil;
import com.bytefinger.common.core.utils.OssUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class StorageConfig implements CommandLineRunner {

    @Resource
    private StorageProperties properties;

    @Override
    public void run(String... args) {
        AzureUtil.init(properties.getConnectionString(), properties.getContainerName(), properties.getPrefix());
    }
}
