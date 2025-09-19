package com.bytefinger.toutuo.job.task;

import com.alibaba.fastjson2.JSON;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.company.api.RemoteCompanyService;
import com.bytefinger.toutuo.api.biz.project.api.RemoteProjectService;
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
public class CompanyTask {

    private final RemoteCompanyService remoteCompanyService;

    private static boolean FLAG;

    @Async("taskExecutor")
    @Scheduled(cron = "0 30 10 * * ?")
    public void sendNotifyTask() {
        if (FLAG) {return;}
        FLAG = true;
        try {
            log.info("================>子公司执行风险提醒通知定时器Task");
            R result = remoteCompanyService.sendNotifyTask(SecurityConstants.INNER);
            log.info("子公司执行风险提醒通知定时器Task：{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("子公司执行风险提醒通知定时器Task：{}", e.getMessage());
        } finally {
            FLAG = false;
        }
    }

}
