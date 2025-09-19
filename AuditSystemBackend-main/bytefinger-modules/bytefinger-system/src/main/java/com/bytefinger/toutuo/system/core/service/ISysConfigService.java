package com.bytefinger.toutuo.system.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.api.system.core.domain.SysConfig;

/**
 * <p>
 * 参数配置表 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-10
 */
public interface ISysConfigService extends IService<SysConfig> {

    /**
     * 重置参数缓存数据
     */
    void resetConfigCache();

    void setConfig(String key, String val);
}
