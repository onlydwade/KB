package com.bytefinger.toutuo.biz.auditproject.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.audit.domain.Audit;
import com.bytefinger.toutuo.biz.audit.service.IAuditService;
import com.bytefinger.toutuo.biz.auditproject.domain.AuditProjectSchedule;
import com.bytefinger.toutuo.biz.auditproject.service.IAuditProjectScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 项目审计任务管理 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-07
 */
@Slf4j
@Api(tags = "项目审计任务管理")
@AllArgsConstructor
@RestController
@RequestMapping("/auditProjectSchedule")
public class AuditProjectScheduleController extends BaseController {
    private final IAuditProjectScheduleService auditProjectScheduleService;

    private final IAuditService auditService;

    @ApiOperation(value = "新增-项目审计任务管理")
    @PutMapping("/add")
    @RequiresPermissions("biz:auditProjectSchedule:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody AuditProjectSchedule auditProjectSchedule) {
        return success(auditProjectScheduleService.save(auditProjectSchedule));
    }

    @ApiOperation(value = "修改-项目审计任务管理")
    @PutMapping("/update")
    @RequiresPermissions("biz:auditProjectSchedule:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AuditProjectSchedule auditProjectSchedule) {
        return success(auditProjectScheduleService.updateById(auditProjectSchedule));
    }

    @ApiOperation(value = "删除-项目审计任务管理")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:auditProjectSchedule:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(auditProjectScheduleService.removeById(id));
    }

    @ApiOperation(value = "详情-项目审计任务管理")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditProjectSchedule:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditProjectScheduleService.getById(id));
    }

    @ApiOperation(value = "查询审计计划是否已经绑定审计任务")
    @GetMapping("/isBind/{id}")
    @RequiresPermissions("biz:auditProjectSchedule:get")
    public AjaxResult isBind(@PathVariable("id") Integer id) {
        List<Audit> audits =auditService.getByScheduleId(id);
        if(CollectionUtils.isNotEmpty(audits)){
            return success(Boolean.TRUE);
        }
        return success(Boolean.FALSE);
    }

    @ApiOperation(value = "列表-项目审计任务管理")
    @GetMapping("/list")
    @RequiresPermissions("biz:auditProjectSchedule:list")
    public AjaxResult list() {
        return success(auditProjectScheduleService.list());
    }

    @ApiOperation(value = "分页（带条件）-项目审计任务管理")
    @PostMapping("/page")
    @RequiresPermissions("biz:auditProjectSchedule:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(auditProjectScheduleService.page(queryPage.toPage(), queryPage.getWrapper()));
    }




}
