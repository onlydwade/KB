package com.bytefinger.toutuo.biz.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.performance.service.IBudgetInService;
import com.bytefinger.toutuo.biz.project.domain.ProjectAchievement;
import com.bytefinger.toutuo.biz.project.domain.ProjectBid;
import com.bytefinger.toutuo.biz.project.service.IProjectAchievementService;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 项目业绩分配 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "项目业绩分配")
@AllArgsConstructor
@RestController
@RequestMapping("/projectAchievement")
public class ProjectAchievementController extends BaseController {
    private final IProjectAchievementService projectAchievementService;

    private final IProjectService projectService;
    private final IBudgetInService budgetInYearService;

    @ApiOperation(value = "新增-项目业绩分配")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectAchievement:add")
    public AjaxResult add(@RequestBody ProjectAchievement projectAchievement) {
        projectAchievementService.save(projectAchievement);
        Integer currYear= DateUtils.year(new Date());
        budgetInYearService.calcPerformanceAllocationData(  projectAchievement.getProjectId());
        return success(projectAchievement);
    }

    @ApiOperation(value = "修改-项目业绩分配")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectAchievement:update")
    public AjaxResult update(@RequestBody ProjectAchievement projectAchievement) {
        projectAchievementService.updateById(projectAchievement);
        Integer currYear= DateUtils.year(new Date());
        budgetInYearService.calcPerformanceAllocationData( projectAchievement.getProjectId());
        return success(projectAchievement);
    }

    @ApiOperation(value = "删除-项目业绩分配")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectAchievement:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        ProjectAchievement projectAchievement =projectAchievementService.getById(id);
        Boolean flag =projectAchievementService.removeById(id);
        Integer currYear= DateUtils.year(new Date());
        budgetInYearService.calcPerformanceAllocationData( projectAchievement.getProjectId());
        return success(flag);
    }

    @ApiOperation(value = "详情-项目业绩分配")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectAchievement:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectAchievementService.getById(id));
    }

    @ApiOperation(value = "列表-项目业绩分配")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectAchievement:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId){
        return success(projectAchievementService.list(projectId));
    }

    @ApiOperation(value = "分页（带条件）-项目业绩分配")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectAchievement:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectAchievementService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
