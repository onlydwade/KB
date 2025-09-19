package com.bytefinger.toutuo.biz.auditcommunication.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.audit.domain.Audit;
import com.bytefinger.toutuo.biz.auditcommunication.domain.AuditCommunication;
import com.bytefinger.toutuo.biz.auditcommunication.service.IAuditCommunicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审计任务交流记录 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-04
 */
@Slf4j
@Api(tags = "审计任务交流记录")
@AllArgsConstructor
@RestController
@RequestMapping("/auditCommunication")
public class AuditCommunicationController extends BaseController {
    private final IAuditCommunicationService auditCommunicationService;

    @ApiOperation(value = "新增-审计任务交流记录")
    @PutMapping("/add")
    @RequiresPermissions("biz:auditCommunication:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody AuditCommunication auditCommunication) {
        auditCommunicationService.add(auditCommunication);
        return success();
    }

    @ApiOperation(value = "修改-审计任务交流记录")
    @PutMapping("/update")
    @RequiresPermissions("biz:auditCommunication:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AuditCommunication auditCommunication) {
        return success(auditCommunicationService.updateById(auditCommunication));
    }

    @ApiOperation(value = "删除-审计任务交流记录")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:auditCommunication:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(auditCommunicationService.removeById(id));
    }

    @ApiOperation(value = "详情-审计任务交流记录")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditCommunication:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditCommunicationService.getById(id));
    }

    @ApiOperation(value = "列表-审计任务交流记录")
    @GetMapping("/list")
    @RequiresPermissions("biz:auditCommunication:list")
    public AjaxResult list() {
        return success(auditCommunicationService.list());
    }

    @ApiOperation(value = "分页（带条件）-审计任务交流记录")
    @PostMapping("/page")
    @RequiresPermissions("biz:auditCommunication:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        IPage<AuditCommunication> page = auditCommunicationService.page(queryPage);
        return success(page);
    }

}
