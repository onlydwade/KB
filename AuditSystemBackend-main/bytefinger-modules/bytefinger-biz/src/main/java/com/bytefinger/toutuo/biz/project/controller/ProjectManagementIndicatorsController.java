package com.bytefinger.toutuo.biz.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.domain.ProjectEntrust;
import com.bytefinger.toutuo.biz.project.domain.ProjectManagementIndicators;
import com.bytefinger.toutuo.biz.project.service.IProjectManagementIndicatorsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 项目经营指标 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "项目经营指标")
@AllArgsConstructor
@RestController
@RequestMapping("/projectManagementIndicators")
public class ProjectManagementIndicatorsController extends BaseController {
    private final IProjectManagementIndicatorsService projectManagementIndicatorsService;

    @ApiOperation(value = "新增-项目经营指标")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectManagementIndicators:add")
    public AjaxResult add(@RequestBody ProjectManagementIndicators projectManagementIndicators) {
        projectManagementIndicatorsService.save(projectManagementIndicators);
        return success(projectManagementIndicators);
    }

    @ApiOperation(value = "修改-项目经营指标")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectManagementIndicators:update")
    public AjaxResult update(@RequestBody ProjectManagementIndicators projectManagementIndicators) {
        projectManagementIndicatorsService.updateById(projectManagementIndicators);
        return success(projectManagementIndicators);
    }

    @ApiOperation(value = "删除-项目经营指标")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectManagementIndicators:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectManagementIndicatorsService.removeById(id));
    }

    @ApiOperation(value = "详情-项目经营指标")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectManagementIndicators:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectManagementIndicatorsService.getById(id));
    }

    @ApiOperation(value = "列表-项目经营指标")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectManagementIndicators:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId) {
        return success(projectManagementIndicatorsService.list(Wrappers.<ProjectManagementIndicators>lambdaQuery().eq(ProjectManagementIndicators::getProjectId, projectId)));
    }
    @ApiOperation(value = "分页（带条件）-项目经营指标")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectManagementIndicators:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectManagementIndicatorsService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
