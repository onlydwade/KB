package com.bytefinger.toutuo.biz.projectextension.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCheck;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectOperateIncome;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectCheckService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectOperateIncomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 经营管理-项目收支
 *
 * @author ycj
 * @email 
 * @date 2023-03-17 14:23:41
 */
@Api(tags = "经营管理-项目收支")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectOperateincome")
public class ProjectOperateIncomeController  extends BaseController {
    @Autowired
    private IProjectOperateIncomeService projectOperateIncomeService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IProjectCheckService projectCheckService;

    @ApiOperation(value = "修改-项目收支")
    @PostMapping("/update")
    @RequiresPermissions("biz:projectOperateincome:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectOperateIncome projectOperateIncome) {
        return success(projectOperateIncomeService.update(projectOperateIncome));
    }

    @ApiOperation(value = "添加-项目收支")
    @PostMapping("/add")
    @RequiresPermissions("biz:projectOperateincome:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectOperateIncome projectOperateIncome) {
        return success(projectOperateIncomeService.add(projectOperateIncome));
    }

    @ApiOperation(value = "删除-项目收支")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:projectOperateincome:delete")
    public AjaxResult get(@PathVariable("id") Long id) {
        return success(projectOperateIncomeService.deleteById(id));
    }

    @ApiOperation(value = "项目收支列表")
    @GetMapping("/list/{projectId}/{stepMenuId}")
    @RequiresPermissions("biz:projectOperateincome:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId,@PathVariable("stepMenuId") Long stepMenuId) {
        List<ProjectOperateIncome> projectOperateIncomeVos = projectOperateIncomeService.list(projectId,stepMenuId);
        return success(projectOperateIncomeVos);
    }

}
