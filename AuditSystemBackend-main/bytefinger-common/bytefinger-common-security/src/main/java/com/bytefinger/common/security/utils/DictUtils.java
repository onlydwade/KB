package com.bytefinger.common.security.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.TypeReference;
import com.bytefinger.common.core.constant.CacheConstants;
import com.bytefinger.common.core.utils.ReflectionUtils;
import com.bytefinger.common.core.utils.SpringUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.redis.service.RedisService;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.toutuo.api.system.core.domain.SysDictData;
import com.bytefinger.toutuo.api.system.core.domain.SysDictType;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典工具类
 *
 * @author patrick
 */
public class DictUtils {

    private static String ALL = "ALL";

    private static String PARENT = "PARENT_";

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysDictData> getDictCache(String key) {
        JSONArray arrayCache = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(arrayCache)) {
            return arrayCache.toList(SysDictData.class);
        }
        return null;
    }

    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas) {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysDictType> getDictPtypeCache(String key) {
        JSONArray arrayCache = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(PARENT + key));
        if (StringUtils.isNotNull(arrayCache)) {
            return arrayCache.toList(SysDictType.class);
        }
        return null;
    }

    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictPtypeCache(String key, List<SysDictType> dictDatas) {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(PARENT + key), dictDatas);
    }

    /**
     * 获取全部
     *
     * @return
     */
    public static Map<String, List<SysDictData>> getDictCacheAll() {
        String jsonStr = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(ALL));
        if (StringUtils.isNotNull(jsonStr)) {
            try {
                return JSON.parseObject(jsonStr, new TypeReference<HashMap<String, List<SysDictData>>>() {
                });
            } catch (Exception e) {

            }
        }

        return null;
    }

    /**
     * 保存全部
     *
     * @param dict
     */
    public static void setDictCacheAll(Map<String, List<SysDictData>> dict) {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(ALL), JSON.toJSONString(dict));
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        Collection<String> keys = SpringUtils.getBean(RedisService.class).keys(CacheConstants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(RedisService.class).deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }

    /**
     * 获取字典指定值
     *
     * @param type
     * @param value
     * @return
     */
    public static String getDictDataOptions(String type, String value) {

        if (StringUtils.isNull(value)) {
            return null;
        }

        List<SysDictData> dictCache = getDictCache(type);
        if (null == dictCache || dictCache.isEmpty()) {
            return null;
        }

        for (SysDictData sysDictData : dictCache) {
            if (StringUtils.isNotNull(sysDictData.getDictValue()) && sysDictData.getDictValue().equals(value)) {
                return sysDictData.getDictLabel();
            }
        }

        return null;
    }

    /**
     * 获取字典指定值
     *
     * @param type
     * @param value
     * @return
     */
    public static SysDictData getDictData(String type, String value) {

        if (StringUtils.isNull(value)) {
            return null;
        }

        List<SysDictData> dictCache = getDictCache(type);
        if (null == dictCache || dictCache.isEmpty()) {
            return null;
        }

        for (SysDictData sysDictData : dictCache) {
            if (StringUtils.isNotNull(sysDictData.getDictValue()) && sysDictData.getDictValue().equals(value)) {
                return sysDictData;
            }
        }

        return null;
    }

    /**
     * 获取字典指定值
     *
     * @param type
     * @param value
     * @return
     */
    public static SysDictData getDictDataBycc(String type, String value) {

        if (StringUtils.isNull(value)) {
            return null;
        }

        List<SysDictData> dictCache = getDictCache(type);
        if (null == dictCache || dictCache.isEmpty()) {
            return null;
        }

        for (SysDictData sysDictData : dictCache) {
            if (StringUtils.isNotNull(sysDictData.getCssClass()) && sysDictData.getCssClass().equals(value)) {
                return sysDictData;
            }
        }

        return null;
    }

    /**
     * 获取字典指定值
     *
     * @param ptype
     * @param value
     * @return
     */
    public static String getDictPdataOptions(String ptype, String value) {

        if (StringUtils.isNull(value)) {
            return null;
        }

        List<SysDictType> dictCache = getDictPtypeCache(ptype);
        if (null == dictCache || dictCache.isEmpty()) {
            return null;
        }

        String res = null;
        for (SysDictType sysDictType : dictCache) {
            res = getDictDataOptions(sysDictType.getDictType(), value);
            if (null != res) {
                return res;
            }
        }

        return res;
    }

    /**
     * 通过指定
     *
     * @param obj
     * @param fieldName
     * @param dictEnum
     * @return
     */
    public static String getDictValue4Field(Object obj, String fieldName, String dictEnum) {
        String reValue = dictEnum;
        Field field = ReflectionUtils.getDeclaredField(obj, fieldName);
        if (null != field) {
            Dict dict = field.getAnnotation(Dict.class);
            if (null != dict && StringUtils.isNotEmpty(dict.type())) {
                reValue = getDictDataOptions(dict.type(), dictEnum);
            }
        }

        return reValue;
    }

    /**
     * 通过指定
     *
     * @param filed
     * @param dictEnum
     * @return
     */
    public static String getValue4Field(Field filed, Dict dict, String dictEnum) {
        if (null != filed && StringUtils.isNotNull(dictEnum)) {
            if (null != dict && StringUtils.isNotEmpty(dict.type())) {
                String reValue = getDictDataOptions(dict.type(), dictEnum);
                return StringUtils.isNotNull(reValue) ? reValue : "";
            }
        }

        return "";
    }

}
