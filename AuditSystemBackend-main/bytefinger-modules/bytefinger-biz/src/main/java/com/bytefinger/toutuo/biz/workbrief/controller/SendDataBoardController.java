package com.bytefinger.toutuo.biz.workbrief.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.workbrief.domain.SendDataBoard;
import com.bytefinger.toutuo.biz.workbrief.service.IGroupInfoService;
import com.bytefinger.toutuo.biz.workbrief.service.ISendDataBoardService;
import com.bytefinger.toutuo.biz.workbrief.service.IWorkDataBoardPushInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dataBoard")
@RequiredArgsConstructor
public class SendDataBoardController extends BaseController {

    @Autowired
    IGroupInfoService groupInfoService;

    @Autowired
    ISendDataBoardService sendDataBoardService;

    @Autowired
    IWorkDataBoardPushInfoService iWorkDataBoardPushInfoService;


    @ApiOperation(value = "发送数据看板代办")
    @PostMapping("/sendTodo")
    public AjaxResult sendDataBoardTodo(@RequestBody SendDataBoard sendDataBoard) {
        iWorkDataBoardPushInfoService.sendDataBoard(sendDataBoard);
        return R.ok("发送成功").toAjaxResult();
    }

    @ApiOperation(value = "获取数据看板代办")
    @GetMapping("/getTodo")
    public AjaxResult getTodo() {
        return R.ok(iWorkDataBoardPushInfoService.getSendList()).toAjaxResult();
    }

}
