package com.bytefinger.toutuo.auth.service;

import com.bytefinger.common.core.constant.Constants;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.utils.ip.IpUtils;
import com.bytefinger.toutuo.api.system.log.api.RemoteLogService;
import com.bytefinger.toutuo.api.system.log.domain.SysLogininfor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 记录日志方法
 *
 * @author patrick
 */
@Component
@AllArgsConstructor
public class SysRecordLogService {

    private final RemoteLogService remoteLogService;

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    public void recordLogininfor(String username, String status, String message) {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        logininfor.setMsg(message);

        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        remoteLogService.saveLogininfor(logininfor, SecurityConstants.INNER);
    }
}
