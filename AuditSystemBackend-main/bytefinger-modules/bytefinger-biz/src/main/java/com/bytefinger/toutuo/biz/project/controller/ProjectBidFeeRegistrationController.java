package com.bytefinger.toutuo.biz.project.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectAchievement;
import com.bytefinger.toutuo.biz.project.domain.ProjectBidFeeRegistration;
import com.bytefinger.toutuo.biz.project.service.IProjectBidFeeRegistrationService;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目投标费用记录 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "项目投标费用记录")
@AllArgsConstructor
@RestController
@RequestMapping("/projectBidFeeRegistration")
public class ProjectBidFeeRegistrationController extends BaseController {
    private final IProjectBidFeeRegistrationService projectBidFeeRegistrationService;

    private final IProjectService projectService;

    @ApiOperation(value = "新增-项目投标费用记录")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectBidFeeRegistration:add")
    public AjaxResult add(@RequestBody ProjectBidFeeRegistration projectBidFeeRegistration) {
        projectBidFeeRegistrationService.save(projectBidFeeRegistration);
        return success(projectBidFeeRegistration);
    }

    @ApiOperation(value = "修改-项目投标费用记录")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectBidFeeRegistration:update")
    public AjaxResult update(@RequestBody ProjectBidFeeRegistration projectBidFeeRegistration) {
        projectBidFeeRegistrationService.updateById(projectBidFeeRegistration);
        return success(projectBidFeeRegistration);
    }

    @ApiOperation(value = "删除-项目投标费用记录")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectBidFeeRegistration:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectBidFeeRegistrationService.removeById(id));
    }

    @ApiOperation(value = "详情-项目投标费用记录")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectBidFeeRegistration:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectBidFeeRegistrationService.getById(id));
    }

    @ApiOperation(value = "列表-项目投标费用记录")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectBidFeeRegistration:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId){
        return success(projectBidFeeRegistrationService.list(Wrappers.<ProjectBidFeeRegistration>lambdaQuery().eq(ProjectBidFeeRegistration::getProjectId, projectId).orderByDesc(ProjectBidFeeRegistration::getCreateTime)));
    }

    @ApiOperation(value = "分页（带条件）-项目投标费用记录")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectBidFeeRegistration:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        Page<ProjectBidFeeRegistration> page = projectBidFeeRegistrationService.page(queryPage.toPage(), queryPage.getWrapper());
        List<Long> projectIds = page.getRecords().stream().map(ProjectBidFeeRegistration::getProjectId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(projectIds)) {
            List<Project> list = projectService.list(Wrappers.<Project>lambdaQuery().in(Project::getId, projectIds));

            if (!list.isEmpty()) {
                for (ProjectBidFeeRegistration record : page.getRecords()) {
                    for (Project project : list) {
                        if (project.getId().equals(record.getProjectId())) {
                            record.setProjectName(project.getProjectName());
                        }
                    }
                }
            }
        }
        return success(page);
    }

}
