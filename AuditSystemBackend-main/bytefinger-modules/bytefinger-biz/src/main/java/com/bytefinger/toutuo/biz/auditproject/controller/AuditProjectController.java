package com.bytefinger.toutuo.biz.auditproject.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.auditproject.domain.AuditProject;
import com.bytefinger.toutuo.biz.auditproject.service.IAuditProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 项目管理 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-04
 */
@Slf4j
@Api(tags = "审计项目管理")
@AllArgsConstructor
@RestController
@RequestMapping("/auditProject")
public class AuditProjectController extends BaseController {

    private final IAuditProjectService auditProjectService;

    @ApiOperation(value = "新增-项目管理")
    @PutMapping("/add")
    @RequiresPermissions("biz:auditProject:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody AuditProject auditProject) {
        auditProjectService.add(auditProject);
        return success();
    }

    @ApiOperation(value = "修改-项目管理")
    @PutMapping("/update")
    @RequiresPermissions("biz:auditProject:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AuditProject auditProject) {
        auditProjectService.update(auditProject);
        return success();
    }

    @ApiOperation(value = "删除-项目管理")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:auditProject:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Long id) {
        return success(auditProjectService.removeById(id));
    }

    @ApiOperation(value = "详情-项目管理")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditProject:get")
    public AjaxResult get(@PathVariable("id") Long id) {
        AuditProject auditProject = auditProjectService.findById(id);
        return success(auditProject);
    }

    @ApiOperation(value = "列表-项目管理")
    @GetMapping("/list")
    @RequiresPermissions("biz:auditProject:list")
    public AjaxResult list() {
        return success(auditProjectService.list());
    }

    @ApiOperation(value = "分页（带条件）-项目管理")
    @PostMapping("/page")
    @RequiresPermissions("biz:auditProject:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(auditProjectService.page(queryPage));
    }

}
