package com.bytefinger.toutuo.biz.auditstep.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.auditstep.domain.AuditStep;
import com.bytefinger.toutuo.biz.auditstep.service.IAuditStepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审计步骤完成状态 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "审计步骤完成状态")
@AllArgsConstructor
@RestController
@RequestMapping("/auditStep")
public class AuditStepController extends BaseController {
    private final IAuditStepService auditStepService;

    @ApiOperation(value = "新增-审计步骤完成状态")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:auditStep:add")
    public AjaxResult add(@RequestBody AuditStep auditStep) {
        auditStepService.save(auditStep);
        return success(auditStep);
    }

    @ApiOperation(value = "修改Or新增-审计步骤完成状态")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:auditStep:update")
    public AjaxResult update(@RequestBody AuditStep auditStep) {
        AjaxResult ajaxResult = auditStepService.update(auditStep);
        if (ajaxResult.isError()){
            return ajaxResult;
        }
        return success(auditStep);
    }

    @ApiOperation(value = "删除-审计步骤完成状态")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:auditStep:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(auditStepService.removeById(id));
    }

    @ApiOperation(value = "详情-审计步骤完成状态")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditStep:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditStepService.getById(id));
    }

    @ApiOperation(value = "列表-审计步骤完成状态")
    @GetMapping("/list")
    @RequiresPermissions("biz:auditStep:list")
    public AjaxResult list() {
        return success(auditStepService.list());
    }

    @ApiOperation(value = "分页（带条件）-审计步骤完成状态")
    @PostMapping("/page")
    @RequiresPermissions("biz:auditStep:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(auditStepService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
