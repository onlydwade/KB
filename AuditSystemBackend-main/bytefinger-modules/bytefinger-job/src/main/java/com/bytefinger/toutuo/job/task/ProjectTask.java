package com.bytefinger.toutuo.job.task;

import com.alibaba.fastjson2.JSON;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.project.api.RemoteProjectService;
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
public class ProjectTask {

    private final RemoteProjectService remoteProjectService;

    private static boolean FLAG;
    private static boolean BFLAG;
    private static boolean CFLAG;


    @Async("taskExecutor")
    @Scheduled(cron = "0 15 10 * * ?")
    public void sendNotifyTask() {
        if (FLAG) {return;}
        FLAG = true;
        try {
            log.info("================>执行风险提醒通知定时器Task");
            R result = remoteProjectService.sendNotifyTask(SecurityConstants.INNER);
            log.info("执行风险提醒通知定时器Task：{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("执行风险提醒通知定时器Task：{}", e.getMessage());
        } finally {
            FLAG = false;
        }
    }

    @Async("taskExecutor")
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void projectDaoQiStatusChange() {
        if (BFLAG) {return;}
        BFLAG = true;
        try {
            log.info("================>项目到期/还原定时器Task");
            R result = remoteProjectService.projectDaoQiStatusChange(SecurityConstants.INNER);
            log.info("项目到期/还原定时器Task：{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("项目到期/还原定时器Task：{}", e.getMessage());
        } finally {
            BFLAG = false;
        }
    }

    @Async("taskExecutor")
    @Scheduled(cron = "0 0/3 * * * ? ")
    public void projectCircularChange() {
        if (CFLAG) {return;}
        CFLAG = true;
        try {
            log.info("================>首页通告定时器Task");
            R result = remoteProjectService.projectCircularChange(SecurityConstants.INNER);
            log.info("首页通告定时器Task：{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("首页通告定时器Task：{}", e.getMessage());
        } finally {
            CFLAG = false;
        }
    }

}
