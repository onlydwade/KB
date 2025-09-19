package com.bytefinger.toutuo.biz.companyappraise.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.biz.companyappraise.service.IProjectCompanyAppraiseReportService;
import com.bytefinger.toutuo.biz.companyappraise.domain.ProjectCompanyAppraiseReport;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import com.bytefinger.toutuo.biz.companyoperate.domain.ProjectCompanyOperateReport;
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
 * 评价报告 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "评价报告")
@AllArgsConstructor
@RestController
@RequestMapping("/companyAppraise")
public class ProjectCompanyAppraiseReportController extends BaseController {
    private final IProjectCompanyAppraiseReportService projectCompanyAppraiseReportService;

    @ApiOperation(value = "新增-评价报告")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyAppraiseReport projectCompanyAppraiseReport) {
        projectCompanyAppraiseReportService.save(projectCompanyAppraiseReport);
        return success(projectCompanyAppraiseReport);
    }

    @ApiOperation(value = "修改-评价报告")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyAppraiseReport projectCompanyAppraiseReport) {
        return success(projectCompanyAppraiseReportService.updateById(projectCompanyAppraiseReport));
    }

    @ApiOperation(value = "删除-评价报告")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyAppraiseReportService.removeById(id));
    }

    @ApiOperation(value = "详情-评价报告")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyAppraiseReportService.getById(id));
    }

    @ApiOperation(value = "列表-评价报告")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyAppraiseReportService.list());
    }

    @ApiOperation(value = "分页（带条件）-评价报告")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyAppraiseReportService.page(queryPage));
    }

    @ApiOperation(value = "分页（带条件）-股权信息变更记录")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) {
        return success(projectCompanyAppraiseReportService.page(stepMenuId, queryPage));
    }

}
