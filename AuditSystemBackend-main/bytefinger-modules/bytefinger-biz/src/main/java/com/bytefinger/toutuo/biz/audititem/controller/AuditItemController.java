package com.bytefinger.toutuo.biz.audititem.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.audititem.domain.AuditItem;
import com.bytefinger.toutuo.biz.audititem.service.IAuditItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审计项目关系表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-05
 */
@Slf4j
@Api(tags = "审计项目关系表")
@AllArgsConstructor
@RestController
@RequestMapping("/auditItem")
public class AuditItemController extends BaseController {
    private final IAuditItemService auditItemService;

    @ApiOperation(value = "新增-审计项目关系表")
    @PutMapping("/add")
    @RequiresPermissions("biz:auditItem:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody AuditItem auditItem) {
        auditItemService.add(auditItem);
        return success(auditItem);
    }

    @ApiOperation(value = "导入模版-审计项目关系表")
    @GetMapping("/template/{auditId}/{templateId}")
    @RequiresPermissions("biz:auditItem:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult template(@PathVariable("auditId") Long auditId, @PathVariable("templateId") Long templateId) {
        auditItemService.template(auditId,templateId);
        return success();
    }

    @ApiOperation(value = "提交调研记录-审计项目关系表")
    @PutMapping("/findingDetails")
    @RequiresPermissions("biz:auditItem:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult findingDetails(@RequestBody AuditItem auditItem) {
        return success(auditItemService.findingDetails(auditItem));
    }

    @ApiOperation(value = "提交纠错措施-审计项目关系表")
    @PutMapping("/response")
    @RequiresPermissions("biz:auditItem:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult response(@RequestBody AuditItem auditItem) {
        auditItemService.response(auditItem);
        return success();
    }


    @ApiOperation(value = "修改-审计项目关系表")
    @PutMapping("/update")
    @RequiresPermissions("biz:auditItem:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AuditItem auditItem) {
        return success(auditItemService.updateById(auditItem));
    }

    @ApiOperation(value = "删除-审计项目关系表")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:auditItem:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        auditItemService.delete(id);
        return success();
    }

    @ApiOperation(value = "详情-审计项目关系表")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditItem:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditItemService.getById(id));
    }

    @ApiOperation(value = "列表-审计项目关系表")
    @GetMapping("/list")
    @RequiresPermissions("biz:auditItem:list")
    public AjaxResult list() {
        return success(auditItemService.list());
    }

    @ApiOperation(value = "列表-审计团队信息")
    @GetMapping("/list/{auditId}")
    @RequiresPermissions("biz:auditTeam:list")
    public AjaxResult list(@PathVariable("auditId") Long auditId) {
        return success(auditItemService.list(auditId));
    }

    @ApiOperation(value = "分页（带条件）-审计项目关系表")
    @PostMapping("/page")
    @RequiresPermissions("biz:auditItem:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(auditItemService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
