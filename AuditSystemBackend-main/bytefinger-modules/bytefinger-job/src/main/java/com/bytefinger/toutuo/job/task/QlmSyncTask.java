package com.bytefinger.toutuo.job.task;

import com.alibaba.fastjson2.JSON;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.qlmSync.api.RemoteQlmSyncService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author chengwei
 * @date 2023/12/12 15:00
 **/
@Component
@AllArgsConstructor
@Slf4j
public class QlmSyncTask {

//    private final RemoteQlmSyncService remoteQlmSyncService;
//
//    private static boolean FLAG;
//
//    @Async("taskExecutor")
//    @Scheduled(cron = "0 0 2 * * ?")
//    public void sendNotifyTask() {
//        if (FLAG) {return;}
//        FLAG = true;
//        try {
//            log.info("================>千里马同步定时器Task");
//            R result = remoteQlmSyncService.sendNotifyTask(SecurityConstants.INNER);
//            log.info("千里马同步定时器Task：{}", JSON.toJSONString(result));
//        } catch (Exception e) {
//            log.error("千里马同步定时器Task：{}", e.getMessage());
//        } finally {
//            FLAG = false;
//        }
//    }

}
