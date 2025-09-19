package com.bytefinger.toutuo.biz.workbrief.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.service.ThisService;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.biz.oa.config.OaProperties;
import com.bytefinger.toutuo.biz.oa.domain.OaMessageLog;
import com.bytefinger.toutuo.biz.oa.service.IOaMessageLogService;
import com.bytefinger.toutuo.biz.projectstep.dto.OaTodo;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import com.bytefinger.toutuo.biz.workbrief.service.ISendDataBoardService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 数据看板推送 服务实现类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
@Service
@RequiredArgsConstructor
public class SendDataBoardServiceImpl implements ISendDataBoardService,ThisService<SendDataBoardServiceImpl> {

    @Autowired
    public ISysUserService sysUserService;

    public final OaProperties oaProperties;

    public final IOaMessageLogService iOaMessageLogService;

    @Value("${oa.dataBoardUrl}")
    private String url;

    @Override
    public void sendTodo(Set<String> userIds,Integer year,Long deptId) {

        List<SysUser> users = sysUserService.list(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, userIds));
        try {
            users.forEach(user-> {

                String title = "报表分析数据看板";
                //发送待办通知的url
                String todoUrl = oaProperties.getTodoUrl();

                //记录发送oa待办日志
                OaMessageLog messageLog = new OaMessageLog();

                messageLog.setContent("");
                messageLog.setModule("数据看板");
                messageLog.setModuleType(3);
                messageLog.setUserId(user.getUserId());
                messageLog.setTitle(title);
                messageLog.setCreateTime(new Date());
                messageLog.setStatus(1);
                iOaMessageLogService.saveOrUpdate(messageLog);

                String detailUrl = String.format(url,year,deptId,messageLog.getId());
                messageLog.setContent(detailUrl);

                String phone = user.getPhonenumber();
                String modelName = "数据看板待办提示";

                OaTodo todo = new OaTodo();
                todo.setAppName("TOUTUO");
                todo.setModelName(modelName);
                todo.setModelId(messageLog.getId() == null ? "" : messageLog.getId().toString());
                todo.setSubject(title);
                todo.setLink(detailUrl);
                todo.setMobileLink(detailUrl);
                todo.setPadLink(detailUrl);
                todo.setType(1);
                Map<String, String> targets = new HashMap<>();
                targets.put("MobileNo", phone);
                todo.setTargets(targets);
                todo.setDocCreator(targets);
                todo.setCreateTime(DateUtils.getTime());
                String str = com.alibaba.fastjson2.JSON.toJSONString(todo);
                System.out.println("数据看板发送待办消息传参:" + str);
                String body = HttpUtil.createPost(todoUrl).contentType("application/json").body(com.alibaba.fastjson2.JSON.toJSONString(todo)).execute().body();
                System.out.println("数据看板发送待办消息返回结果:" + body);

                messageLog.setMessage(JSON.toJSONString(todo));
                iOaMessageLogService.updateById(messageLog);
            });
        } catch (Exception e) {
            System.out.println("发送待办异常：" + e.getMessage());
            e.printStackTrace();
        }

    }

}
