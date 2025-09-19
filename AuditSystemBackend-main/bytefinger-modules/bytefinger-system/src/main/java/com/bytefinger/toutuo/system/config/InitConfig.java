package com.bytefinger.toutuo.system.config;

import com.bytefinger.toutuo.system.core.service.ISysCnareaService;
import com.bytefinger.toutuo.system.core.service.ISysConfigService;
import com.bytefinger.toutuo.system.core.service.ISysDictTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 初始化配置
 *
 * @author pat
 * @date 2022/10/10 13:50
 **/
@Component
@AllArgsConstructor
public class InitConfig {

    private final ISysConfigService sysConfigService;

    private final ISysDictTypeService dictTypeService;

    private final ISysCnareaService cnareaService;

    @PostConstruct
    public void doConstruct() throws Exception {

        sysConfigService.resetConfigCache();

        dictTypeService.resetDictCache();

        cnareaService.setCache();

        cnareaService.listProvinceCity();

        cnareaService.listProvinceCityArea();
    }

}
