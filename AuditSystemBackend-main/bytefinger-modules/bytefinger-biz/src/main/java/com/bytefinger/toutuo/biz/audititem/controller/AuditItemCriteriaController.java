package com.bytefinger.toutuo.biz.audititem.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.audititem.domain.AuditItemCriteria;
import com.bytefinger.toutuo.biz.audititem.service.IAuditItemCriteriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审计项目标准表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-07
 */
@Slf4j
@Api(tags = "审计项目标准表")
@AllArgsConstructor
@RestController
@RequestMapping("/auditItemCriteria")
public class AuditItemCriteriaController extends BaseController {
    private final IAuditItemCriteriaService auditItemCriteriaService;

    @ApiOperation(value = "新增-审计项目标准表")
    @PutMapping("/add")
    @RequiresPermissions("biz:auditItemCriteria:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody AuditItemCriteria auditItemCriteria) {
        return success(auditItemCriteriaService.save(auditItemCriteria));
    }

    @ApiOperation(value = "修改-审计项目标准表")
    @PutMapping("/update")
    @RequiresPermissions("biz:auditItemCriteria:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AuditItemCriteria auditItemCriteria) {
        return success(auditItemCriteriaService.updateById(auditItemCriteria));
    }

    @ApiOperation(value = "删除-审计项目标准表")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:auditItemCriteria:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(auditItemCriteriaService.removeById(id));
    }

    @ApiOperation(value = "详情-审计项目标准表")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditItemCriteria:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditItemCriteriaService.getById(id));
    }

    @ApiOperation(value = "列表-审计项目标准表")
    @GetMapping("/list")
    @RequiresPermissions("biz:auditItemCriteria:list")
    public AjaxResult list() {
        return success(auditItemCriteriaService.list());
    }

    @ApiOperation(value = "分页（带条件）-审计项目标准表")
    @PostMapping("/page")
    @RequiresPermissions("biz:auditItemCriteria:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(auditItemCriteriaService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
