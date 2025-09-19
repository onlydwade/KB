package com.bytefinger.toutuo.system.config;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.toutuo.api.system.core.domain.SysMessage;
import com.bytefinger.toutuo.system.core.service.ISysMessageService;
import com.bytefinger.toutuo.system.dto.OaTodo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 系统消息消费者
 *
 * @author pat
 * @date 2022/10/11 14:24
 **/
@Component
@Slf4j
@AllArgsConstructor
public class MQSysMessage {

    private final ISysMessageService sysMessageService;

    private final RemoteUserService remoteUserService;

    private final OaProperties oaProperties;

    private final static String XI_TONG_TONG_ZHI = "XI_TONG_TONG_ZHI";
    private final static String DAI_BAN_TONG_ZHI = "DAI_BAN_TONG_ZHI";

    @Bean
    public Consumer<SysMessage> sysMessage() {
        return message -> {
            log.info("系统消息消费=>" + JSON.toJSONString(message));
            message.setCreateTime(new Date());
            String sendChannels = message.getSendChannels();
            if(sendChannels.contains(XI_TONG_TONG_ZHI)){
                sysMessageService.save(message);
            }
            if(sendChannels.contains(DAI_BAN_TONG_ZHI)){
                //发送OA待办通知
                if(message.getId() == null){
                    sysMessageService.save(message);
                }
                String detailUrl = String.format(oaProperties.getDetailUrl(), message.getId());
                UserVO user = remoteUserService.getInfo(message.getUserId(), SecurityConstants.INNER).getData();
                OaTodo todo = new OaTodo();
                todo.setAppName("TOUTUO");
                todo.setModelName(message.getModule());
                todo.setModelId(message.getModule());
                todo.setSubject(message.getTitle());
                todo.setLink(detailUrl);
                todo.setMobileLink(detailUrl);
                todo.setPadLink(detailUrl);
                todo.setType(2);
                Map<String, String> targets = new HashMap<>();
                targets.put("MobileNo", user.getPhonenumber());
                todo.setTargets(targets);
                todo.setCreateTime(DateUtils.getTime());
                log.info("系统消息消费-OA通知参数=>{}",todo);
                String body = HttpUtil.createPost(oaProperties.getTodoUrl()).contentType("application/json").body(JSON.toJSONString(todo)).execute().body();
                log.info("系统消息消费-OA通知返回结果=>{}",body);
                JSONObject json = JSONObject.parseObject(body);
                Integer returnState = json.getInteger("returnState");
                //TODO 状态保存数据库
            }
        };
    }


//    @Bean
//    public Consumer<SysMessage> oaMessage() {
//        return message -> {
//            log.info("系统消息消费=>" + JSON.toJSONString(message));
//            message.setCreateTime(new Date());
//            //sysMessageService.save(message);
//
//            //发送OA通知
//            String sendChannels = message.getSendChannels();
//
//
//        };
//    }

}
