package com.bytefinger.toutuo.job.task;

import com.alibaba.fastjson2.JSON;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author pat
 * @date 2022/10/04 15:00
 **/
@Component
@AllArgsConstructor
@Slf4j
public class UserTask {

    private final RemoteUserService remoteUserService;

    private static boolean FLAG;

    @Async("taskExecutor")
    @Scheduled(cron = "0 0/2 * * * ? ")
    public void userSyncTask() {
        if (FLAG) {return;}
        FLAG = true;
        try {
            log.info("================>执行主数据用户同步定时器Task");
            R result = remoteUserService.userSyncTask(SecurityConstants.INNER);
            log.info("执行主数据用户同步定时器Task：{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("执行主数据用户同步定时器Task：{}", e.getMessage());
        } finally {
            FLAG = false;
        }
    }

}
