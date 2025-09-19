package com.bytefinger.toutuo.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "azure")
public class StorageProperties {

    private String connectionString;
    // Blob容器名称
    private String containerName;

    private String prefix;


}
