package com.bytefinger.toutuo.api.biz.company.api;

import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.constant.ServiceNameConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.company.factory.RemoteCompanyServiceFallbackFactory;
import com.bytefinger.toutuo.api.biz.project.factory.RemoteProjectServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 项目服务
 *
 * @author patrick
 */
@FeignClient(contextId = "remoteCompanyService", value = ServiceNameConstants.BIZ_SERVICE, fallbackFactory = RemoteCompanyServiceFallbackFactory.class)
public interface RemoteCompanyService {

    @PutMapping("/companyRiskInspection/sendNotifyTask")
    R sendNotifyTask(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
