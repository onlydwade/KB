package com.bytefinger.toutuo.biz.projectextension.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.project.enums.OAApprovalStatus;
import com.bytefinger.toutuo.biz.interfacelog.service.IInterfaceLogService;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectOperationHistory;
import com.bytefinger.toutuo.biz.project.mapper.ProjectOperationHistoryMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtension;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtensionExit;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectStepExpansion;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectExtensionExitStatus;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectExtensionStatus;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectProcessStatus;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectExtensionExitMapper;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectExtensionMapper;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectInterveneMapper;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionExitService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectStepExpansionService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectExtensionExitVo;
import com.bytefinger.toutuo.biz.projectstep.constants.ProjectStepConstant;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProjectExtensionExitServiceImpl extends BizService<ProjectExtensionExitMapper, ProjectExtensionExit> implements IProjectExtensionExitService {

    @Autowired
    private ProjectInterveneMapper projectInterveneMapper;


    @Autowired
    private ProjectExtensionExitMapper projectExtensionExitMapper;

    @Autowired
    private IProjectService projectService;

    /**
     * 查询全部数据
     * 拼装用户信息
     *
     * @param queryWrapper
     * @return
     */
    @DataFill
    @Override
    public List<ProjectExtensionExit> listQuery(LambdaQueryWrapper<ProjectExtensionExit> queryWrapper) {
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectExtensionExit> add(ProjectExtensionExit projectExtensionExit) {
        projectExtensionExit.setApprovalSponsorTime(new Date());
        return R.ok(super.addExtension4Log(projectExtensionExit,projectExtensionExit.getProjectId()));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectExtensionExit> update(ProjectExtensionExit projectExtensionExit) {

        return R.ok(super.updateExtension4Log(projectExtensionExit,projectExtensionExit.getProjectId()));
    }

    @DataFill
    @Override
    public ProjectExtensionExit get(Long stepMenuId, Long projectId) {
        LambdaQueryWrapper<ProjectExtensionExit> queryWrapper = new LambdaQueryWrapper<ProjectExtensionExit>();
        queryWrapper.eq(ProjectExtensionExit::getProjectId, projectId);
        ProjectExtensionExit projectExtensionExit = this.getOne(queryWrapper);
        Project project = projectService.getById(projectId);

        if (ObjectUtils.isEmpty(projectExtensionExit)) {
            projectExtensionExit = new ProjectExtensionExit();
        }
        this.getProjectExtensionExit(project, projectExtensionExit);


        super.dataProjectFillDcoument(stepMenuId, Lists.newArrayList(projectExtensionExit),null);
        return projectExtensionExit;
    }


    public ProjectExtensionExit getProjectExtensionExit(Project project, ProjectExtensionExit projectExtensionExit) {
        projectExtensionExit.setProjectName(project.getProjectName());
        projectExtensionExit.setCompanyName(project.getCompanyName());
        projectExtensionExit.setBusinessDeptName(project.getBusinessDeptName());
        return projectExtensionExit;
    }


    @DataFill
    @Override
    public IPage<Project> projectPage(QueryPage queryPage) {
        String processState = "extension.processState";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(processState))) {
            Object ob =  queryPage.getParams().get(processState);
            if (ObjectUtils.isNotEmpty(ob)){
                queryPage.getDbParams().put("processState", queryPage.getParams().get(processState));
                queryPage.getParams().remove(processState);
            }
        }
        String serviceContent = "serviceContent";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceContent))) {
            queryPage.getDbParams().put("serviceContent", queryPage.getInParams().get(serviceContent));
            queryPage.getInParams().remove(serviceContent);
        }
        return projectExtensionExitMapper.list(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());
    }

    @Override
    public List<ProjectExtensionExitVo> listPage(List<Project> projects) {
        List<ProjectExtensionExitVo> projectExtensionExitVos = new ArrayList<>();
        LambdaQueryWrapper<ProjectExtensionExit> queryWrapper = new LambdaQueryWrapper<ProjectExtensionExit>();
        queryWrapper.in(ProjectExtensionExit::getProjectId, projects.stream().map(v -> v.getId()).collect(Collectors.toList()));
        List<ProjectExtensionExit> list = this.list(queryWrapper);
        for (Project project : projects) {
            ProjectExtensionExit projectExtensionExit = list.stream().filter(a -> a.getProjectId().equals(project.getId())).findFirst().orElse(new ProjectExtensionExit());
            ProjectExtensionExitVo projectExtensionExitVo = new ProjectExtensionExitVo();

            BeanUtils.copyProperties(project, projectExtensionExitVo);
            if (ObjectUtils.isNotEmpty(projectExtensionExit.getId())) {
                this.getProjectExtensionExit(projectExtensionExitVo,projectExtensionExit);
            }
            projectExtensionExitVos.add(projectExtensionExitVo);
        }
        return projectExtensionExitVos;
    }

    private void getProjectExtensionExit(ProjectExtensionExitVo projectExtensionExitVo,ProjectExtensionExit projectExtensionExit) {
        projectExtensionExitVo.setTitle(projectExtensionExit.getTitle());
        projectExtensionExitVo.setApprovalSendTime(projectExtensionExit.getApprovalSendTime());
        projectExtensionExitVo.setApprovalUrl(projectExtensionExit.getApprovalUrl());
        projectExtensionExitVo.setApprovalUserName(projectExtensionExit.getApprovalUserName());
        projectExtensionExitVo.setContent(projectExtensionExit.getContent());
    }

}