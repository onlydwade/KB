package com.bytefinger.toutuo.biz.interfacelog.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.toutuo.biz.companyrisk.domain.ProjectCompanyRiskInspection;
import com.bytefinger.toutuo.biz.companyrisk.service.IProjectCompanyRiskInspectionService;
import com.bytefinger.toutuo.biz.interfacelog.service.IInterfaceLogService;
import com.bytefinger.toutuo.biz.oa.domain.dto.ReimbursementApplicationRespone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 接口调用日志
 * </p>
 *
 * @author chengwei
 * @since 2023-12-12
 */
@Slf4j
@Api(tags = "接口调用日志")
@AllArgsConstructor
@RestController
@RequestMapping("/resentInterface")
public class InterfaceLogController extends BaseController {
    private final IInterfaceLogService iInterfaceLogService;


    @ApiOperation(value = "报销系统-发起申请")
    @GetMapping("/reimbursementApplication/sent/{projectId}")
    public AjaxResult reimbursementApplicationSent(@PathVariable("projectId") String projectId) {
        ReimbursementApplicationRespone respone = iInterfaceLogService.sentReimbursementApplication( projectId ,"YES");
        return success(respone);
    }

    @ApiOperation(value = "报销-发起申请-数据初始化")
    @GetMapping("/reimbursementApplication/sent/init")
    public AjaxResult reimbursementApplicationSentInit() {
        return success( iInterfaceLogService.sentReimbursementApplicationInit( ));
    }

    @ApiOperation(value = "报销系统-作废接口")
    @GetMapping("/reimbursementApplication/abolish/{projectId}")
    public AjaxResult reimbursementApplicationAbolish(@PathVariable("projectId") String projectId) {
        ReimbursementApplicationRespone respone = iInterfaceLogService.sentReimbursementApplication( projectId ,"NO");
        return success(respone);
    }


    @ApiOperation(value = "重发报销系统", hidden = true)
    @GetMapping("/resentReimbursementApplication")
    @InnerAuth
    public R resentReimbursementApplication() {
        log.info("重发报销系统：resentReimbursementApplication");
        //iInterfaceLogService.ResentReimbursementApplication();
        return R.ok();
    }

}
