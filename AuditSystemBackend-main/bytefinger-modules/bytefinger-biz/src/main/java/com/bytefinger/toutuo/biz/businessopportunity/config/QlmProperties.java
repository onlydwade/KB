package com.bytefinger.toutuo.biz.businessopportunity.config;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * qlm配置类
 *
 * @Author: chengwei
 * @Date:  2024/1/2
**/
@Data
@Configuration
@ConfigurationProperties(prefix = "qlm")
public class QlmProperties {

    private String publicKey;

    private String system;

    private String realTimeListUrl;

    private String realTimeDetailUrl;

    private String stockListUrl;

    private String stockDetailUrl;

    private String openCodeUrl;

    private String companyUrl;


    private Integer unlockBussinessAllCount;
    private Integer unlockStockAllCount;
    private Integer unlockCompanyAllCount;
}
