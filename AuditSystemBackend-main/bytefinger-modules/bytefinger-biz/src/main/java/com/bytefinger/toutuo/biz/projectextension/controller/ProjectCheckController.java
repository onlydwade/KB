package com.bytefinger.toutuo.biz.projectextension.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectOperation;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectCheckService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCheckVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectOperationVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryVo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 承接查验
 *
 * @author ycj
 * @email 
 * @date 2023-03-15 14:44:28
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectCheck")
public class ProjectCheckController extends BaseController {
    @Autowired
    public  IProjectService projectService;

    @Autowired
    private IProjectCheckService projectCheckService;

    @ApiOperation(value = "修改-承接查验")
    @PostMapping("/update")
    @RequiresPermissions("biz:projectCheck:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCheck projectCheck) {
        return success(projectCheckService.update(projectCheck));
    }


    @ApiOperation(value = "添加-承接查验")
    @PostMapping("/add")
    @RequiresPermissions("biz:projectCheck:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCheck projectCheck) {
        return success(projectCheckService.add(projectCheck));
    }

    @ApiOperation(value = "查询-承接查验节点详情")
    @GetMapping("/get/{projectId}")
    @RequiresPermissions("biz:projectCheck:get")
    public AjaxResult get(@PathVariable("projectId") Long projectId) {
        ProjectCheckVo projectCheckVo =  projectCheckService.get(projectId);
        return success(projectCheckVo);
    }


    @ApiOperation(value = "分页（带条件）-查验列表")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectCheck:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        IPage<Project> page = projectCheckService.projectPage(queryPage);

        page.setRecords(projectService.getYtProjetList(page.getRecords()));

        List<ProjectQueryVo> projectQueryVos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())){
             projectQueryVos = projectCheckService.page(page.getRecords());
        }
        IPage<ProjectQueryVo> projectQueryVoIPage = new Page<>();
        BeanUtils.copyProperties(page, projectQueryVoIPage);
        projectQueryVoIPage.setRecords(projectQueryVos);
        return success(projectQueryVoIPage);
    }
}

