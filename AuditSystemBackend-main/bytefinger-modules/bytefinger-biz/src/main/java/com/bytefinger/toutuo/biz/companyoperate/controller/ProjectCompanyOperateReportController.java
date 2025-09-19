package com.bytefinger.toutuo.biz.companyoperate.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import com.bytefinger.toutuo.biz.companyoperate.service.IProjectCompanyOperateReportService;
import com.bytefinger.toutuo.biz.companyoperate.domain.ProjectCompanyOperateReport;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyShareholderLog;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;

/**
 * <p>
 * 经营报告 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "经营报告")
@AllArgsConstructor
@RestController
@RequestMapping("/companyOperateReport")
public class ProjectCompanyOperateReportController extends BaseController {
    private final IProjectCompanyOperateReportService projectCompanyOperateReportService;

    private final IProjectCompanyDocumentTemplateService projectCompanyDocumentTemplateService;

    private final IProjectCompanyDocumentService projectCompanyDocumentService;
    private final IProjectCompanyService projectCompanyService;

    @ApiOperation(value = "新增-经营报告")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyOperateReport projectCompanyOperateReport) {
        projectCompanyOperateReportService.save(projectCompanyOperateReport);
        return success(projectCompanyOperateReport);
    }

    @ApiOperation(value = "修改-经营报告")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyOperateReport projectCompanyOperateReport) {
        return success(projectCompanyOperateReportService.updateById(projectCompanyOperateReport));
    }

    @ApiOperation(value = "删除-经营报告")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyOperateReportService.removeById(id));
    }

    @ApiOperation(value = "详情-经营报告")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        return success(projectCompanyOperateReportService.getByReportId(id));
    }

    @ApiOperation(value = "列表-经营报告")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyOperateReportService.list());
    }

    @ApiOperation(value = "分页（带条件）-经营报告")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyOperateReportService.page(queryPage));
    }

    @ApiOperation(value = "分页（带条件）-股权信息变更记录")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) {
        return success(projectCompanyOperateReportService.page(stepMenuId, queryPage));
    }

}

