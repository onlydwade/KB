package com.bytefinger.toutuo.biz.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.domain.ProjectPool;
import com.bytefinger.toutuo.biz.project.domain.ProjectRiskInspection;
import com.bytefinger.toutuo.biz.project.service.IProjectRiskInspectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 项目-风险检查 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-01
 */
@Slf4j
@Api(tags = "项目-风险检查")
@AllArgsConstructor
@RestController
@RequestMapping("/projectRiskInspection")
public class ProjectRiskInspectionController extends BaseController {
    private final IProjectRiskInspectionService projectRiskInspectionService;

    @ApiOperation(value = "新增-项目-风险检查")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectRiskInspection:add")
    public AjaxResult add(@RequestBody ProjectRiskInspection projectRiskInspection) {
        projectRiskInspectionService.save(projectRiskInspection);
        return success(projectRiskInspection);
    }

    @ApiOperation(value = "修改-项目-风险检查")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectRiskInspection:update")
    public AjaxResult update(@RequestBody ProjectRiskInspection projectRiskInspection) {
        projectRiskInspectionService.updateById(projectRiskInspection);
        return success(projectRiskInspection);
    }

    @ApiOperation(value = "删除-项目-风险检查")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectRiskInspection:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectRiskInspectionService.removeById(id));
    }

    @ApiOperation(value = "详情-项目-风险检查")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectRiskInspection:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectRiskInspectionService.getById(id));
    }

    @ApiOperation(value = "列表-项目-风险检查")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectRiskInspection:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId) {
        return success(projectRiskInspectionService.list(Wrappers.<ProjectRiskInspection>lambdaQuery().eq(ProjectRiskInspection::getProjectId, projectId)
                .orderByDesc(ProjectRiskInspection::getCreateTime)));
    }
    @ApiOperation(value = "分页（带条件）-项目-风险检查")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectRiskInspection:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectRiskInspectionService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation(value = "", hidden = true)
    @PutMapping("/sendNotifyTask")
    @InnerAuth
    public R sendNotifyTask() {
        projectRiskInspectionService.sendNotifyTask();
        return R.ok();
    }

}
