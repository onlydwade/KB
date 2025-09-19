package com.bytefinger.toutuo.biz.oa.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.oa.config.OaProperties;
import com.bytefinger.toutuo.biz.oa.domain.OaMessageLog;
import com.bytefinger.toutuo.biz.oa.service.IOaMessageLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * <p>
 * oa接口 前端控制器
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
@Slf4j
@Api(tags = "oa接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa")
public class OaController extends BaseController {

//    private final OaProperties oaProperties;
//    private final IOaMessageLogService iOaMessageLogService;
//    private final IWorkBriefService IWorkBriefService;
//
//    @ApiOperation(value = "设为已办")
//    @GetMapping("/setTodoDone/{id}")
//    public AjaxResult setTodoDone(@PathVariable("id") Long id) {
//        OaMessageLog log = iOaMessageLogService.getById(id);
//        IWorkBriefService.setOaMessage(log);
//        return success();
//    }

}
