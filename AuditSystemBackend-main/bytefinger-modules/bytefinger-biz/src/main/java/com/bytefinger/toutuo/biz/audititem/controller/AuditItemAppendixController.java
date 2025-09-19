package com.bytefinger.toutuo.biz.audititem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.audititem.domain.AuditItemAppendix;
import com.bytefinger.toutuo.biz.audititem.service.IAuditItemAppendixService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审计项目附录表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-08
 */
@Slf4j
@Api(tags = "审计项目附录表")
@AllArgsConstructor
@RestController
@RequestMapping("/auditItemAppendix")
public class AuditItemAppendixController extends BaseController {

    private final IAuditItemAppendixService auditItemAppendixService;


    @ApiOperation(value = "新增-审计项目附录表")
    @PutMapping("/add")
    @RequiresPermissions("biz:auditItemAppendix:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody AuditItemAppendix auditItemAppendix) {
        return success(auditItemAppendixService.save(auditItemAppendix));
    }

    @ApiOperation(value = "修改-审计项目附录表")
    @PutMapping("/update")
    @RequiresPermissions("biz:auditItemAppendix:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AuditItemAppendix auditItemAppendix) {
        return success(auditItemAppendixService.updateById(auditItemAppendix));
    }

    @ApiOperation(value = "删除-审计项目附录表")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:auditItemAppendix:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(auditItemAppendixService.removeById(id));
    }

    @ApiOperation(value = "详情-审计项目附录表")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditItemAppendix:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditItemAppendixService.getById(id));
    }

    @ApiOperation(value = "列表-审计项目附录表")
    @GetMapping("/list")
    @RequiresPermissions("biz:auditItemAppendix:list")
    public AjaxResult list() {
        return success(auditItemAppendixService.list());
    }

    @ApiOperation(value = "分页（带条件）-审计项目附录表")
    @PostMapping("/page")
    @RequiresPermissions("biz:auditItemAppendix:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        Page<AuditItemAppendix> page = auditItemAppendixService.page(queryPage.toPage(), queryPage.getWrapper());
        return success(page);
    }

}
