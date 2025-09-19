package com.bytefinger.toutuo.system.log.controller;

import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.api.system.log.domain.SysOperLog;
import com.bytefinger.toutuo.system.log.service.ISysOperLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 操作日志记录
 *
 * @author patrick
 */
@Api(tags = "日志")
@RestController
@RequestMapping("/operlog")
@AllArgsConstructor
public class SysOperLogController extends BaseController {

    private final ISysOperLogService operLogService;

    @ApiOperation("操作日志")
    @RequiresPermissions("system:operlog:list")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return AjaxResult.success(operLogService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation(value = "新增操作日志", hidden = true)
    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysOperLog operLog) {
        operLog.setOperTime(new Date());
        return toAjax(operLogService.save(operLog));
    }
}
