package com.bytefinger.toutuo.biz.projectextension.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectServeSatisfaction;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectRiskManagementService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 拓后运营-风险管理
 *
 * @author ycj
 * @email 
 * @date 2023-03-20
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectRiskManagement")
public class ProjectRiskManagementController extends BaseController {

    private final IProjectRiskManagementService projectRiskManagementService;


    @ApiOperation(value = "修改-风险管理")
    @PostMapping("/update")
    @RequiresPermissions("biz:projectRiskManagement:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectRiskManagement projectRiskManagement) {
        return success(projectRiskManagementService.update(projectRiskManagement));
    }

    @ApiOperation(value = "添加-风险管理")
    @PostMapping("/add")
    @RequiresPermissions("biz:projectRiskManagement:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectRiskManagement projectRiskManagement) {
        return projectRiskManagementService.add(projectRiskManagement);
    }

    @ApiOperation(value = "删除-风险管理")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:projectRiskManagement:delete")
    public AjaxResult get(@PathVariable("id") Long id) {
        return success(projectRiskManagementService.deleteById(id));
    }

    @ApiOperation(value = "风险管理列表")
    @GetMapping("/list/{projectId}/{stepMenuId}")
    @RequiresPermissions("biz:projectRiskManagement:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId,@PathVariable("stepMenuId") Long stepMenuId) {

        List<ProjectRiskManagement> serveSatisfactionList = projectRiskManagementService.listQuery(projectId,stepMenuId);
        return success(serveSatisfactionList);
    }
}
