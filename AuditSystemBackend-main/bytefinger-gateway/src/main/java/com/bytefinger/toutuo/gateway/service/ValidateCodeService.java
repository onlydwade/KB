package com.bytefinger.toutuo.gateway.service;

import com.bytefinger.common.core.exception.CaptchaException;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;

import java.io.IOException;

/**
 * 验证码处理
 *
 * @author patrick
 */
public interface ValidateCodeService {

    /**
     * 生成验证码
     */
    public AjaxResult createCaptcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     */
    public void checkCaptcha(String key, String value) throws CaptchaException;
}
