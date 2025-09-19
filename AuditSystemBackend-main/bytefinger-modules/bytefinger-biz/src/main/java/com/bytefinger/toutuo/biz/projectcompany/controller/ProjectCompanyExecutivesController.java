package com.bytefinger.toutuo.biz.projectcompany.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyExecutives;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyShareholderLog;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyExecutivesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 股权合作项目-公司高管信息 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-01
 */
@Slf4j
@Api(tags = "股权合作项目-公司高管信息")
@AllArgsConstructor
@RestController
@RequestMapping("/projectCompanyExecutives")
public class ProjectCompanyExecutivesController extends BaseController {
    private final IProjectCompanyExecutivesService projectCompanyExecutivesService;

    private final IProjectCompanyDocumentTemplateService projectCompanyDocumentTemplateService;

    private final IProjectCompanyDocumentService projectCompanyDocumentService;

    @ApiOperation(value = "新增-股权合作项目-公司高管信息")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectCompanyExecutives:add")
    public AjaxResult add(@RequestBody ProjectCompanyExecutives projectCompanyExecutives) {
        projectCompanyExecutivesService.save(projectCompanyExecutives);
        return success(projectCompanyExecutives);
    }

    @ApiOperation(value = "修改-股权合作项目-公司高管信息")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectCompanyExecutives:update")
    public AjaxResult update(@RequestBody ProjectCompanyExecutives projectCompanyExecutives) {
        projectCompanyExecutivesService.updateById(projectCompanyExecutives);
        return success(projectCompanyExecutives);
    }

    @ApiOperation(value = "删除-股权合作项目-公司高管信息")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectCompanyExecutives:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyExecutivesService.removeById(id));
    }

    @ApiOperation(value = "详情-股权合作项目-公司高管信息")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectCompanyExecutives:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyExecutivesService.getById(id));
    }

    @ApiOperation(value = "列表-股权合作项目-公司高管信息")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectCompanyExecutives:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId) {
        List<ProjectCompanyExecutives> executivesList = projectCompanyExecutivesService.list(Wrappers.<ProjectCompanyExecutives>lambdaQuery()
                .eq(ProjectCompanyExecutives::getProjectId, projectId).orderByDesc(ProjectCompanyExecutives::getCreateTime));
        return success(executivesList);
    }
    @ApiOperation(value = "分页（带条件）-股权合作项目-公司高管信息")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectCompanyExecutives:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyExecutivesService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation(value = "分页（带条件）-公司高管信息")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) {
        return success(projectCompanyExecutivesService.page(stepMenuId, queryPage));
    }

}
