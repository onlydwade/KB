package com.bytefinger.toutuo.api.system.log.factory;

import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.system.log.api.RemoteLogService;
import com.bytefinger.toutuo.api.system.log.domain.SysLogininfor;
import com.bytefinger.toutuo.api.system.log.domain.SysOperLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 日志服务降级处理
 *
 * @author patrick
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable throwable) {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService() {
            @Override
            public R<Boolean> saveLog(SysOperLog sysOperLog, String source) {
                return null;
            }

            @Override
            public R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source) {
                return null;
            }
        };

    }
}
