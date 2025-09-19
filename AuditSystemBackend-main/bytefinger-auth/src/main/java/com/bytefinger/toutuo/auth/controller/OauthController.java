package com.bytefinger.toutuo.auth.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.bytefinger.common.core.constant.Constants;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.exception.ServiceException;
import com.bytefinger.common.core.utils.JwtUtils;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.security.auth.AuthUtil;
import com.bytefinger.common.security.service.TokenService;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.auth.config.DingProperties;
import com.bytefinger.toutuo.auth.config.SsoProperties;
import com.bytefinger.toutuo.auth.controller.form.SwitchBody;
import com.bytefinger.toutuo.auth.service.DingTalkService;
import com.bytefinger.toutuo.auth.service.SysLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * SSO 登录
 *
 * @author patrick
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/login")
public class OauthController {

    private final DingTalkService dingTalkService;

    private final SsoProperties ssoProperties;

    private final TokenService tokenService;

    private final SysLoginService sysLoginService;

    private final RemoteUserService remoteUserService;

    public static void main(String[] args) {
        String userInfo = HttpUtil.createGet("https://gis.gem-flower.com/bsh-unify-api/oauth/check_token?token=7ecf1852-5b7f-40d4-91e1-b7051bf1f719")
                .execute().body();
        System.out.println("userInfo==============================>" + userInfo);

    }

    /**
     * 获取CorpId
     */
    @GetMapping("/ding/getDingCorpId")
    public AjaxResult getDingCorpId(){
        return  AjaxResult.success(dingTalkService.getDingCorpId());
    }


    @GetMapping("/ding/getTokenByDingCode")
    public AjaxResult getTokenByDingCode(String code) {
        //1.获取钉钉accesstoken
        if(StringUtils.isBlank(code)){
            throw new ServiceException("code 为空！");
        }
        String accesstoken = dingTalkService.getDingToken();
        //2.根据accesstoken和code获取钉钉userId
        if(StringUtils.isBlank(accesstoken)){
            throw new ServiceException("钉钉accesstoken 为空！");
        }
        String userId = dingTalkService.getDingUserId(code, accesstoken);
        //3.根据钉钉userId获取手机号码
        if(StringUtils.isBlank(userId)){
            throw new ServiceException("钉钉userId 为空！");
        }
        String userPhone = dingTalkService.getDingUserInfo(userId, accesstoken);
        //4.根据手机号码获取密码：调system服务
        if(StringUtils.isBlank(userPhone)){
            throw new ServiceException("钉钉手机号码获取为空！");
        }
//        String password = dingTalkService.getPasswordByPhone(userPhone);
//        //5.调用登陆方法返回token
//        if(StringUtils.isBlank(password)){
//            throw new ServiceException(userPhone+"该手机获取的用户信息错误！");
//        }
        return R.ok(tokenService.createToken(sysLoginService.login(userPhone,"",true))).toAjaxResult();
    }


    @GetMapping("/oauth2/getSsoToken")
    public AjaxResult getSsoToken(String mobile) {
        return R.ok(tokenService.getSsoToken(mobile)).toAjaxResult();
    }

    @GetMapping("/oauth2/code")
    public AjaxResult login(String code) throws IOException {

        if(StringUtils.isBlank(code)){
           throw new RuntimeException("code 为空！");
        }

        //1.获取SSO TOKEN
        String tokenUrl = String.format(ssoProperties.getTokenUrl(), code, ssoProperties.getCallBackUrl());
        String tokenStr = HttpUtil.createGet(tokenUrl).basicAuth(ssoProperties.getClientId(), ssoProperties.getClientSecret()).execute().body();
        System.out.println("TOKEN==============================>" + tokenStr);
        if (StringUtils.isBlank(tokenStr)){
            throw new RuntimeException("TOKEN 为空！");
        }

        //2.获取用户信息
        JSONObject tokenObj = new JSONObject(tokenStr);
        String accessToken = tokenObj.getStr("access_token");

        //存储Session
        String refreshToken = tokenObj.getStr("refresh_token");
        String userInfo = HttpUtil.createGet(ssoProperties.getUserInfoUrl()).bearerAuth(accessToken).execute().body();
        System.out.println("userInfo==============================>" + userInfo);
        //3.登录当前业务系统
        JSONObject jsonObject = new JSONObject(userInfo);
        String username = jsonObject.getJSONObject("data").getJSONObject("sysUser").getStr("username");
        LoginUser login = null;
        try {
            login = sysLoginService.login(username, username, true);
        }catch (ServiceException e) {
            String loginErrorUrl = String.format(ssoProperties.getLoginErrorUrl(), e.getMessage());
            HttpServletResponse response = ServletUtils.getResponse();
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.sendRedirect(loginErrorUrl);
        }

        Map<String, Object> map = tokenService.createToken(login);
        tokenService.saveSsoToken(login.getSysUser().getPhonenumber(), accessToken);
        //4.重定向业务系统登录页面
        String homeUrl = String.format(ssoProperties.getTargetUrl(), map.get("access_token"), map.get("expires_in"), accessToken);
        ServletUtils.getResponse().sendRedirect(homeUrl);
        return R.ok().toAjaxResult();
    }

    @GetMapping("/oauth2/token")
    public AjaxResult token(String redirectUrl, String token) throws IOException {

        if(StringUtils.isBlank(token)){
            throw new ServiceException("token 为空！");
        }
        if(StringUtils.isBlank(redirectUrl)){
            throw new ServiceException("redirectUrl 为空！");
        }

        //2.获取用户信息
        String tokenUrl = String.format(ssoProperties.getCheckTokenUrl(), token);
        String userInfo = HttpUtil.createGet(tokenUrl).execute().body();
        System.out.println("userInfo==============================>" + userInfo);
        //3.登录当前业务系统
        JSONObject jsonObject = new JSONObject(userInfo);
        String username = jsonObject.getJSONObject("user_info").getStr("username");
        LoginUser login = null;
        try {
            login = sysLoginService.login(username, username, true);
        }catch (ServiceException e) {
            log.info("login=========================================>" + e.getMessage());
            String loginErrorUrl = String.format(ssoProperties.getLoginErrorUrl(), URLEncoder.encode(e.getMessage()));
            HttpServletResponse response = ServletUtils.getResponse();
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.sendRedirect(loginErrorUrl);
            throw new ServiceException(e.getMessage());
        }

        tokenService.saveSsoToken(login.getSysUser().getPhonenumber(), token);
        Map<String, Object> map = tokenService.createToken(login);
        //4.重定向业务系统页面
        redirectUrl = URLDecoder.decode(redirectUrl, Constants.UTF8);
        redirectUrl = redirectUrl.contains("?") ? redirectUrl + "&token=" + map.get("access_token") + "&expires_in=" + map.get("expires_in") + "&ssoToken=" + token :
                redirectUrl + "?token=" + map.get("access_token") + "&expires_in=" + map.get("expires_in") + "&ssoToken=" + token;
        HttpServletResponse response = ServletUtils.getResponse();
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.sendRedirect(redirectUrl);
        return R.ok().toAjaxResult();
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
