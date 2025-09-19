package com.bytefinger.common.security.utils;

import com.alibaba.fastjson2.JSONArray;
import com.bytefinger.common.core.constant.CacheConstants;
import com.bytefinger.common.core.utils.SpringUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.redis.service.RedisService;
import com.bytefinger.common.security.annotation.Cnarea;
import com.bytefinger.toutuo.api.system.core.domain.vo.AreaListVo;
import com.bytefinger.toutuo.api.system.core.domain.vo.CnareaVO;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * 工具类
 *
 * @author patrick
 */
public class CnareaUtils {

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<AreaListVo> getMapCache(String key) {
        JSONArray arrayCache = SpringUtils.getBean(RedisService.class).getCacheObject(CacheConstants.SYS_CNAREA_MAP_KEY + key);
        if (StringUtils.isNotNull(arrayCache)) {
            return arrayCache.toList(AreaListVo.class);
        }
        return null;
    }

    /**
     * 设置字典缓存
     *
     * @param key  参数键
     * @param data 字典数据列表
     */
    public static void setMapCache(String key, List<AreaListVo> data) {
        SpringUtils.getBean(RedisService.class).setCacheObject(CacheConstants.SYS_CNAREA_MAP_KEY + key, data);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static CnareaVO getCache(String key) {
        CnareaVO objCache = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(key));
        return objCache;
    }

    /**
     * 设置字典缓存
     *
     * @param key  参数键
     * @param data 字典数据列表
     */
    public static void setCache(String key, CnareaVO data) {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(key), data);
    }

    public static boolean exists() {
        return !SpringUtils.getBean(RedisService.class).keys(CacheConstants.SYS_CNAREA_KEY + "*").isEmpty();
    }

    public static void clearCache() {
        Collection<String> keys = SpringUtils.getBean(RedisService.class).keys(CacheConstants.SYS_CNAREA_KEY + "*");
        SpringUtils.getBean(RedisService.class).deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return CacheConstants.SYS_CNAREA_KEY + configKey;
    }

    /**
     * 获取指定值
     *
     * @param type
     * @param value
     * @return
     */
    public static String getOptions(String type, String value) {
        if (StringUtils.isNull(value)) {
            return null;
        }

        CnareaVO obj = getCache(type + value);
        if (null == obj) {
            return null;
        } else {
            return obj.getName();
        }
    }

    /**
     * 通过指定
     *
     * @param filed
     * @param dictEnum
     * @return
     */
    public static String getValue4Field(Field filed, Cnarea cnarea, String dictEnum) {
        if (null != filed && StringUtils.isNotNull(dictEnum)) {
            String reValue = getOptions(cnarea.type().name(), dictEnum);
            return StringUtils.isNotNull(reValue) ? reValue : "";
        }

        return "";
    }

}