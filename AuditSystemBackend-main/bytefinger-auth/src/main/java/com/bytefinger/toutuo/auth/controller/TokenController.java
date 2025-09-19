package com.bytefinger.toutuo.auth.controller;

import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.utils.JwtUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.security.auth.AuthUtil;
import com.bytefinger.common.security.service.TokenService;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.auth.controller.form.LoginBody;
import com.bytefinger.toutuo.auth.controller.form.SwitchBody;
import com.bytefinger.toutuo.auth.service.SysLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * token 控制
 *
 * @author patrick
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final TokenService tokenService;

    private final SysLoginService sysLoginService;

    private final RemoteUserService remoteUserService;

    /**
     * 常规用户密码登录
     *
     * @param form
     * @return
     */
    @PostMapping("/login")
    public AjaxResult login(@Validated @RequestBody LoginBody form) {
        return R.ok(tokenService.createToken(sysLoginService.login(form.getUsername(), form.getPassword()))).toAjaxResult();
    }

    /**
     * 角色切换
     *
     * @param form
     * @return
     */
    @PostMapping("/switchAuth")
    public AjaxResult switchAuth(@Validated @RequestBody SwitchBody form) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (null != loginUser && null != loginUser.getDeptId() && null != loginUser.getPostId()) {
            // 判断用户是否有权限
            R<LoginUser> result = remoteUserService.authRole(loginUser.getUserid(), form.getDeptId(), form.getPostId(), SecurityConstants.INNER);
            if (result.isSuccess()) {
                return R.ok(tokenService.createToken(result.getData())).toAjaxResult();
            }
        }

        return R.fail("角色切换失败").toAjaxResult();
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            sysLoginService.logout(username);
        }

        return R.ok();
    }

    /**
     * 刷新token
     *
     * @param request
     * @return
     */
    @PostMapping("refresh")
    public R refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }

        return R.ok();
    }

}
