package com.bytefinger.toutuo.biz.projectextension.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.core.enums.ShiFou;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectTeam;
import com.bytefinger.toutuo.biz.project.mapper.ProjectTeamMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentService;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentTemplateService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectAssess;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectIntervene;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectAssessLabel;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectAssessMapper;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectInterveneMapper;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectAssessService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectAssessSuggestVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectAssessVo;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepMenuService;
import com.bytefinger.toutuo.biz.user.service.UserService;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProjectAssessServiceImpl extends BizService<ProjectAssessMapper, ProjectAssess> implements IProjectAssessService {
    @Autowired
    private IAuditDocumentService projectDocumentService;
    @Autowired
    private IProjectStepMenuService projectStepMenuService;
    @Autowired
    private IAuditDocumentTemplateService projectDocumentTemplateService;

    @Autowired
    private ProjectAssessMapper projectAssessMapper;

    private final ProjectInterveneMapper projectInterveneMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private IProjectService projectService;

    private final ProjectTeamMapper projectTeamMapper;

    @Override
    public IPage<ProjectAssess> page(QueryPage queryPage, Long stepMenuId) {
        //查询属于的节点
        IPage<ProjectAssess> page = this.page(queryPage.toPage(), queryPage.getWrapper());
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            SimpleDateFormat simpleEndDateStart = new SimpleDateFormat("yyyy-MM-dd");
            for (ProjectAssess projectAssess : page.getRecords()) {
                List<ProjectAssessSuggestVo> projectAssessSuggestVos = new ArrayList<>();

                Project project = projectService.getById(projectAssess.getProjectId());
                projectAssess.setAssessDeadline(simpleEndDateStart.format(DateUtils.addAndSubtractDate(project.getServiceBeginTime(), 1, null, 15)));

                List<ProjectAssessLabel> label = Lists.newArrayList(ProjectAssessLabel.values());
                for (ProjectAssessLabel projectAssessLabel : label) {
                    ProjectAssessSuggestVo projectAssessSuggestVo = new ProjectAssessSuggestVo();
                    projectAssessSuggestVo.setLabel(projectAssessLabel.getDesc());
                    if (projectAssessLabel.getDesc().equals(ProjectAssessLabel.law.getDesc())) {
                        projectAssessSuggestVo.setImproveSuggest(projectAssess.getImproveSuggestLaw());
                        projectAssessSuggestVo.setRiskDescribe(projectAssess.getRiskDescribeLaw());
                    }
                    if (projectAssessLabel.getDesc().equals(ProjectAssessLabel.hidden.getDesc())) {
                        projectAssessSuggestVo.setImproveSuggest(projectAssess.getImproveSuggestHidden());
                        projectAssessSuggestVo.setRiskDescribe(projectAssess.getRiskDescribeHidden());
                    }
                    if (projectAssessLabel.getDesc().equals(ProjectAssessLabel.contract.getDesc())) {
                        projectAssessSuggestVo.setImproveSuggest(projectAssess.getImproveSuggestContract());
                        projectAssessSuggestVo.setRiskDescribe(projectAssess.getRiskDescribeContract());
                    }
                    projectAssessSuggestVos.add(projectAssessSuggestVo);
                }
                projectAssess.setProjectAssessSuggestVos(projectAssessSuggestVos);
                UserVO userVO = userService.getUser(projectAssess.getAssessUserId());
                if (ObjectUtils.isNotEmpty(userVO)) {
                    projectAssess.setAssessUserName(userVO.getRealname());
                }

            }
        }
        super.dataProjectFillDcoument(stepMenuId, Lists.newArrayList(page.getRecords()), null);
        return page;
    }

    @Override
    @DataFill
    public ProjectAssess get(Long stepMenuId, Long id) {
        ProjectAssess projectAssess = this.getById(id);
        List<ProjectAssessSuggestVo> projectAssessSuggestVos = new ArrayList<>();
//                projectAssessSuggestVos.
        List<ProjectAssessLabel> label = Lists.newArrayList(ProjectAssessLabel.values());
        for (ProjectAssessLabel projectAssessLabel : label) {
            ProjectAssessSuggestVo projectAssessSuggestVo = new ProjectAssessSuggestVo();
            projectAssessSuggestVo.setLabel(projectAssessLabel.getDesc());
            if (projectAssessLabel.getDesc().equals(ProjectAssessLabel.law.getDesc())) {
                projectAssessSuggestVo.setImproveSuggest(projectAssess.getImproveSuggestLaw());
                projectAssessSuggestVo.setRiskDescribe(projectAssess.getRiskDescribeLaw());
            }
            if (projectAssessLabel.getDesc().equals(ProjectAssessLabel.hidden.getDesc())) {
                projectAssessSuggestVo.setImproveSuggest(projectAssess.getImproveSuggestHidden());
                projectAssessSuggestVo.setRiskDescribe(projectAssess.getRiskDescribeHidden());
            }
            if (projectAssessLabel.getDesc().equals(ProjectAssessLabel.contract.getDesc())) {
                projectAssessSuggestVo.setImproveSuggest(projectAssess.getImproveSuggestContract());
                projectAssessSuggestVo.setRiskDescribe(projectAssess.getRiskDescribeContract());
            }
            projectAssessSuggestVos.add(projectAssessSuggestVo);
        }
        projectAssess.setProjectAssessSuggestVos(projectAssessSuggestVos);
        super.dataProjectFillDcoument(stepMenuId, Lists.newArrayList(projectAssess), null);
        return projectAssess;
    }

    @Override
    public List<ProjectAssessVo> listPage(List<Project> projects) {
        List<ProjectAssessVo> projectAssessVos = new ArrayList<>();
        LambdaQueryWrapper<ProjectAssess> queryWrapper = new LambdaQueryWrapper<ProjectAssess>();
        queryWrapper.in(ProjectAssess::getProjectId, projects.stream().map(v -> v.getId()).collect(Collectors.toList()));
        queryWrapper.eq(ProjectAssess::getDeleted, 0);
        List<ProjectAssess> assessList = this.list(queryWrapper);
        SimpleDateFormat simpleEndDateStart = new SimpleDateFormat("yyyy-MM-dd");
        for (Project project : projects) {
            //
            ProjectAssess projectAssess = new ProjectAssess();
            List<ProjectAssess> projectAssessList = assessList.stream().filter(a -> a.getProjectId().equals(project.getId())).collect(Collectors.toList());
            if (ObjectUtils.isNotEmpty(projectAssessList) && projectAssessList.size() > 0) {
                projectAssess = projectAssessList.stream().max(Comparator.comparing(ProjectAssess::getAssessTime)).orElse(new ProjectAssess());
            }
            projectAssess.setAssessDeadline(simpleEndDateStart.format(DateUtils.addAndSubtractDate(project.getServiceBeginTime(), 1, null, 15)));
            ProjectAssessVo projectAssessVo = new ProjectAssessVo();
            BeanUtils.copyProperties(project, projectAssessVo);
            this.getProAssessVo(projectAssess, projectAssessVo);
            //当前登入人是否是项目归属人、拓后负责人、项目团队成员、创建人
            //项目团队成员
            List<ProjectTeam> projectTeamList =  projectTeamMapper.selectList(Wrappers.<ProjectTeam>lambdaQuery().eq(ProjectTeam::getRecordId, project.getId()).eq(ProjectTeam::getUserId,SecurityUtils.getUserId()));
            if((ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(projectTeamList) && projectTeamList.size()>0)){
                projectAssessVo.setShow(true);
            }else {
                projectAssessVo.setShow(false);
            }
            projectAssessVos.add(projectAssessVo);
        }
        return projectAssessVos;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult add(ProjectAssess projectAssess) {
        List<ProjectAssess> projectAssesses = super.list(Wrappers.<ProjectAssess>lambdaQuery().eq(ProjectAssess::getProjectId, projectAssess.getProjectId()).eq(ProjectAssess::getAssessTitle, projectAssess.getAssessTitle()).eq(ProjectAssess::getDeleted, 0));
        if (projectAssesses.size() > 0) {
            return R.fail("该评估标题已经存在").toAjaxResult();
        }
        if (ObjectUtils.isNotEmpty(projectAssess.getProjectAssessSuggestVos())) {
            List<ProjectAssessSuggestVo> projectAssessSuggestVos = projectAssess.getProjectAssessSuggestVos();
            for (ProjectAssessSuggestVo projectAssessSuggestVo : projectAssessSuggestVos) {
                if (projectAssessSuggestVo.getLabel().equals(ProjectAssessLabel.law.getDesc())) {
                    projectAssess.setImproveSuggestLaw(projectAssessSuggestVo.getImproveSuggest());
                    projectAssess.setRiskDescribeLaw(projectAssessSuggestVo.getRiskDescribe());
                }
                if (projectAssessSuggestVo.getLabel().equals(ProjectAssessLabel.hidden.getDesc())) {
                    projectAssess.setImproveSuggestHidden(projectAssessSuggestVo.getImproveSuggest());
                    projectAssess.setRiskDescribeHidden(projectAssessSuggestVo.getRiskDescribe());
                }
                if (projectAssessSuggestVo.getLabel().equals(ProjectAssessLabel.contract.getDesc())) {
                    projectAssess.setImproveSuggestContract(projectAssessSuggestVo.getImproveSuggest());
                    projectAssess.setRiskDescribeContract(projectAssessSuggestVo.getRiskDescribe());
                }
            }
        }
        projectAssess.setDeleted(0);
        projectAssess.setAssessUserId(SecurityUtils.getUserId());
        projectAssess.setAssessUserName(SecurityUtils.getRealname());
        projectAssess.setAssessTime(new Date());
        projectAssess.setTransmitAlreadyState(0);
        return R.ok(super.addExtension4Log(projectAssess, projectAssess.getProjectId())).toAjaxResult();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult update(ProjectAssess projectAssess) {
        List<ProjectAssess> projectAssesses = super.list(Wrappers.<ProjectAssess>lambdaQuery().eq(ProjectAssess::getProjectId, projectAssess.getProjectId()).eq(ProjectAssess::getAssessTitle, projectAssess.getAssessTitle()).eq(ProjectAssess::getDeleted, 0).notIn(ProjectAssess::getId, Lists.newArrayList(projectAssess.getId())));
        if (projectAssesses.size() > 0) {
            return R.fail("该评估标题已经存在").toAjaxResult();
        }
        if (ObjectUtils.isNotEmpty(projectAssess.getProjectAssessSuggestVos())) {
            List<ProjectAssessSuggestVo> projectAssessSuggestVos = projectAssess.getProjectAssessSuggestVos();
            for (ProjectAssessSuggestVo projectAssessSuggestVo : projectAssessSuggestVos) {
                if (projectAssessSuggestVo.getLabel().equals(ProjectAssessLabel.law.getDesc())) {
                    projectAssess.setImproveSuggestLaw(projectAssessSuggestVo.getImproveSuggest());
                    projectAssess.setRiskDescribeLaw(projectAssessSuggestVo.getRiskDescribe());
                }
                if (projectAssessSuggestVo.getLabel().equals(ProjectAssessLabel.hidden.getDesc())) {
                    projectAssess.setImproveSuggestHidden(projectAssessSuggestVo.getImproveSuggest());
                    projectAssess.setRiskDescribeHidden(projectAssessSuggestVo.getRiskDescribe());
                }
                if (projectAssessSuggestVo.getLabel().equals(ProjectAssessLabel.contract.getDesc())) {
                    projectAssess.setImproveSuggestContract(projectAssessSuggestVo.getImproveSuggest());
                    projectAssess.setRiskDescribeContract(projectAssessSuggestVo.getRiskDescribe());
                }
            }
        }

        projectAssess.setAssessState(1);
        return R.ok(super.updateExtension4Log(projectAssess, projectAssess.getProjectId())).toAjaxResult();
    }

    @Override
    public AjaxResult deleteById(Long id) {
        ProjectAssess projectAssess = this.getById(id);
        LambdaQueryWrapper<ProjectIntervene> queryWrapper = new LambdaQueryWrapper<ProjectIntervene>();
        queryWrapper.eq(ProjectIntervene::getAssessId, id);
        List<ProjectIntervene> projectInterList = projectInterveneMapper.selectList(queryWrapper);
        if (projectInterList.size() > 0) {
            return R.fail("该评估已被项目干预:" + projectInterList.get(0).getInterveneSchemeName() + "关联引用").toAjaxResult();
        }
        LambdaQueryWrapper<ProjectAssess> Wrapper = new LambdaQueryWrapper<ProjectAssess>();
        Wrapper.eq(ProjectAssess::getProjectId, projectAssess.getProjectId());
        Wrapper.eq(ProjectAssess::getAssessState, 1);
        Wrapper.eq(ProjectAssess::getDeleted, Deleted.N);
        List<ProjectAssess> projectAssessList = this.list(Wrapper);
        if (ObjectUtils.isNotEmpty(projectAssessList) && projectAssessList.size() == 1 && projectAssessList.get(0).getId().equals(id)){
            return R.fail("最后一条已完成评估不可删除").toAjaxResult();
        }



        super.deleteExtension(projectAssess, null, projectAssess.getProjectId());
        return R.ok().toAjaxResult();
    }

    private ProjectAssessVo getProAssessVo(ProjectAssess projectAssess, ProjectAssessVo getProAssessVo) {
        getProAssessVo.setAssessDeadline(projectAssess.getAssessDeadline());
        getProAssessVo.setAssessTime(projectAssess.getAssessTime());
        getProAssessVo.setAssessTitle(projectAssess.getAssessTitle());
        getProAssessVo.setAssessState(ObjectUtils.isNotEmpty(projectAssess.getId()) ? ShiFou.SHI.getCode() : ShiFou.FOU.getCode());
        return getProAssessVo;
    }

    @DataFill
    @Override
    public IPage<Project> projectPage(QueryPage queryPage) {
        Map params = queryPage.getParams();
        Object assessState = params.get("assessState");
        if (ObjectUtils.isNotEmpty(assessState)) {
            LambdaQueryWrapper<ProjectAssess> queryWrapper = new LambdaQueryWrapper<ProjectAssess>();
            queryWrapper.eq(ProjectAssess::getDeleted, Deleted.N);
            List<ProjectAssess> list = this.list(queryWrapper);
            if (ObjectUtils.isNotEmpty(assessState)) {
                queryPage.getParams().remove("assessState");
                if (assessState.equals(ShiFou.FOU.getCode())) {
                    queryPage.getNotInParams().put("project.id", list.stream().map(v -> v.getProjectId()).collect(Collectors.toList()));
                } else {
                    queryPage.getInParams().put("project.id", list.stream().map(v -> v.getProjectId()).collect(Collectors.toList()));
                }
            }
        }
        String serviceContent = "serviceContent";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceContent))) {
            queryPage.getDbParams().put("serviceContent", queryPage.getInParams().get(serviceContent));
            queryPage.getInParams().remove(serviceContent);
        }
        String assessDeadline = "assess.assessDeadline";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(assessDeadline))) {
            queryPage.getDbParams().put("assessDeadline", queryPage.getParams().get(assessDeadline));
            queryPage.getParams().remove(assessDeadline);
        }

        return projectAssessMapper.list(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());
    }

    private List<String> fileIds(String fileId) {
        List<String> list = new ArrayList<>();
        String[] split = fileId.split(",");
        for (String str : split) {
            list.add(str);
        }
        return list;
    }
}