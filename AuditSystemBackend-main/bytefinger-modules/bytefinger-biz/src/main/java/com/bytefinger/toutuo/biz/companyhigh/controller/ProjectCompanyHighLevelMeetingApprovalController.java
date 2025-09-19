package com.bytefinger.toutuo.biz.companyhigh.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import com.bytefinger.toutuo.biz.companyhigh.service.IProjectCompanyHighLevelMeetingApprovalService;
import com.bytefinger.toutuo.biz.companyhigh.domain.ProjectCompanyHighLevelMeetingApproval;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
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
 * 三会记录 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-19
 */
@Slf4j
@Api(tags = "三会记录")
@AllArgsConstructor
@RestController
@RequestMapping("/ompanyHighLevelMeetingApproval")
public class ProjectCompanyHighLevelMeetingApprovalController extends BaseController {
    private final IProjectCompanyHighLevelMeetingApprovalService projectCompanyHighLevelMeetingApprovalService;

    private final IProjectCompanyDocumentTemplateService projectCompanyDocumentTemplateService;

    private final IProjectCompanyDocumentService projectCompanyDocumentService;

    @ApiOperation(value = "新增-三会记录")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyHighLevelMeetingApproval projectCompanyHighLevelMeetingApproval) {
        projectCompanyHighLevelMeetingApprovalService.save(projectCompanyHighLevelMeetingApproval);
        return success(projectCompanyHighLevelMeetingApproval);
    }

    @ApiOperation(value = "修改-三会记录")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyHighLevelMeetingApproval projectCompanyHighLevelMeetingApproval) {
        return success(projectCompanyHighLevelMeetingApprovalService.updateById(projectCompanyHighLevelMeetingApproval));
    }

    @ApiOperation(value = "删除-三会记录")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyHighLevelMeetingApprovalService.removeById(id));
    }

    @ApiOperation(value = "详情-三会记录")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        return success(projectCompanyHighLevelMeetingApprovalService.getById(id));
    }

    @ApiOperation(value = "列表-三会记录")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyHighLevelMeetingApprovalService.list());
    }

    @ApiOperation(value = "分页（带条件）-三会记录")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyHighLevelMeetingApprovalService.page(queryPage));
    }

    @ApiOperation(value = "分页（带条件）-股权信息变更记录")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) {
        return success(projectCompanyHighLevelMeetingApprovalService.page(stepMenuId, queryPage));
    }

}
