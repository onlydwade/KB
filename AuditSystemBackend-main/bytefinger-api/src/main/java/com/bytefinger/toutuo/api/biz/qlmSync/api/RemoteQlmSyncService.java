package com.bytefinger.toutuo.api.biz.qlmSync.api;

import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.constant.ServiceNameConstants;
import com.bytefinger.common.core.domain.R;

import com.bytefinger.toutuo.api.biz.qlmSync.factory.RemoteQlmSyncServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 千里马获取
 *
 * @author chengwei
 */
@FeignClient(contextId = "remoteQlmSyncService", value = ServiceNameConstants.BIZ_SERVICE, fallbackFactory = RemoteQlmSyncServiceFallbackFactory.class)
public interface RemoteQlmSyncService {

    //千里马获取
    @PutMapping("/businessOpportunity/qlm/list")
    R sendNotifyTask(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
