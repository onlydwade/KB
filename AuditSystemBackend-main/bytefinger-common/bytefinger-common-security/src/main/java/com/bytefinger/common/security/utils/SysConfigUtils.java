package com.bytefinger.common.security.utils;

import com.bytefinger.common.core.constant.CacheConstants;
import com.bytefinger.common.core.utils.SpringUtils;
import com.bytefinger.common.redis.service.RedisService;

/**
 * 系统配置工具
 *
 * @author pat
 * @date 2022/10/10 13:56
 **/
public class SysConfigUtils {


    /**
     * 获取配置缓存
     *
     * @param key 参数键
     */
    public static String getConfigCache(String key) {
        return SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(key));
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

}
