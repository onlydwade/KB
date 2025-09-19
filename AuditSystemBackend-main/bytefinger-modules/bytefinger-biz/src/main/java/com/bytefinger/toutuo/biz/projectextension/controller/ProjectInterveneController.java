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
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectAssess;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectIntervene;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectInterveneService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectAssessVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectInterveneVo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 拓后运营项目干预
 *
 * @author ycj
 * @email
 * @date 2023-03-21 16:14:17
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectIntervene")
public class ProjectInterveneController extends BaseController {
    @Autowired
    private IProjectInterveneService projectInterveneService;
    @Autowired
    public IProjectService projectService;

    @ApiOperation(value = "修改-反馈执行情况")
    @PostMapping("/update")
    @RequiresPermissions("biz:projectIntervene:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectIntervene projectIntervene) {
        return projectInterveneService.update(projectIntervene);
    }

    @ApiOperation(value = "添加-下达项目干预")
    @PostMapping("/addTransmit")
    @RequiresPermissions("biz:projectIntervene:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult addTransmit(@RequestBody ProjectIntervene projectIntervene) {
        return projectInterveneService.add(projectIntervene);
    }


    @ApiOperation(value = "通过或不通过")
    @PostMapping("/isNotPass")
    @RequiresPermissions("biz:projectIntervene:isNotPass")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult isNotPass(@RequestBody ProjectIntervene projectIntervene) {
        return success(projectInterveneService.isNotPass(projectIntervene));
    }

    @ApiOperation(value = "节点分页（带条件）-项目干预列表")
    @PostMapping("/page/{stepMenuId}")
    @RequiresPermissions("biz:projectIntervene:page")
    public AjaxResult page(@RequestBody QueryPage queryPage,@PathVariable("stepMenuId")Long stepMenuId) {
        IPage<ProjectIntervene> page = projectInterveneService.page(queryPage,stepMenuId);
        return success(page);
    }

    @ApiOperation(value = "分页列表（带条件）-项目干预列表")
    @PostMapping("/listPage")
    @RequiresPermissions("biz:projectIntervene:listPage")
    public AjaxResult listPage(@RequestBody QueryPage queryPage) {
        IPage<Project> page = projectInterveneService.projectPage(queryPage);

        page.setRecords(projectService.getYtProjetList(page.getRecords()));

        IPage<ProjectInterveneVo> projectInterveneVoIPage = new Page<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())){
            List<ProjectInterveneVo> listPage = projectInterveneService.listPage(page.getRecords());
            BeanUtils.copyProperties(page, projectInterveneVoIPage);
            projectInterveneVoIPage.setRecords(listPage);
        }
        return success(projectInterveneVoIPage);
    }

    @ApiOperation(value = "详情查看")
    @GetMapping("/get/{stepMenuId}/{id}")
    @RequiresPermissions("biz:projectAssess:get")
    public AjaxResult get(@PathVariable("stepMenuId") Long stepMenuId,@PathVariable("id") Long id  ) {
        ProjectIntervene projectIntervene = projectInterveneService.get(stepMenuId,id);
        if (ObjectUtils.isNotEmpty(projectIntervene.getSchemeUser())){
            projectIntervene.setSchemeUserName(projectIntervene.getSchemeUser().getRealname());
        }

        return success(projectIntervene);
    }
}
