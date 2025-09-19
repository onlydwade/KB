package com.bytefinger.toutuo.auth.service;

import com.alibaba.fastjson2.JSONObject;

import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.auth.config.DingProperties;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 钉钉
 *
 * @author chengwei
 */
@Component
@AllArgsConstructor
@Slf4j
public class DingTalkService {
    private final DingProperties dingProperties;
    private final RemoteUserService remoteUserService;
    /**
     * 获取钉钉acctoken
     */
    public String getDingToken() {
        try{
            DingTalkClient client = new DefaultDingTalkClient(dingProperties.getTokenURL());
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(dingProperties.getAppkey());
            request.setAppsecret(dingProperties.getAppsecret());
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            if(Objects.nonNull(response)){
                return JSONObject.parseObject(response.getBody()).getString("access_token");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return  "";
    }

    /**
     * 获取钉钉userId
     */
    public String getDingUserId(String code,String acctoken) {
        try{
            DingTalkClient client = new DefaultDingTalkClient(dingProperties.getLoginURL());
            OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
            req.setCode(code);
            log.info("code："+code);
            log.info("acctoken："+acctoken);
            OapiV2UserGetuserinfoResponse response = client.execute(req, acctoken);
            log.info("response.getBody()："+response);
            if(Objects.nonNull(response)){
                log.info("response.getBody()："+response.getBody());
                JSONObject respJsonObj= JSONObject.parseObject(response.getBody());
                if("0".equals(respJsonObj.getString("errcode"))  ){
                    String userid=respJsonObj.getJSONObject("result").getString("userid");
                    return  userid;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return  "";
    }

    /**
     * 获取钉钉手机
     */
    public String getDingUserInfo(String userId ,String access_token) {
        try{
            DingTalkClient client = new DefaultDingTalkClient(dingProperties.getUserURL());
            OapiV2UserGetRequest request = new OapiV2UserGetRequest();
            request.setUserid(userId);
            request.setLanguage("zh_CN");
            OapiV2UserGetResponse response = client.execute(request, access_token);
            if(Objects.nonNull(response)){
                log.info("response.getBody()："+response.getBody());
                JSONObject respJsonObj= JSONObject.parseObject(response.getBody());
                if("0".equals(respJsonObj.getString("errcode"))  ){
                    String mobile=respJsonObj.getJSONObject("result").getString("mobile");
                    return  mobile;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return  "";
    }


    /**
     * 通过system服务获取密码
     */
    public String getPasswordByPhone(String phone ){
        SysUser user   =remoteUserService.getInfoByPhone(phone, SecurityConstants.INNER).getData();
        log.info("user="+user);
        return user.getPassword();
    }

    /**
     * 获取CorpId
     */
    public String getDingCorpId( ){
        return dingProperties.getCorpid();
    }

}
