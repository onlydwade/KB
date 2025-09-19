package com.bytefinger.toutuo.api.biz.project.api;

import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.constant.ServiceNameConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.project.factory.RemoteProjectServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 项目服务
 *
 * @author patrick
 */
@FeignClient(contextId = "remoteProjectService", value = ServiceNameConstants.BIZ_SERVICE, fallbackFactory = RemoteProjectServiceFallbackFactory.class)
public interface RemoteProjectService {

    @PutMapping("/projectRiskInspection/sendNotifyTask")
    public R sendNotifyTask(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PutMapping("/project/syncProjectCompany")
    public R syncProjectCompany(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PutMapping("/project/projectDaoQiStatusChange")
    public R projectDaoQiStatusChange(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PutMapping("/project/projectCircularChange")
    public R projectCircularChange(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
