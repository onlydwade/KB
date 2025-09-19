package com.bytefinger.toutuo.biz.companyrisk.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.toutuo.biz.companyrisk.domain.ProjectCompanyRiskInspectionApproval;
import com.bytefinger.toutuo.biz.companyrisk.service.IProjectCompanyRiskInspectionApprovalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 风险处理 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-19
 */
@Slf4j
@Api(tags = "风险处理")
@AllArgsConstructor
@RestController
@RequestMapping("/companyrisk")
public class ProjectCompanyRiskInspectionApprovalController extends BaseController {
    private final IProjectCompanyRiskInspectionApprovalService projectCompanyRiskInspectionApprovalService;

    @ApiOperation(value = "新增-风险处理")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyRiskInspectionApproval projectCompanyRiskInspectionApproval) {
        projectCompanyRiskInspectionApprovalService.save(projectCompanyRiskInspectionApproval);
        return success(projectCompanyRiskInspectionApproval);
    }

    @ApiOperation(value = "修改-风险处理")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyRiskInspectionApproval projectCompanyRiskInspectionApproval) {
        return success(projectCompanyRiskInspectionApprovalService.updateById(projectCompanyRiskInspectionApproval));
    }

    @ApiOperation(value = "删除-风险处理")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Long id) {
        return success(projectCompanyRiskInspectionApprovalService.removeById(id));
    }

    @ApiOperation(value = "详情-风险处理")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        return success(projectCompanyRiskInspectionApprovalService.getById(id));
    }

    @ApiOperation(value = "列表-风险处理")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyRiskInspectionApprovalService.list());
    }

    @ApiOperation(value = "分页（带条件）-风险处理")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyRiskInspectionApprovalService.page(queryPage));
    }

    @ApiOperation(value = "分页（带条件）-风险处理")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) {
        return success(projectCompanyRiskInspectionApprovalService.page(stepMenuId, queryPage));
    }

}
