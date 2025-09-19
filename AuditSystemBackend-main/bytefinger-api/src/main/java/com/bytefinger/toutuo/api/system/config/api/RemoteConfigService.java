package com.bytefinger.toutuo.api.system.config.api;

import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.constant.ServiceNameConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.project.factory.RemoteProjectServiceFallbackFactory;
import com.bytefinger.toutuo.api.system.factory.RemoteConfigServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 项目服务
 *
 * @author patrick
 */
@FeignClient(contextId = "remoteConfigService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteConfigServiceFallbackFactory.class)
public interface RemoteConfigService {

    @PostMapping("/config/set/{configKey}")
    public R set(@PathVariable("configKey") String configKey, @RequestBody String confiVal);

}
