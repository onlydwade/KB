package com.bytefinger.toutuo.api.biz.company.factory;

import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.company.api.RemoteCompanyService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 服务降级处理
 *
 * @author patrick
 */
@Component
public class RemoteCompanyServiceFallbackFactory implements FallbackFactory<RemoteCompanyService> {

    @Override
    public RemoteCompanyService create(Throwable throwable) {
        return source -> R.fail("请稍后再试:" + throwable.getMessage());
    }
}
