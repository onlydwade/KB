package com.bytefinger.toutuo.api.biz.qlmSync.factory;

import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.project.api.RemoteProjectService;
import com.bytefinger.toutuo.api.biz.project.factory.RemoteProjectServiceFallbackFactory;
import com.bytefinger.toutuo.api.biz.qlmSync.api.RemoteQlmSyncService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 千里马
 *
 * @author chengwei
 */
@Component
public class RemoteQlmSyncServiceFallbackFactory implements FallbackFactory<RemoteQlmSyncService> {


    private static final Logger log = LoggerFactory.getLogger(RemoteQlmSyncServiceFallbackFactory.class);
    @Override
    public RemoteQlmSyncService create(Throwable throwable) {
        log.error("千里马服务调用失败:{}", throwable.getMessage());
        return source -> R.fail("请稍后再试:" + throwable.getMessage());
    }


}
