package com.bytefinger.toutuo.api.biz.rules.api;

import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.constant.ServiceNameConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.rules.factory.RemoteRulesFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = "remoteRulesService", value = ServiceNameConstants.BIZ_SERVICE, fallbackFactory = RemoteRulesFallbackFactory.class)
public interface RemoteRulesService {

    /**
     * 定时器，检查并自动放弃
     *
     * @param source
     * @return
     */
    @PutMapping("/rules/rulesTask")
    R rulesTask(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
