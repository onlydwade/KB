package com.bytefinger.toutuo.biz.auditinfo.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.auditinfo.domain.AuditInfoRequest;
import com.bytefinger.toutuo.biz.auditinfo.service.IAuditInfoRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审计信息请求表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-09
 */
@Slf4j
@Api(tags = "审计信息请求表")
@AllArgsConstructor
@RestController
@RequestMapping("/auditInfoRequest")
public class AuditInfoRequestController extends BaseController {
    private final IAuditInfoRequestService auditInfoRequestService;

    @ApiOperation(value = "新增-审计信息请求表")
    @PutMapping("/add")
    @RequiresPermissions("biz:auditInfoRequest:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody AuditInfoRequest auditInfoRequest) {
        auditInfoRequestService.add(auditInfoRequest);
        return success();
    }

    @ApiOperation(value = "修改-审计信息请求表")
    @PutMapping("/update")
    @RequiresPermissions("biz:auditInfoRequest:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AuditInfoRequest auditInfoRequest) {
        auditInfoRequestService.update(auditInfoRequest);
        return success();
    }

    @ApiOperation(value = "删除-审计信息请求表")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:auditInfoRequest:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(auditInfoRequestService.removeById(id));
    }

    @ApiOperation(value = "详情-审计信息请求表")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditInfoRequest:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditInfoRequestService.getById(id));
    }

    @ApiOperation(value = "列表-审计信息请求表")
    @GetMapping("/list")
    @RequiresPermissions("biz:auditInfoRequest:list")
    public AjaxResult list() {
        return success(auditInfoRequestService.list());
    }

    @ApiOperation(value = "列表-审计信息请求表")
    @GetMapping("/list/{auditId}")
    @RequiresPermissions("biz:auditInfoRequest:list")
    public AjaxResult list(@PathVariable("auditId") Long auditId) {
        return success(auditInfoRequestService.list(auditId));
    }

    @ApiOperation(value = "分页（带条件）-审计信息请求表")
    @PostMapping("/page")
    @RequiresPermissions("biz:auditInfoRequest:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(auditInfoRequestService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
