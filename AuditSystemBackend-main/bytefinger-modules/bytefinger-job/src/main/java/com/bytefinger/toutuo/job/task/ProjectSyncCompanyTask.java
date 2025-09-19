package com.bytefinger.toutuo.job.task;

import com.alibaba.fastjson2.JSON;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
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
public class ProjectSyncCompanyTask {

    private final RemoteProjectService remoteProjectService;

    private static boolean FLAG;

    @Async("taskExecutor")
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void sendNotifyTask() {
        if (FLAG) {return;}
        FLAG = true;
        try {
            log.info("================>执行项目数据同步子公司定时器Task");
            R result = remoteProjectService.syncProjectCompany(SecurityConstants.INNER);
            log.info("执行项目数据同步子公司定时器Task：{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("执行项目数据同步子公司定时器Task：{}", e.getMessage());
        } finally {
            FLAG = false;
        }
    }

}
