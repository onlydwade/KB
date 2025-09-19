package com.bytefinger.toutuo.api.biz.resentInterface.factory;

import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.qlmSync.factory.RemoteQlmSyncServiceFallbackFactory;
import com.bytefinger.toutuo.api.biz.resentInterface.api.RemoteResentInterfaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 服务降级处理
 *
 * @author chengwei
 */
@Component
public class RemoteResentInterfaceServiceFallbackFactory implements FallbackFactory<RemoteResentInterfaceService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteResentInterfaceServiceFallbackFactory.class);
    @Override
    public RemoteResentInterfaceService create(Throwable throwable) {
        log.error("重调接口服务调用失败:{}", throwable.getMessage());
        return source -> R.fail("请稍后再试:" + throwable.getMessage());
    }
}
