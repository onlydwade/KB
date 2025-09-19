package com.bytefinger.toutuo.biz.projectextension.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.enums.ProjectStatus;
import com.bytefinger.toutuo.biz.project.mapper.ProjectMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCorrelation;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtension;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectCorrelationMapper;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectExtensionMapper;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectCorrelationService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCorrelationVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryVo;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenu;
import com.bytefinger.toutuo.biz.projectstep.mapper.ProjectStepMenuMapper;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProjectCorrelationServiceImpl extends BizService<ProjectCorrelationMapper, ProjectCorrelation> implements IProjectCorrelationService {
    @Autowired
    private IProjectService projectService;

    @Autowired(required = false)
    private IProjectExtensionService projectExtensionService;

    @Autowired(required = false)
    private ProjectExtensionMapper projectExtensionMapper;
    /**
     * 在管关联项目查询
     * @return 结果
     */
    @Override
    public List<Project> projectRelevance(ProjectCorrelation projectCorrelation){
        //在管项目查询 and 排除当前项目
//        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<Project>();
//        QueryPage queryPage = new  QueryPage();
//
//        queryPage.getLikeParams().put("projectName",projectCorrelation.getProjectName());
//        IPage<Project> page = projectExtensionService.list(queryPage);

        QueryPage queryPage = new QueryPage();
        queryPage.setPageSize(Integer.MAX_VALUE);
        queryPage.setPageNo(1);
        if (ObjectUtil.isNotEmpty(projectCorrelation.getProjectName())) {
            queryPage.getDbParams().put("projectName", projectCorrelation.getProjectName());
        }

        Page<Project> projectList = projectExtensionMapper.projectList(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());
//        queryWrapper.eq(Project::getServiceStatus, ProjectStatus.ZAI_GUAN.getCode());
//        //模糊匹配项目名称
//        if (ObjectUtils.isNotEmpty(projectCorrelation.getProjectName())){
//            queryWrapper.like(Project::getProjectName, projectCorrelation.getProjectName());
//            queryWrapper.like(Project::getProjectNo, projectCorrelation.getProjectName());
//        }
//        List<Project> projects = projectService.list(queryWrapper);
        List<Project> projects = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(projectList.getRecords()) && projectList.getRecords().size()>0){
            projects = projectList.getRecords();
            projects = projects.stream().filter(pro -> !pro.getId().equals(projectCorrelation.getProjectId())).collect(Collectors.toList());
        }
        return projects;
    }
    @Override
    public List<ProjectCorrelationVo> get(Long id){
        List<ProjectCorrelationVo> projectCorrelationVos = new ArrayList<>();
        LambdaQueryWrapper<ProjectCorrelation> queryWrapper = new LambdaQueryWrapper<ProjectCorrelation>();
        queryWrapper.eq(ProjectCorrelation::getProjectId, id);
        queryWrapper.eq(ProjectCorrelation::getDeleted, 0);
        List<ProjectCorrelation>  projects = this.list(queryWrapper);
        if (ObjectUtils.isNotEmpty(projects) && ObjectUtils.isNotEmpty(projects.size()>0)){
            LambdaQueryWrapper<ProjectCorrelation> queryWrapperNew = new LambdaQueryWrapper<ProjectCorrelation>();
            queryWrapperNew.eq(ProjectCorrelation::getBatchId, projects.get(0).getBatchId());
            queryWrapperNew.eq(ProjectCorrelation::getDeleted, 0);
            queryWrapperNew.notIn(ProjectCorrelation::getProjectId, Lists.newArrayList(id));
            List<ProjectCorrelation> projectCorrelationList = this.list(queryWrapperNew);

            if (ObjectUtil.isNotEmpty(projectCorrelationList)){
                for (ProjectCorrelation projectCorrelation : projectCorrelationList){
                    ProjectCorrelationVo projectCorrelationVo = new ProjectCorrelationVo();
                    BeanUtils.copyProperties(projectCorrelation, projectCorrelationVo);
                    Project project1 = projectService.getById(projectCorrelation.getProjectId());
                    projectCorrelationVo.setProjectName(ObjectUtil.isNotEmpty(project1.getId())? project1.getProjectName() : "");
                    projectCorrelationVos.add(projectCorrelationVo);
                }
            }
        }
        return projectCorrelationVos;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ProjectCorrelation add(ProjectCorrelation projectCorrelation){
        LambdaQueryWrapper<ProjectCorrelation> queryWrapper = new LambdaQueryWrapper<ProjectCorrelation>();
        queryWrapper.eq(ProjectCorrelation::getProjectId, projectCorrelation.getProjectId());
        queryWrapper.eq(ProjectCorrelation::getDeleted, 0);
        ProjectCorrelation project = this.getOne(queryWrapper);
        //如果有过历史批次，添加一条关联批次项目
        if (ObjectUtils.isNotEmpty(project)){
            ProjectCorrelation projectCorrelationNew = new ProjectCorrelation();
            projectCorrelationNew.setProjectId(projectCorrelation.getProjectRelevantId());
            projectCorrelationNew.setBatchId(project.getBatchId());
            projectCorrelationNew.setDeleted(0);
//            this.save(projectCorrelation);
            super.addExtension4Log(projectCorrelationNew,projectCorrelation.getProjectId());
        }else {
            ProjectCorrelation projectCorrelationNew = new ProjectCorrelation();
            projectCorrelationNew.setBatchId(projectCorrelation.getProjectId());
            projectCorrelationNew.setProjectId(projectCorrelation.getProjectId());
            projectCorrelationNew.setDeleted(0);
            //没有批次id，添加当前项目和关联项目到批次

            super.addExtension4Log(projectCorrelationNew,projectCorrelation.getProjectId());
            projectCorrelationNew.setProjectId(projectCorrelation.getProjectRelevantId());
            super.addExtension4Log(projectCorrelationNew,projectCorrelationNew.getProjectId());
        }
        return projectCorrelation;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ProjectCorrelation update(ProjectCorrelation projectCorrelation){
        projectCorrelation.setProjectId(projectCorrelation.getProjectRelevantId());

        super.updateExtension4Log(projectCorrelation,projectCorrelation.getProjectId());
        return projectCorrelation;
    }

    @Override
    public R deleteById(Long id) {
        ProjectCorrelation projectCorrelation = this.getById(id);
        //  逻辑删除
        super.deleteExtension(projectCorrelation,null,projectCorrelation.getProjectId());
//        // 记录关联状态状态修改
//        operLogService.changeLog(ProjectRiskManagement.class, id, "风险被删除", null, null);
        return R.ok();
    }
}

