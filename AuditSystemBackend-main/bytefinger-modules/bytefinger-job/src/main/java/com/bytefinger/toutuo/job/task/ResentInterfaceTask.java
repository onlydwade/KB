package com.bytefinger.toutuo.job.task;

import com.alibaba.fastjson2.JSON;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.company.api.RemoteCompanyService;
import com.bytefinger.toutuo.api.biz.resentInterface.api.RemoteResentInterfaceService;
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
public class ResentInterfaceTask {

//    private final RemoteResentInterfaceService remoteResentInterfaceService;
//
//    private static boolean FLAG;
//
//    @Async("taskExecutor")
//    @Scheduled(cron = "0 0/2 * * * ? ")
//    public void sendNotifyTask() {
//        if (FLAG) {return;}
//        FLAG = true;
//        try {
//            log.info("================>重调接口定时器Task");
//            R result = remoteResentInterfaceService.sendNotifyTask(SecurityConstants.INNER);
//            log.info("重调接口定时器Task：{}", JSON.toJSONString(result));
//        } catch (Exception e) {
//            log.error("重调接口定时器Task：{}", e.getMessage());
//        } finally {
//            FLAG = false;
//        }
//    }

}
