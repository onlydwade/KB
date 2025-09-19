package com.bytefinger.common.log.service;

import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.toutuo.api.system.log.api.RemoteLogService;
import com.bytefinger.toutuo.api.system.log.domain.SysOperLog;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author patrick
 */
@Service
@AllArgsConstructor
public class AsyncLogService {

    private final RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) {
        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
