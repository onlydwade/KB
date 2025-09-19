package com.bytefinger.toutuo.system.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.redis.service.RedisService;
import com.bytefinger.common.security.utils.SysConfigUtils;
import com.bytefinger.toutuo.api.system.core.domain.SysConfig;
import com.bytefinger.toutuo.system.core.mapper.SysConfigMapper;
import com.bytefinger.toutuo.system.core.service.ISysConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-10
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    private final RedisService redisService;

    @Override
    public void resetConfigCache() {
        List<SysConfig> configsList = list();
        for (SysConfig config : configsList) {
            log.debug("系统配置：{}->{}", config.getConfigKey(), config.getConfigValue());
            redisService.setCacheObject(SysConfigUtils.getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    @Override
    public void setConfig(String key, String val){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("config_key",key);
        SysConfig config = this.baseMapper.selectOne(queryWrapper);
        if(config != null){
            config.setConfigValue(val);
            this.baseMapper.updateById(config);
        }

    }

}
