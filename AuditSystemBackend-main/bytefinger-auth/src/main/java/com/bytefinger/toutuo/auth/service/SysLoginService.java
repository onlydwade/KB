package com.bytefinger.toutuo.auth.service;

import com.bytefinger.common.core.constant.CacheConstants;
import com.bytefinger.common.core.constant.Constants;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.exception.ServiceException;
import com.bytefinger.common.core.exception.user.UserException;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.utils.ip.IpUtils;
import com.bytefinger.common.redis.service.RedisService;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录校验方法
 *
 * @author patrick
 */
@Component
@AllArgsConstructor
@Slf4j
public class SysLoginService {

    private final RemoteUserService remoteUserService;

    private final SysPasswordService passwordService;

    private final SysRecordLogService recordLogService;

    private final RedisService redisService;

    private static final int maxRetryCount = 20;

    public LoginUser login(String username, String password){
        return login(username, password, false);
    }
    /**
     * 登录
     */
    public LoginUser login(String username, String password, boolean sso)  throws ServiceException{
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);

        // 查无用户
        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData()) || R.FAIL == userResult.getCode()) {
            if (R.FAIL == userResult.getCode()) {
                throw new ServiceException(userResult.getMsg());
            }

            String ipAddr = IpUtils.getIpAddr(ServletUtils.getRequest());
            String key = CacheConstants.IP_MORE_LOGIN_KEY + ipAddr;
            Integer retryCount = redisService.getCacheObject(key);
            if (retryCount == null) {
                retryCount = 0;
            }
            redisService.setCacheObject(key, ++retryCount, CacheConstants.PASSWORD_LOCK_TIME, TimeUnit.MINUTES);
            if (retryCount.compareTo(maxRetryCount) > -1) {
                throw new ServiceException("尝试次数过多，请稍后再试");
            }

            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new ServiceException("登录失败：用户不存在或密码错误，请检查");
        }

        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();
        if(!sso){
            passwordService.validate(user, password);
        }
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }

    public void logout(String loginName) {
        recordLogService.recordLogininfor(loginName, Constants.LOGOUT, "退出成功");
    }

}
