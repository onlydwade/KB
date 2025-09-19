package com.bytefinger.toutuo.biz.projectextension.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCorrelation;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectOperation;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectCorrelationService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectOperationService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCorrelationVo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 拓后运营项目关联
 *
 * @author ycj
 * @email 
 * @date 2023-03-14 11:05:23
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectCorrelation")
public class ProjectCorrelationController extends BaseController {
    @Autowired
    private IProjectCorrelationService projectCorrelationService;


    @ApiOperation(value = "修改-拓后运营项目关联")
    @PostMapping("/update")
    @RequiresPermissions("biz:projectCorrelation:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCorrelation projectCorrelation) {
        projectCorrelationService.update(projectCorrelation);
        return success(projectCorrelation);
    }


    @ApiOperation(value = "添加-拓后运营项目关联")
    @PostMapping("/add")
    @RequiresPermissions("biz:projectCorrelation:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCorrelation projectCorrelation) {
        projectCorrelationService.add(projectCorrelation);
        return success(projectCorrelation);
    }

    @ApiOperation(value = "在管项目关联详情")
    @GetMapping("/get/{projectId}")
    @RequiresPermissions("biz:projectCorrelation:get")
    public AjaxResult get(@PathVariable("projectId") Long projectId) {
        List<ProjectCorrelationVo> projects = projectCorrelationService.get(projectId);
        return success(projects);
    }

    @ApiOperation(value = "删除-拓后运营项目关联")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:projectCorrelation:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Long id) {
        projectCorrelationService.deleteById(id);
        return success(id);
    }

    @ApiOperation(value = "在管项目下拉查询")
    @PostMapping("/projectRelevance")
    @RequiresPermissions("biz:projectCorrelation:projectRelevance")
    public AjaxResult projectRelevance(@RequestBody ProjectCorrelation projectCorrelation) {
        List<Project> projects = projectCorrelationService.projectRelevance(projectCorrelation);
        return success(projects);
    }


}
