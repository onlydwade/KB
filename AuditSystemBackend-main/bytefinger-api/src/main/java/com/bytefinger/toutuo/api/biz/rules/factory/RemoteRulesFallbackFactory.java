package com.bytefinger.toutuo.api.biz.rules.factory;

import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.rules.api.RemoteRulesService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteRulesFallbackFactory implements FallbackFactory<RemoteRulesService> {

    @Override
    public RemoteRulesService create(Throwable throwable) {
        return (source) -> R.fail("调用失败==>" + throwable.getMessage());
    }
}
