package com.bytefinger.toutuo.api.biz.resentInterface.api;

import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.constant.ServiceNameConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.company.factory.RemoteCompanyServiceFallbackFactory;
import com.bytefinger.toutuo.api.biz.resentInterface.factory.RemoteResentInterfaceServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 接口重调
 *
 * @author chengwei
 */
@FeignClient(contextId = "remoteResentInterfaceService", value = ServiceNameConstants.BIZ_SERVICE, fallbackFactory = RemoteResentInterfaceServiceFallbackFactory.class)
public interface RemoteResentInterfaceService {

    //报销系统调用失败重调
    @GetMapping("/resentInterface/resentReimbursementApplication")
    R sendNotifyTask(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
