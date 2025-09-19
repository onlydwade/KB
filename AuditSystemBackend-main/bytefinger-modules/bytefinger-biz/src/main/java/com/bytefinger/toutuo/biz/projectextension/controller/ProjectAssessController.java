package com.bytefinger.toutuo.biz.projectextension.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.bytefinger.toutuo.biz.projectextension.service.IProjectAssessService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectCheckService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectAssessVo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 项目评估
 *
 * @author ycj
 * @email
 * @date 2023-03-16 10:18:21
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectAssess")
public class ProjectAssessController extends BaseController {
    @Autowired
    private IProjectAssessService projectAssessService;

    @Autowired
    private IProjectCheckService projectCheckService;

    @Autowired
    public IProjectService projectService;

    @ApiOperation(value = "修改-经营评估")
    @PostMapping("/update")
    @RequiresPermissions("biz:projectAssess:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectAssess projectAssess) {
        return projectAssessService.update(projectAssess);
    }

    @ApiOperation(value = "添加-经营评估")
    @PostMapping("/add")
    @RequiresPermissions("biz:projectAssess:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectAssess projectAssess) {
        return projectAssessService.add(projectAssess);
    }

    @ApiOperation(value = "删除-经营评估")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:projectAssess:add")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Long id) {
        return projectAssessService.deleteById(id);
    }

    @ApiOperation(value = "节点分页（带条件）-经营评估列表")
    @PostMapping("/page/{stepMenuId}")
    @RequiresPermissions("biz:projectAssess:page")
    public AjaxResult page(@RequestBody QueryPage queryPage,@PathVariable("stepMenuId")Long stepMenuId) {
        IPage<ProjectAssess> page = projectAssessService.page(queryPage,stepMenuId);
        return success(page);
    }


    @ApiOperation(value = "详情查看")
    @GetMapping("/get/{stepMenuId}/{id}")
    @RequiresPermissions("biz:projectAssess:get")
    public AjaxResult get(@PathVariable("stepMenuId") Long stepMenuId,@PathVariable("id") Long id  ) {
        ProjectAssess projectAssess = projectAssessService.get(stepMenuId,id);
        return success(projectAssess);
    }


    @ApiOperation(value = "分页列表（带条件）-经营评估列表")
    @PostMapping("/listPage")
    @RequiresPermissions("biz:projectAssess:listPage")
    public AjaxResult listPage(@RequestBody QueryPage queryPage) {
        IPage<Project> page = projectAssessService.projectPage(queryPage);

        page.setRecords(projectService.getYtProjetList(page.getRecords()));

        IPage<ProjectAssessVo> projectAssessVoPage = new Page<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())){
            List<ProjectAssessVo> listPage = projectAssessService.listPage(page.getRecords());
            BeanUtils.copyProperties(page, projectAssessVoPage);
            projectAssessVoPage.setRecords(listPage);
        }
        return success(projectAssessVoPage);
    }

    @ApiOperation(value = "项目干预标题模糊匹配评估")
    @PostMapping("/list")
    @RequiresPermissions("biz:projectAssess:list")
    public AjaxResult listPage(@RequestBody ProjectAssess projectAssess) {
        LambdaQueryWrapper<ProjectAssess> queryWrapper = new LambdaQueryWrapper<ProjectAssess>();
        queryWrapper.eq(ProjectAssess::getDeleted, 0);
        queryWrapper.eq(ProjectAssess::getAssessState, 1);
        if (ObjectUtils.isNotEmpty(projectAssess.getProjectId())){
            queryWrapper.eq(ProjectAssess::getProjectId, projectAssess.getProjectId());
        }
        //匹配标题
        if (ObjectUtils.isNotEmpty(projectAssess.getAssessTitle())){
            queryWrapper.like(ProjectAssess::getAssessTitle, projectAssess.getAssessTitle());
        }
        List<ProjectAssess> list = projectAssessService.list(queryWrapper);
        return success(list);
    }


//    private ProjectAssess fileIds(ProjectAssess projectAssess) {
//        if (ObjectUtils.isNotEmpty(projectAssess.getAssessFileIds())) {
//            projectAssess.setAssessFileId(String.join(",", projectAssess.getAssessFileIds()));
//        }
//        if (ObjectUtils.isNotEmpty(projectAssess.getRestFileIds())) {
//            projectAssess.setRestFileId(String.join(",", projectAssess.getRestFileIds()));
//        }
//        return projectAssess;
//    }

}
