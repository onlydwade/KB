package com.bytefinger.toutuo.biz.companycoordination.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.toutuo.biz.companycoordination.domain.ProjectCompanyDailyCoordination;
import com.bytefinger.toutuo.biz.companycoordination.service.IProjectCompanyDailyCoordinationService;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "日常协调")
@AllArgsConstructor
@RestController
@RequestMapping("/companyCoordination")
public class ProjectCompanyDailyCoordinationController extends BaseController {
    private final IProjectCompanyDailyCoordinationService projectCompanyDailyCoordinationService;

    @ApiOperation(value = "新增")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyDailyCoordination projectCompanyDailyCoordination) {
        projectCompanyDailyCoordinationService.save(projectCompanyDailyCoordination);
        return success(projectCompanyDailyCoordination);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyDailyCoordination projectCompanyDailyCoordination) {
        return success(projectCompanyDailyCoordinationService.updateById(projectCompanyDailyCoordination));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyDailyCoordinationService.removeById(id));
    }

    @ApiOperation(value = "详情")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyDailyCoordinationService.getById(id));
    }

    @ApiOperation(value = "列表")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyDailyCoordinationService.list());
    }

    @ApiOperation(value = "分页（带条件）")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyDailyCoordinationService.page(queryPage));
    }
    @ApiOperation(value = "分页（带条件）")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) {
        return success(projectCompanyDailyCoordinationService.page(stepMenuId, queryPage));
    }

}
