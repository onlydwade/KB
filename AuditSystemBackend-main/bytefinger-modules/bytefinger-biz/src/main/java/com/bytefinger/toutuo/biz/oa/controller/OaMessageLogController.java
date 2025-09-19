package com.bytefinger.toutuo.biz.oa.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.oa.domain.OaMessageLog;
import com.bytefinger.toutuo.biz.oa.service.IOaMessageLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 发送oa待办记录表 前端控制器
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
@Slf4j
@Api(tags = "发送oa待办记录表")
@RequiredArgsConstructor
@RestController
@RequestMapping("/oaLog")
public class OaMessageLogController extends BaseController {

    @Autowired
    IOaMessageLogService iOaMessageLogService;

    //@Autowired
    //IWorkBriefService iWorkBriefService;

    @ApiOperation(value = "获取-发送oa待办记录")
    @GetMapping("/view/{id}")
    public AjaxResult view(@PathVariable("id") Long id) {
        return success(iOaMessageLogService.getById(id));
    }

    @ApiOperation(value = "已办")
    @GetMapping("/done/{id}")
    public AjaxResult done(@PathVariable("id") Long id) {
        OaMessageLog log = new OaMessageLog();
        log.setId(id);
        log.setStatus(2);
        boolean result = iOaMessageLogService.updateById(log);
        if(result){
            OaMessageLog readLog = iOaMessageLogService.getById(id);
            //iWorkBriefService.setOaMessage(readLog);
            return success(result);
        }
        else {
            return error("操作失败");
        }
    }
}
