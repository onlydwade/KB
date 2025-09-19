package com.bytefinger.toutuo.biz.projectextension.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.interfacelog.service.IInterfaceLogService;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectOperationHistory;
import com.bytefinger.toutuo.biz.project.mapper.ProjectOperationHistoryMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtension;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtensionExit;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectIntervene;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectStepExpansion;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectExtensionExitStatus;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectExtensionStatus;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionExitService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectStepExpansionService;
import com.bytefinger.toutuo.biz.projectextension.service.impl.ProjectExtensionServiceImpl;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectExtensionExitVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectInterveneVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryVo;
import com.bytefinger.toutuo.biz.projectstep.constants.ProjectStepConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * 拓后项目退场
 *
 * @author ycj
 * @email 
 * @date 2023-03-27 16:23:19
 */
@Api(tags = "拓后项目退场")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectExtensionExit")
public class ProjectExtensionExitController extends BaseController {
    @Autowired
    private IProjectExtensionExitService projectExtensionExitService;
    @Autowired
    private IProjectExtensionService projectExtensionService;
    @Autowired
    public IProjectService projectService;
    @Autowired
    private ProjectExtensionServiceImpl extensionService;
    @Autowired
    private IProjectStepExpansionService projectStepExpansionService;
    @Autowired
    private final ProjectOperationHistoryMapper projectOperationHistoryMapper;
    @Autowired
    private final IInterfaceLogService iInterfaceLogService;

    @ApiOperation(value = "修改-退场管理")
    @PostMapping("/update")
    @RequiresPermissions("biz:projectExtensionExit:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectExtensionExit projectExtensionExit) {
        LambdaQueryWrapper<ProjectExtension> queryWrapper = new LambdaQueryWrapper<ProjectExtension>();
        queryWrapper.eq(ProjectExtension::getProjectId, projectExtensionExit.getProjectId());
        List<ProjectExtension> projectExtensions = projectExtensionService.list(queryWrapper);
        //如果是暂存就不用更新状态
        if (ObjectUtils.isNotEmpty(projectExtensionExit.getTemporary())){
            //发起审批后，系统将把该拓后项目的“处理状态”字段对应将更新为“审批中”，“处理方式”列更新为“退场”
            if (ObjectUtils.isNotEmpty(projectExtensions) && projectExtensions.size()>0){
                if (projectExtensions.stream().filter(pro->pro.getProcessState().equals(ProjectExtensionExitStatus.YI_CHU_LI.getCode())).count()>0){
                    //包含已处理状态，不更新状态与审批流
                    projectExtensionExit.setApprovalStatus(null);
                    projectExtensionExit.setApprovalUrl(null);
                }
                ProjectExtension projectExtension = projectExtensions.get(0);
//                projectExtension.setProcessState(ProjectExtensionExitStatus.SHEN_PI_ZHONG.getCode());
                projectExtension.setProcessMode(ProjectExtensionStatus.TUI_CHANG.getCode());
                projectExtensionService.updateById(projectExtension);
            }else {
                ProjectExtension projectExtension = new ProjectExtension();
                projectExtension.setProjectId(projectExtensionExit.getProjectId());
                projectExtension.setProcessState(ProjectExtensionExitStatus.SHEN_PI_ZHONG.getCode());
                projectExtension.setProcessMode(ProjectExtensionStatus.TUI_CHANG.getCode());
                projectExtensionService.save(projectExtension);
            }
        }

        return success(projectExtensionExitService.update(projectExtensionExit));
    }

    @ApiOperation(value = "添加-退场管理")
    @PostMapping("/add")
    @RequiresPermissions("biz:projectExtensionExit:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult addTransmit(@RequestBody ProjectExtensionExit projectExtensionExit) {
        LambdaQueryWrapper<ProjectExtension> queryWrapper = new LambdaQueryWrapper<ProjectExtension>();
        queryWrapper.eq(ProjectExtension::getProjectId, projectExtensionExit.getProjectId());
        List<ProjectExtension> projectExtensions = projectExtensionService.list(queryWrapper);
        //如果是暂存就不用更新状态
        if (ObjectUtils.isNotEmpty(projectExtensionExit.getTemporary())){
            //发起审批后，系统将把该拓后项目的“处理状态”字段对应将更新为“审批中”，“处理方式”列更新为“退场”
            if (ObjectUtils.isNotEmpty(projectExtensions) && projectExtensions.size()>0){
                ProjectExtension projectExtension = projectExtensions.get(0);
                projectExtension.setProcessState(ProjectExtensionExitStatus.SHEN_PI_ZHONG.getCode());
                projectExtension.setProcessMode(ProjectExtensionStatus.TUI_CHANG.getCode());
                projectExtensionService.updateById(projectExtension);
            }else {
                ProjectExtension projectExtension = new ProjectExtension();
                projectExtension.setProjectId(projectExtensionExit.getProjectId());
                projectExtension.setProcessState(ProjectExtensionExitStatus.SHEN_PI_ZHONG.getCode());
                projectExtension.setProcessMode(ProjectExtensionStatus.TUI_CHANG.getCode());
                projectExtensionService.save(projectExtension);
            }
        }
        return success(projectExtensionExitService.add(projectExtensionExit));
    }

    @ApiOperation(value = "节点分页（带条件）-退场管理列表")
    @GetMapping("/get/{stepMenuId}/{projectId}")
    @RequiresPermissions("biz:projectExtensionExit:get")
    public AjaxResult page(@PathVariable("stepMenuId")Long stepMenuId,@PathVariable("projectId")Long projectId) {
        ProjectExtensionExit projectExtensionExit = projectExtensionExitService.get(stepMenuId,projectId);
        return success(projectExtensionExit);
    }

    @ApiOperation(value = "分页列表（带条件）-退场管理列表")
    @PostMapping("/listPage")
    @RequiresPermissions("biz:projectExtensionExit:listPage")
    public AjaxResult listPage(@RequestBody QueryPage queryPage) {
        IPage<Project> page = projectExtensionExitService.projectPage(queryPage);

        page.setRecords(projectService.getYtProjetList(page.getRecords()));

        IPage<ProjectQueryVo> projectExtensionExitVoIPage = new Page<>();
        BeanUtils.copyProperties(page, projectExtensionExitVoIPage);
        if (ObjectUtils.isNotEmpty(page.getRecords())){
            List<ProjectQueryVo> projectQueryVos = projectExtensionService.page(page.getRecords());
            projectExtensionExitVoIPage.setRecords(projectQueryVos);
        }
        return success(projectExtensionExitVoIPage);
    }


    @ApiOperation(value = "退场线下审批通过")
    @PostMapping("/exitOfflinePass")
    @RequiresPermissions("biz:projectExtensionExit:listPage")
    public AjaxResult exitOfflinePass(@RequestBody ProjectExtensionExit projectExtensionExit) {
        projectExtensionExit.setApprovalSponsorTime(new Date());
        projectExtensionExit.setApprovalStatus(8); //线下审批通过
        this.projectExtensionExitService.saveOrUpdate(projectExtensionExit);

        //更新项目状态
        Project project = projectService.getById(projectExtensionExit.getProjectId());
        project.setServiceStatus("YI_TUI_CHANG");
        projectService.updateById(project);

        //退场节点完成
        ProjectStepExpansion projectStepExpansion = new ProjectStepExpansion();
        projectStepExpansion.setProjectId(projectExtensionExit.getProjectId());
        projectStepExpansion.setStepMenuId(51L);
        projectStepExpansion.setStatus(1);
        projectStepExpansionService.update(projectStepExpansion);

        //迭代二：退场，发起报销申请流程
        if (ProjectStepConstant.DAN_YI_TOU_BIAO_XIANG_MU.equals(project.getProjectType())) {
            iInterfaceLogService.sentReimbursementApplication( project.getId().toString(),"YES");
        }

        LambdaQueryWrapper<ProjectExtension> queryWrapper = new LambdaQueryWrapper<ProjectExtension>();
        queryWrapper.eq(ProjectExtension::getProjectId, projectExtensionExit.getProjectId());
        List<ProjectExtension> projectExtensions = extensionService.list(queryWrapper);
        //“处理状态”更新为“审批中”，“处理方式”更新为“退场”
        if (ObjectUtils.isNotEmpty(projectExtensions) && projectExtensions.size() > 0) {
            ProjectExtension projectExtension = projectExtensions.get(0);
            projectExtension.setProcessState(ProjectExtensionExitStatus.YI_CHU_LI.getCode());
            projectExtension.setProcessMode(ProjectExtensionStatus.TUI_CHANG.getCode());
            extensionService.updateById(projectExtension);
        } else {
            ProjectExtension projectExtension = new ProjectExtension();
            projectExtension.setProjectId(projectExtensionExit.getProjectId());
            projectExtension.setProcessState(ProjectExtensionExitStatus.YI_CHU_LI.getCode());
            projectExtension.setProcessMode(ProjectExtensionStatus.TUI_CHANG.getCode());
            extensionService.save(projectExtension);
        }

        //记录到操作历史
        ProjectOperationHistory operationHistory = new ProjectOperationHistory();
        operationHistory.setOldStatus(project.getServiceStatus());
        operationHistory.setOldProjectId(project.getId());
        operationHistory.setOperationType("YI_TUI_CHANG");
        operationHistory.setOperationTime(new Date());
        operationHistory.setOperationMan(SecurityUtils.getUserId());
        operationHistory.setDeleted(0);
        projectOperationHistoryMapper.insert(operationHistory);


        return success("成功");
    }

    @ApiOperation(value = "退场暂存")
    @PostMapping("/storage")
    public AjaxResult storage(@RequestBody ProjectExtensionExit projectExtensionExit) {
        projectExtensionExit.setApprovalSponsorTime(new Date());
        projectExtensionExitService.saveOrUpdate(projectExtensionExit);

        return success("成功");
    }

}
