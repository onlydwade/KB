package com.bytefinger.toutuo.biz.projectextension.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.enums.ShiFou;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.DictUtils;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.system.core.domain.SysDictData;
import com.bytefinger.toutuo.api.system.core.domain.SysDictType;
import com.bytefinger.toutuo.biz.interfacelog.service.IInterfaceLogService;
import com.bytefinger.toutuo.biz.oa.domain.OaApproval;
import com.bytefinger.toutuo.biz.oa.mapper.OaApprovalMapper;
import com.bytefinger.toutuo.biz.performance.domain.vo.ProjectListStepVo;
import com.bytefinger.toutuo.biz.project.bo.ProjectAchievementBO;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectOperationHistory;
import com.bytefinger.toutuo.biz.project.domain.ProjectTeam;
import com.bytefinger.toutuo.biz.project.enums.ProjectStatus;
import com.bytefinger.toutuo.biz.project.mapper.ProjectMapper;
import com.bytefinger.toutuo.biz.project.mapper.ProjectOperationHistoryMapper;
import com.bytefinger.toutuo.biz.project.mapper.ProjectTeamMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.project.service.IProjectTeamService;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocument;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentService;
import com.bytefinger.toutuo.biz.projectextension.entity.*;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectExtensionStatus;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectProcessStatus;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectExtensionMapper;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectStepExpansionMenuMapper;
import com.bytefinger.toutuo.biz.projectextension.service.*;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryTotalVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectTotalDetailVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectTotalVo;
import com.bytefinger.toutuo.biz.projectstep.constants.ProjectStepConstant;
import com.bytefinger.toutuo.biz.user.service.UserService;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProjectExtensionServiceImpl extends BizService<ProjectExtensionMapper, ProjectExtension> implements IProjectExtensionService {

    private final ProjectExtensionMapper projectExtensionMapper;

    private final ProjectMapper projectMapper;

    private final ProjectStepExpansionMenuMapper projectStepMenuExpansionMapper;

    private final IProjectService projectService;

    private final IProjectCheckService projectCheckService;

    private final IProjectAssessService projectAssessService;

    private final IProjectExtensionExitService projectExtensionExitService;

    private final UserService userService;

    private final IInterfaceLogService iInterfaceLogService;
    private final ProjectTeamMapper projectTeamMapper;

    private final IAuditDocumentService projectDocumentService;

    private final IProjectTeamService projectTeamService;

    private final ProjectOperationHistoryMapper projectOperationHistoryMapper;

    private final IProjectStepExpansionService projectStepExpansionService;

    private final OaApprovalMapper oaApprovalMapper;

    @Override
    public List<ProjectQueryVo> page(List<Project> projects) {
        //项管基础信息
        List<ProjectQueryVo> projectQueryVos = new ArrayList<>();
        return this.projectQueryVos(projects);
    }

    public List<ProjectQueryVo> projectQueryVos(List<Project> projects) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<ProjectQueryVo> projectQueryVos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(projects)) {
            List<Long> projectIds = projects.stream().map(pro -> pro.getId()).collect(Collectors.toList());
            //查验
            LambdaQueryWrapper<ProjectCheck> wrapperCheck = new LambdaQueryWrapper<ProjectCheck>();
            wrapperCheck.in(ProjectCheck::getProjectId, projectIds);
            wrapperCheck.notIn(ProjectCheck::getTemporary, 0);
            List<ProjectCheck> projectChecks = projectCheckService.list(wrapperCheck);

            //评估
            LambdaQueryWrapper<ProjectAssess> wrapperAssess = new LambdaQueryWrapper<ProjectAssess>();
            wrapperAssess.in(ProjectAssess::getProjectId, projectIds);
            wrapperAssess.eq(ProjectAssess::getDeleted, 0);
            List<ProjectAssess> projectAssesss = projectAssessService.list(wrapperAssess);

            //退场项目
            LambdaQueryWrapper<ProjectExtensionExit> wrapperExtensionExit = new LambdaQueryWrapper<ProjectExtensionExit>();
            wrapperExtensionExit.in(ProjectExtensionExit::getProjectId, projectIds);
            List<ProjectExtensionExit> projectExtensionExits = projectExtensionExitService.list(wrapperExtensionExit);

            //审批状态/退场
//            LambdaQueryWrapper<ProjectExtension> wrapperExtension = new LambdaQueryWrapper<ProjectExtension>();
//            wrapperExtension.in(ProjectExtension::getProjectId, projectIds);
//            wrapperExtension.eq(ProjectExtension::getProcessMode,3);
//            List<ProjectExtension> projectExtensions = this.list(wrapperExtension);
//
//            //审批状态/续签，重新投标
//            LambdaQueryWrapper<ProjectExtension> wrapperXcExtension = new LambdaQueryWrapper<ProjectExtension>();
//            wrapperXcExtension.in(ProjectExtension::getRelevanceProjectId, projectIds);
//            List<Integer> modeList = new ArrayList<>();
//            modeList.add(1);
//            modeList.add(2);
//            wrapperXcExtension.in(ProjectExtension::getProcessMode,modeList);
//            List<ProjectExtension> projectXcExtensions = this.list(wrapperXcExtension);

            for (Project project : projects) {
                ProjectQueryVo projectQueryVo = new ProjectQueryVo();
                BeanUtils.copyProperties(project, projectQueryVo);
                //当前登入人是否是项目归属人、拓后负责人、项目团队成员、创建人
                //项目团队成员
                List<ProjectTeam> projectTeamList = projectTeamMapper.selectList(Wrappers.<ProjectTeam>lambdaQuery().eq(ProjectTeam::getRecordId, project.getId()).eq(ProjectTeam::getUserId, SecurityUtils.getUserId()));
                if ((ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(projectTeamList) && projectTeamList.size() > 0)) {

                    projectQueryVo.setShow(true);
                } else {
                    projectQueryVo.setShow(false);
                }
                //当前登入人是否是拓后负责人
                if (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) {
                    projectQueryVo.setPrincipalShow(true);
                } else {
                    projectQueryVo.setPrincipalShow(false);
                }
                //当前登入人是否创建人和项目归属人
                if (ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId()) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId()))) {
                    projectQueryVo.setAttributorOrCreateShow(true);
                } else {
                    projectQueryVo.setAttributorOrCreateShow(false);
                }
                //是否查验
                projectQueryVo.setCheckState(ObjectUtils.isNotEmpty(projectChecks) ? (projectChecks.stream().filter(check -> check.getProjectId().equals(project.getId())).count() > 0 ? ShiFou.SHI.getCode() : ShiFou.FOU.getCode()) : ShiFou.FOU.getCode());
                //是否评估
                projectQueryVo.setAssessIsNot(ObjectUtils.isNotEmpty(projectAssesss) ? (projectAssesss.stream().filter(check -> check.getProjectId().equals(project.getId())).count() > 0 ? ShiFou.SHI.getCode() : ShiFou.FOU.getCode()) : ShiFou.FOU.getCode());
                //合同到期时间不为空，判断合同是否到期描述 and 即将到期天数
                if (ObjectUtils.isNotEmpty(project.getServiceEndTime())) {
                    dateAddTime(projectQueryVo);
                }
                if (ObjectUtils.isNotEmpty(project.getServiceBeginTime())) {
                    //合同服务开始时间+1年15天为 经营评估期限
                    projectQueryVo.setManageAssessDeadline(DateUtils.addAndSubtractDate(project.getServiceBeginTime(), 1, null, 15));
                }

                if (projectQueryVo.getProcessState() == null) {
                    projectQueryVo.setProcessState(0);
                }
                //                ProjectExtension projectExtension = projectExtensions.stream().filter(exten -> exten.getProjectId().equals(project.getId())).findFirst().orElse(new ProjectExtension());
//                if (ObjectUtils.isNotEmpty(projectExtension.getId())) {
//                    //处理状态 or 处理方式
//                    projectQueryVo.setProcessMode(projectExtension.getProcessMode());
//                    projectQueryVo.setProcessState(projectExtension.getProcessState());
//                    projectQueryVo.setDisposeTime(projectExtension.getUpdateTime());
//                    if (ObjectUtils.isNotEmpty(projectExtension.getCreateUserId())) {
//                        UserVO userVO = userService.getUser(projectExtension.getCreateUserId());
//                        projectQueryVo.setProcessPerson(ObjectUtils.isNotEmpty(userVO) ? userVO.getRealname() : null);
//                    }
//                    if (ObjectUtils.isNotEmpty(projectExtension.getRelevanceProjectId())) {
//                        //关联项目id or项目编号
//                        projectQueryVo.setRelevanceProjectId(projectExtension.getRelevanceProjectId());
//                        Project projectRelevance = projectService.getById(projectExtension.getRelevanceProjectId());
//                        if (ObjectUtils.isNotEmpty(projectRelevance.getId())) {
//                            projectQueryVo.setRelevanceProjectNo(projectRelevance.getProjectNo());
//                        }
//
//                    }
//                } else {
//                    projectQueryVo.setProcessState(0);
//                }
//
//
//                ProjectExtension projectXcExtension = projectXcExtensions.stream().filter(exten -> exten.getRelevanceProjectId().equals(project.getId())).findFirst().orElse(new ProjectExtension());
//                if (ObjectUtils.isNotEmpty(projectXcExtension.getId())) {
//                    //处理状态 or 处理方式
//                    projectQueryVo.setProcessMode(projectXcExtension.getProcessMode());
//                    projectQueryVo.setProcessState(projectXcExtension.getProcessState());
//                    projectQueryVo.setDisposeTime(projectXcExtension.getUpdateTime());
//                    if (ObjectUtils.isNotEmpty(projectXcExtension.getCreateUserId())) {
//                        UserVO userVO = userService.getUser(projectXcExtension.getCreateUserId());
//                        projectQueryVo.setProcessPerson(ObjectUtils.isNotEmpty(userVO) ? userVO.getRealname() : null);
//                    }
//                }


                ProjectExtensionExit projectExtensionExit = projectExtensionExits.stream().filter(exten -> exten.getProjectId().equals(project.getId())).findFirst().orElse(new ProjectExtensionExit());
                if (ObjectUtils.isNotEmpty(projectExtensionExit.getId())) {
                    //审批流程 or 退场描述
                    projectQueryVo.setApprovalProcessOA(projectExtensionExit.getApprovalUrl());
                    projectQueryVo.setExitDescription(projectExtensionExit.getContent());
                    projectQueryVo.setApprovalSponsorTime(sdf.format(projectExtensionExit.getApprovalSponsorTime()));
                }
                if (ObjectUtils.isEmpty(project.getRenewal())) {
                    projectQueryVo.setRenewal("0");
                }
                projectQueryVos.add(projectQueryVo);

            }
        }

        return projectQueryVos;
    }

    public List<ProjectQueryVo> details(Project projectA) {

        List<Project> projects = new ArrayList<>();
        projects.add(projectA);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<ProjectQueryVo> projectQueryVos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(projects)) {
            List<Long> projectIds = projects.stream().map(pro -> pro.getId()).collect(Collectors.toList());
            //查验
            LambdaQueryWrapper<ProjectCheck> wrapperCheck = new LambdaQueryWrapper<ProjectCheck>();
            wrapperCheck.in(ProjectCheck::getProjectId, projectIds);
            wrapperCheck.notIn(ProjectCheck::getTemporary, 0);
            List<ProjectCheck> projectChecks = projectCheckService.list(wrapperCheck);

            //评估
            LambdaQueryWrapper<ProjectAssess> wrapperAssess = new LambdaQueryWrapper<ProjectAssess>();
            wrapperAssess.in(ProjectAssess::getProjectId, projectIds);
            wrapperAssess.eq(ProjectAssess::getDeleted, 0);
            List<ProjectAssess> projectAssesss = projectAssessService.list(wrapperAssess);

            //退场项目
            LambdaQueryWrapper<ProjectExtensionExit> wrapperExtensionExit = new LambdaQueryWrapper<ProjectExtensionExit>();
            wrapperExtensionExit.in(ProjectExtensionExit::getProjectId, projectIds);
            List<ProjectExtensionExit> projectExtensionExits = projectExtensionExitService.list(wrapperExtensionExit);

            //审批状态/退场
            LambdaQueryWrapper<ProjectExtension> wrapperExtension = new LambdaQueryWrapper<ProjectExtension>();
            wrapperExtension.in(ProjectExtension::getProjectId, projectIds);
            wrapperExtension.eq(ProjectExtension::getProcessMode, 3);
            List<ProjectExtension> projectExtensions = this.list(wrapperExtension);

            //审批状态/续签，重新投标
            LambdaQueryWrapper<ProjectExtension> wrapperXcExtension = new LambdaQueryWrapper<ProjectExtension>();
            wrapperXcExtension.in(ProjectExtension::getProjectId, projectIds);
            List<Integer> modeList = new ArrayList<>();
            modeList.add(1);
            modeList.add(2);
            wrapperXcExtension.in(ProjectExtension::getProcessMode, modeList);
            List<ProjectExtension> projectXcExtensions = this.list(wrapperXcExtension);

            for (Project project : projects) {
                ProjectQueryVo projectQueryVo = new ProjectQueryVo();
                BeanUtils.copyProperties(project, projectQueryVo);
                //当前登入人是否是项目归属人、拓后负责人、项目团队成员、创建人
                //项目团队成员
                List<ProjectTeam> projectTeamList = projectTeamMapper.selectList(Wrappers.<ProjectTeam>lambdaQuery().eq(ProjectTeam::getRecordId, project.getId()).eq(ProjectTeam::getUserId, SecurityUtils.getUserId()));
                if ((ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(projectTeamList) && projectTeamList.size() > 0)) {

                    projectQueryVo.setShow(true);
                } else {
                    projectQueryVo.setShow(false);
                }
                //当前登入人是否是拓后负责人
                if (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) {
                    projectQueryVo.setPrincipalShow(true);
                } else {
                    projectQueryVo.setPrincipalShow(false);
                }
                //当前登入人是否创建人和项目归属人
                if (ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId()) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId()))) {
                    projectQueryVo.setAttributorOrCreateShow(true);
                } else {
                    projectQueryVo.setAttributorOrCreateShow(false);
                }
                //是否查验
                projectQueryVo.setCheckState(ObjectUtils.isNotEmpty(projectChecks) ? (projectChecks.stream().filter(check -> check.getProjectId().equals(project.getId())).count() > 0 ? ShiFou.SHI.getCode() : ShiFou.FOU.getCode()) : ShiFou.FOU.getCode());
                //是否评估
                projectQueryVo.setAssessIsNot(ObjectUtils.isNotEmpty(projectAssesss) ? (projectAssesss.stream().filter(check -> check.getProjectId().equals(project.getId())).count() > 0 ? ShiFou.SHI.getCode() : ShiFou.FOU.getCode()) : ShiFou.FOU.getCode());
                //合同到期时间不为空，判断合同是否到期描述 and 即将到期天数
                if (ObjectUtils.isNotEmpty(project.getServiceEndTime())) {
                    dateAddTime(projectQueryVo);
                }
                if (ObjectUtils.isNotEmpty(project.getServiceBeginTime())) {
                    //合同服务开始时间+1年15天为 经营评估期限
                    projectQueryVo.setManageAssessDeadline(DateUtils.addAndSubtractDate(project.getServiceBeginTime(), 1, null, 15));
                }

                if (projectQueryVo.getProcessState() == null) {
                    projectQueryVo.setProcessState(0);
                }
                ProjectExtension projectExtension = projectExtensions.stream().filter(exten -> exten.getProjectId().equals(project.getId())).findFirst().orElse(new ProjectExtension());
                if (ObjectUtils.isNotEmpty(projectExtension.getId())) {
                    //处理状态 or 处理方式
                    projectQueryVo.setProcessMode(projectExtension.getProcessMode());
                    projectQueryVo.setProcessState(projectExtension.getProcessState());
                    projectQueryVo.setDisposeTime(projectExtension.getUpdateTime());
                    if (ObjectUtils.isNotEmpty(projectExtension.getCreateUserId())) {
                        UserVO userVO = userService.getUser(projectExtension.getCreateUserId());
                        projectQueryVo.setProcessPerson(ObjectUtils.isNotEmpty(userVO) ? userVO.getRealname() : null);
                    }
                    if (ObjectUtils.isNotEmpty(projectExtension.getRelevanceProjectId())) {
                        //关联项目id or项目编号
                        projectQueryVo.setRelevanceProjectId(projectExtension.getRelevanceProjectId());
                        Project projectRelevance = projectService.getById(projectExtension.getRelevanceProjectId());
                        if (ObjectUtils.isNotEmpty(projectRelevance.getId())) {
                            projectQueryVo.setRelevanceProjectNo(projectRelevance.getProjectNo());
                        }

                    }
                } else {
                    projectQueryVo.setProcessState(0);
                }


                ProjectExtension projectXcExtension = projectXcExtensions.stream().filter(exten -> exten.getProjectId().equals(project.getId())).findFirst().orElse(new ProjectExtension());
                if (ObjectUtils.isNotEmpty(projectXcExtension.getId())) {
                    //处理状态 or 处理方式
                    projectQueryVo.setProcessMode(projectXcExtension.getProcessMode());
                    projectQueryVo.setProcessState(projectXcExtension.getProcessState());
                    projectQueryVo.setDisposeTime(projectXcExtension.getUpdateTime());
                    if (ObjectUtils.isNotEmpty(projectXcExtension.getCreateUserId())) {
                        UserVO userVO = userService.getUser(projectXcExtension.getCreateUserId());
                        projectQueryVo.setProcessPerson(ObjectUtils.isNotEmpty(userVO) ? userVO.getRealname() : null);
                    }
                }


                ProjectExtensionExit projectExtensionExit = projectExtensionExits.stream().filter(exten -> exten.getProjectId().equals(project.getId())).findFirst().orElse(new ProjectExtensionExit());
                if (ObjectUtils.isNotEmpty(projectExtensionExit.getId())) {
                    //审批流程 or 退场描述
                    projectQueryVo.setApprovalProcessOA(projectExtensionExit.getApprovalUrl());
                    projectQueryVo.setExitDescription(projectExtensionExit.getContent());
                    projectQueryVo.setApprovalSponsorTime(sdf.format(projectExtensionExit.getApprovalSponsorTime()));
                }
                if (ObjectUtils.isEmpty(project.getRenewal())) {
                    projectQueryVo.setRenewal("0");
                }
                projectQueryVos.add(projectQueryVo);

            }
        }

        return projectQueryVos;
    }

    public List<Project> projectExpandQueryVos(List<Project> projects) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //List<ProjectQueryVo> projectQueryVos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(projects)) {
            List<Long> projectIds = projects.stream().map(pro -> pro.getId()).collect(Collectors.toList());

            //退场项目
            LambdaQueryWrapper<ProjectTeam> wrapperExtensionExit = new LambdaQueryWrapper<ProjectTeam>();
            wrapperExtensionExit.in(ProjectTeam::getRecordId, projectIds);
            wrapperExtensionExit.eq(ProjectTeam::getUserId, SecurityUtils.getUserId());
            List<ProjectTeam> projectTeams = projectTeamMapper.selectList(wrapperExtensionExit);

            for (Project project : projects) {
                //ProjectQueryVo projectQueryVo = new ProjectQueryVo();
                //BeanUtils.copyProperties(project, projectQueryVo);
                //当前登入人是否是项目归属人、拓后负责人、项目团队成员、创建人
                //项目团队成员
                List<ProjectTeam> projectTeamList = projectTeams.stream().filter(o -> o.getRecordId().equals(project.getId())).collect(Collectors.toList());
                //projectTeamMapper.selectList(Wrappers.<ProjectTeam>lambdaQuery().eq(ProjectTeam::getRecordId, project.getId()).eq(ProjectTeam::getUserId, SecurityUtils.getUserId()));
                if ((ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(projectTeamList) && projectTeamList.size() > 0)) {
                    project.setShow(true);
                } else {
                    project.setShow(false);
                }
                //当前登入人是否是拓后负责人
                if (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) {
                    project.setPrincipalShow(true);
                } else {
                    project.setPrincipalShow(false);
                }
                //当前登入人是否创建人和项目归属人
                if (ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId()) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId()))) {
                    project.setAttributorOrCreateShow(true);
                } else {
                    project.setAttributorOrCreateShow(false);
                }
            }
        }

        projects = projectService.getYtProjetList(projects);
        return projects;
    }

    /**
     * 续签和重新投标列表-加上归属人和拓后负责人
     * 拼装用户信息
     *
     * @param projects
     * @return
     */
    @Override
    public List<ProjectQueryVo> renewBidPage(List<Project> projects) {
        List<ProjectQueryVo> projectQueryVos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(projects)) {
            for (Project project : projects) {
                ProjectQueryVo projectQueryVo = new ProjectQueryVo();
                BeanUtils.copyProperties(project, projectQueryVo);
                //当前登入人是否是项目归属人、拓后负责人、项目团队成员、创建人
                //项目团队成员
                List<ProjectTeam> projectTeamList = projectTeamMapper.selectList(Wrappers.<ProjectTeam>lambdaQuery().eq(ProjectTeam::getRecordId, project.getId()).eq(ProjectTeam::getUserId, SecurityUtils.getUserId()));
                if ((ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(projectTeamList) && projectTeamList.size() > 0)) {

                    projectQueryVo.setShow(true);
                } else {
                    projectQueryVo.setShow(false);
                }
                //当前登入人是否是拓后负责人
                if (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) {
                    projectQueryVo.setPrincipalShow(true);
                } else {
                    projectQueryVo.setPrincipalShow(false);
                }
                //当前登入人是否创建人和项目归属人
                if (ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId()) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId()))) {
                    projectQueryVo.setAttributorOrCreateShow(true);
                } else {
                    projectQueryVo.setAttributorOrCreateShow(false);
                }

                //转化金额，和新拓管理计算一样
                if (null != project.getContractAmount() && null != project.getProposedServicePeriod() && !project.getProposedServicePeriod().equals(0)) {
                    projectQueryVo.setAnnualConversionAmount(
                            projectService.calculateAnnualConversion(
                                    project.getContractAmount(),
                                    project.getProposedServicePeriod(),
                                    project.getServiceBeginTime(),
                                    project.getServiceEndTime(),
                                    project.getPerformanceConfirmTime()
                            )
                    );
                }

                projectQueryVos.add(projectQueryVo);

            }
        }
        return projectQueryVos;
    }

    /**
     * 是否到期
     *
     * @param projectQueryVo
     * @return
     */
    private void dateAddTime(ProjectQueryVo projectQueryVo) {
        try {
            boolean b = projectQueryVo.getServiceEndTime().before(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date smdate = sdf.parse(sdf.format(new Date()));
            Date bdate = sdf.parse(sdf.format(projectQueryVo.getServiceEndTime()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            if (!b) {
                long between_days = (time2 - time1) / (1000 * 3600 * 24);
                if (between_days <= 90) {
                    projectQueryVo.setContractIsNotExpire("即将到期");
                    //即将到期
                    projectQueryVo.setContractExpireTime(String.valueOf(between_days));
                } else {
                    projectQueryVo.setContractIsNotExpire("未到期");
                }
            } else {
                long between_days = (time1 - time2) / (1000 * 3600 * 24);
                projectQueryVo.setContractIsNotExpire("已到期");
                projectQueryVo.setContractExpireTime(String.valueOf(between_days));
            }
        } catch (Exception e) {

        }
    }

    @Override
    public List<ProjectStepExpansionMenu> findProjectEXtensionStepMenuBy() {
        List<ProjectStepExpansionMenu> list = projectStepMenuExpansionMapper.listbyProjectType();
        return list;
    }

    //    @Override
//    public List<ProjectStepMenu> findProjectStepMenuByProjectServiceStatus() {
//        List<ProjectStepMenu> list = projectStepMenuExpansionMapper.listbyProjectType();
//        return list;
//    }


    @Override
    @Transactional
    public R<Object> rollbackProject(Long id) {
        ProjectOperationHistory projectOperationHistory = projectOperationHistoryMapper.selectOne(Wrappers.<ProjectOperationHistory>lambdaUpdate()
                .eq(ProjectOperationHistory::getOldProjectId, id)
                .eq(ProjectOperationHistory::getDeleted, 0));

        if (projectOperationHistory == null) {
            return R.fail("操作历史记录不存在!");
        }

        //续签和重新投标处理逻辑
        if (
                "XU_QIAN".equals(projectOperationHistory.getOperationType())
                        || "CHONG_XIN_TOU_BIAO".equals(projectOperationHistory.getOperationType())
        ) {
            //删除续签和重新投标项目
            this.projectService.removeById(projectOperationHistory.getNewProjectId());

            //删除续签和重新投标项目  拓后管理续签状态关联记录
            ProjectExtension projectExtension = this.getOne(Wrappers.<ProjectExtension>lambdaUpdate()
                    .eq(ProjectExtension::getProjectId, projectOperationHistory.getNewProjectId()));
            if (projectExtension != null) {
                this.removeById(projectExtension.getId());
            }
        }
        //退场处理逻辑
        if (
                "YI_TUI_CHANG".equals(projectOperationHistory.getOperationType())
        ) {
            //删除退场节点
            ProjectStepExpansion projectExtension = projectStepExpansionService.getOne(Wrappers.<ProjectStepExpansion>lambdaUpdate()
                    .eq(ProjectStepExpansion::getProjectId, projectOperationHistory.getOldProjectId())
                    .eq(ProjectStepExpansion::getStepMenuId, "51")
            );
            if (projectExtension != null) {
                projectStepExpansionService.removeById(projectExtension.getId());
            }

            //删除退场记录
            ProjectExtensionExit projectExtensionExit = projectExtensionExitService.getOne(Wrappers.<ProjectExtensionExit>lambdaUpdate()
                    .eq(ProjectExtensionExit::getProjectId, projectOperationHistory.getOldProjectId())
            );
            if (projectExtensionExit != null) {
                projectExtensionExitService.removeById(projectExtensionExit.getId());
            }

            //删除oa记录
            List<OaApproval> oaApprovals = this.oaApprovalMapper.selectList(Wrappers.<OaApproval>lambdaUpdate()
                    .eq(OaApproval::getRecordId, projectOperationHistory.getOldProjectId())
                    .eq(OaApproval::getSubRecordId, "51"));
            oaApprovals.forEach(oaApproval -> {
                oaApprovalMapper.deleteById(oaApproval.getId());
            });

        }

        //修改原项目 拓后管理续签状态关联记录
        ProjectExtension oldProjectExtension = this.getOne(Wrappers.<ProjectExtension>lambdaUpdate()
                .eq(ProjectExtension::getProjectId, projectOperationHistory.getOldProjectId()));
        if (oldProjectExtension != null) {
            LambdaUpdateWrapper<ProjectExtension> lp = new LambdaUpdateWrapper<>();
            lp.set(ProjectExtension::getRelevanceProjectId, null);
            lp.set(ProjectExtension::getProcessState, 0);
            lp.set(ProjectExtension::getProcessMode, projectOperationHistory.getOldProcessMode());
            lp.eq(ProjectExtension::getId, oldProjectExtension.getId());
            this.update(oldProjectExtension, lp);
        }

        //回退原项目状态
        Project oldPorject = new Project();
        oldPorject.setId(projectOperationHistory.getOldProjectId());
        oldPorject.setServiceStatus(projectOperationHistory.getOldStatus());
        this.projectService.updateById(oldPorject);

        //操作记录失效
        projectOperationHistory.setDeleted(1);
        this.projectOperationHistoryMapper.updateById(projectOperationHistory);

        return R.ok("成功");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Project> saveRenewal(Project project) {

        ProjectOperationHistory operationHistory = new ProjectOperationHistory();

        String oldProjectId = project.getId().toString();
        Project oldProject = projectService.getById(oldProjectId);

        ProjectExtension oldExt = this.projectExtensionMapper.selectOne(Wrappers.<ProjectExtension>lambdaUpdate()
                .eq(ProjectExtension::getProjectId, oldProjectId)
        );

        operationHistory.setOldStatus(oldProject.getServiceStatus());
        operationHistory.setOperationType(project.getOperateType().equals(1) ? "XU_QIAN" : "CHONG_XIN_TOU_BIAO");

        Integer operateType = project.getOperateType();

        project = cloneBasicInformation1(project, oldProject);

        R<Project> projectAdd = projectService.add(project);
        project = projectAdd.getData();

        //新增续签关联关系
        if (ObjectUtils.isNotEmpty(projectAdd.getData())) {
            this.add(Long.parseLong(oldProjectId), 2, operateType, projectAdd.getData());
            //复制项目立项文件信息
            this.cloneProjectFile(Long.parseLong(oldProjectId), project.getId());
        }
        //设置归档
        updateProjectCloseFile(Long.parseLong(oldProjectId));

        //记录到操作历史
        operationHistory.setOldProjectId(oldProject.getId());
        operationHistory.setNewProjectId(project.getId());
        operationHistory.setOldStatus(oldProject.getServiceStatus());
        operationHistory.setOldProcessMode(oldExt == null ? "0" : oldExt.getProcessMode() == null ? "0" : oldExt.getProcessMode().toString());
        operationHistory.setOperationTime(new Date());
        operationHistory.setOperationMan(SecurityUtils.getUserId());
        operationHistory.setDeleted(0);
        projectOperationHistoryMapper.insert(operationHistory);

        return R.ok(project);
    }

    //续签
    @Override
    public void renew(Long id) {
        Project project = projectService.getById(id);
        String oldProjectType = project.getProjectType();
        //“是否为续签项目”类型改为“是”
        //R<Project> projectUpda = projectService.update(project);
        //续签为单一非投标项目
        if (project.getProjectType().equals("DAN_YI_TOU_BIAO_XIANG_MU")) {
            if ("ZHONG_SHI_YOU_TOU_BIAO".equals(project.getExpansionMode())) {
                project.setExpansionMode("ZHONG_SHI_YOU_CHENG_BIAO");
            } else {
                project.setExpansionMode("WAI_BU_CHENG_BIAO");
            }
        }
        project.setProjectType("DAN_YI_FEI_TOU_BIAO_XIANG_MU");
        project.setBided("FOU");
        //系统将自动生成一个单一非投类项目
        R<Project> projectAdd = projectService.add(this.cloneBasicInformation(project, "续签新增", oldProjectType));
        //新增续签关联关系
        if (ObjectUtils.isNotEmpty(projectAdd.getData())) {
            Project projectAddData = projectAdd.getData();
            this.add(project.getId(), 2, 1, projectAddData);
            //复制项目立项文件信息
            this.cloneProjectFile(project.getId(), projectAddData.getId());
        }

        updateProjectCloseFile(id);
    }

    //重新投标
    @Override
    public void newBid(Long id) {
        Project project = projectService.getById(id);
        String oldProjectType = project.getProjectType();
        //“是否为续签项目”类型改为“是”
        //重新投标为单一投标项目
        if (project.getProjectType().equals("DAN_YI_FEI_TOU_BIAO_XIANG_MU")) {
            if ("ZHONG_SHI_YOU_CHENG_BIAO".equals(project.getExpansionMode())) {
                project.setExpansionMode("ZHONG_SHI_YOU_TOU_BIAO");
            } else {
                project.setExpansionMode("WAI_BU_TOU_BIAO");
            }
        }
        project.setProjectType("DAN_YI_TOU_BIAO_XIANG_MU");
        project.setBided("SHI");
        //系统将自动生成一个单一非投类项目
        R<Project> projectAdd = projectService.add(this.cloneBasicInformation(project, "重新投标新增", oldProjectType));
        //新增续签关联关系
        if (ObjectUtils.isNotEmpty(projectAdd.getData())) {
            Project projectAddData = projectAdd.getData();
            this.add(project.getId(), 2, 2, projectAddData);
            //复制项目立项文件信息
            this.cloneProjectFile(project.getId(), projectAddData.getId());
        }

        updateProjectCloseFile(id);
    }

    public void updateProjectCloseFile(Long id) {
        Project projectOld = projectService.getById(id);
        projectOld.setServiceStatus("CLOSE_FILE");
        projectOld.setUpdateStatusDate(new Date());
        projectService.updateById(projectOld);
        //处理报销逻辑-归档
        //迭代二：审批通过后，发起报销申请流程
        if (ProjectStepConstant.DAN_YI_TOU_BIAO_XIANG_MU.equals(projectOld.getProjectType())) {
            iInterfaceLogService.sentReimbursementApplication(projectOld.getId().toString(), "YES");
        }
    }

    //复制基本信息
    private Project cloneBasicInformation(Project project, String sourceName, String oldProjectType) {
        Project projectCopy = new Project();
        projectCopy.setRelevanceProjectId(project.getId());
        projectCopy.setSourceName(project.getProjectName() + sourceName);
        if("续签新增".equals(sourceName)){
            projectCopy.setServiceStatus("TUO_ZHAN_ZHONG");
        }
        if("重新投标新增".equals(sourceName)){
            projectCopy.setServiceStatus("LI_XIANG_ZHONG");
        }
        projectCopy.setInStock(ShiFou.SHI.getCode());
        projectCopy.setExpire(ProjectStatus.YOU_XIAO.getCode());
        projectCopy.setAddress(project.getAddress());
        projectCopy.setApproachTime(project.getApproachTime());
        projectCopy.setAreaCode(project.getAreaCode());
        projectCopy.setAttributorUserId(project.getAttributorUserId());
        projectCopy.setBided(project.getBided());
        projectCopy.setBidingBudget(project.getBidingBudget());
        projectCopy.setBtApply(project.getBtApply());
        projectCopy.setCityCode(project.getCityCode());
        projectCopy.setCompanyId(project.getCompanyId());
        projectCopy.setKeywords(project.getKeywords());
        projectCopy.setCooperationMode(project.getCooperationMode());
        projectCopy.setCustomerId(project.getCustomerId());
        projectCopy.setExpansionMode(project.getExpansionMode());
        projectCopy.setProjectLevel(project.getProjectLevel());
        projectCopy.setProjectName(project.getProjectName());
        projectCopy.setProjectType(project.getProjectType());
        projectCopy.setProvinceCode(project.getProvinceCode());
        projectCopy.setRegionId(project.getRegionId());
        projectCopy.setRemark(project.getRemark());
        projectCopy.setStreetCode(project.getStreetCode());
        projectCopy.setBusinessType(project.getBusinessType());
        projectCopy.setConstructionArea(project.getConstructionArea());
        projectCopy.setServiceContent(project.getServiceContent());
        projectCopy.setServiceContentOther(project.getServiceContentOther());
        projectCopy.setCorporateIdentity(project.getCorporateIdentity());
        projectCopy.setBusinessSegments(project.getBusinessSegments());
        //单一投标项目 额外招标信息(新项目和旧项目都是单一投标)
        if ("DAN_YI_TOU_BIAO_XIANG_MU".equals(project.getProjectType()) && "DAN_YI_TOU_BIAO_XIANG_MU".equals(oldProjectType)) {
            projectCopy.setBidingAgency(project.getBidingAgency());
            projectCopy.setBidingNo(project.getBidingNo());
            projectCopy.setBidingMode(project.getBidingMode());
            projectCopy.setBidingPublishtime(project.getBidingPublishtime());
            projectCopy.setBidingCompany(project.getBidingCompany());
            projectCopy.setBidingCompanyContact(project.getBidingCompanyContact());
            projectCopy.setBidingCompanyPhone(project.getBidingCompanyPhone());
            projectCopy.setBidingAgencyContact(project.getBidingAgencyContact());
            projectCopy.setBidingAgencyPhone(project.getBidingAgencyPhone());
            projectCopy.setBidingEndtime(project.getBidingEndtime());
            projectCopy.setBidingOpentime(project.getBidingOpentime());
            projectCopy.setBidingWebsite(project.getBidingWebsite());
        }
//        else {
//            //股权合作项目
//            projectCopy.setCooperationType(project.getCooperationType());
//            projectCopy.setTargetBusinessType(project.getTargetBusinessType());
//            projectCopy.setTargetCompanyName(project.getTargetCompanyName());
//            projectCopy.setTargetCompanyNo(project.getTargetCompanyNo());
//            projectCopy.setTargetCompanyType(project.getTargetCompanyType());
//            projectCopy.setTargetIncorporationTime(project.getTargetIncorporationTime());
//            projectCopy.setTargetPersonnel(project.getTargetPersonnel());
//            projectCopy.setTargetLegalRepresentative(project.getTargetLegalRepresentative());
//            projectCopy.setTargetRegisteredCapital(project.getTargetRegisteredCapital());
//        }
        return projectCopy;
    }

    //复制基本信息
    private Project cloneBasicInformation1(Project project, Project oldProject) {
        Project projectCopy = new Project();
        String sourceName = "重新投标新增";
        if (project.getOperateType().equals(1)) {
            sourceName = "续签新增";
            projectCopy.setServiceStatus("TUO_ZHAN_ZHONG");
        }
        projectCopy.setServiceStatus("LI_XIANG_ZHONG");
        projectCopy.setSourceName(oldProject.getProjectName() + sourceName);
        projectCopy.setRelevanceProjectId(project.getId());
        projectCopy.setInStock(ShiFou.SHI.getCode());
        projectCopy.setExpire(ProjectStatus.YOU_XIAO.getCode());
        projectCopy.setAddress(project.getAddress());
        projectCopy.setApproachTime(project.getApproachTime());
        projectCopy.setAreaCode(project.getAreaCode());
        projectCopy.setAttributorUserId(project.getAttributorUserId());
        projectCopy.setBided(project.getBided());
        projectCopy.setBidingBudget(project.getBidingBudget());
        projectCopy.setBtApply(project.getBtApply());
        projectCopy.setCityCode(project.getCityCode());
        projectCopy.setCompanyId(project.getCompanyId());
        projectCopy.setKeywords(project.getKeywords());
        projectCopy.setCooperationMode(project.getCooperationMode());
        projectCopy.setCustomerId(project.getCustomerId());
        projectCopy.setExpansionMode(project.getExpansionMode());
        projectCopy.setProjectLevel(project.getProjectLevel());
        projectCopy.setProjectName(project.getProjectName());
        projectCopy.setProjectType(project.getProjectType());
        projectCopy.setProvinceCode(project.getProvinceCode());
        projectCopy.setRegionId(project.getRegionId());
        projectCopy.setRemark(project.getRemark());
        projectCopy.setStreetCode(project.getStreetCode());
        projectCopy.setBusinessType(project.getBusinessType());
        projectCopy.setConstructionArea(project.getConstructionArea());
        projectCopy.setServiceContent(project.getServiceContent());
        projectCopy.setServiceContentOther(project.getServiceContentOther());
        projectCopy.setCorporateIdentity(project.getCorporateIdentity());
        projectCopy.setBusinessSegments(project.getBusinessSegments());
        //单一投标项目 额外招标信息(新项目和旧项目都是单一投标)
        if ("DAN_YI_TOU_BIAO_XIANG_MU".equals(project.getProjectType()) && "DAN_YI_TOU_BIAO_XIANG_MU".equals(oldProject.getProjectType())) {
            projectCopy.setBidingAgency(project.getBidingAgency());
            projectCopy.setBidingNo(project.getBidingNo());
            projectCopy.setBidingMode(project.getBidingMode());
            projectCopy.setBidingPublishtime(project.getBidingPublishtime());
            projectCopy.setBidingCompany(project.getBidingCompany());
            projectCopy.setBidingCompanyContact(project.getBidingCompanyContact());
            projectCopy.setBidingCompanyPhone(project.getBidingCompanyPhone());
            projectCopy.setBidingAgencyContact(project.getBidingAgencyContact());
            projectCopy.setBidingAgencyPhone(project.getBidingAgencyPhone());
            projectCopy.setBidingEndtime(project.getBidingEndtime());
            projectCopy.setBidingOpentime(project.getBidingOpentime());
            projectCopy.setBidingWebsite(project.getBidingWebsite());
        }
        return projectCopy;
    }

    //    List<Long> projectIds = projects.stream().map(v -> v.getId()).collect(Collectors.toList());
//            if (ObjectUtils.isNotEmpty(projectIds)) {
//        //查验
//        LambdaQueryWrapper<ProjectCheck> checkWrapper = new LambdaQueryWrapper<ProjectCheck>();
//        checkWrapper.in(ProjectCheck::getProjectId, projectIds);
//        checkTotal = projectCheckService.count(checkWrapper);
//        //评估
//        LambdaQueryWrapper<ProjectAssess> assessWrapper = new LambdaQueryWrapper<ProjectAssess>();
//        assessWrapper.in(ProjectAssess::getProjectId, projectIds);
//        assessWrapper.groupBy(ProjectAssess::getProjectId);
//        assessTotal = projectAssessService.list(assessWrapper).size();
//        //退场 续签 重新投标
//        LambdaQueryWrapper<ProjectExtension> extensionWrapper = new LambdaQueryWrapper<ProjectExtension>();
//        extensionWrapper.in(ProjectExtension::getProjectId, projectIds);
//        List<ProjectExtension> projectExtensions = this.list(extensionWrapper);
    @Override
    @DataFill
    public ProjectQueryTotalVo total(QueryPage queryPage) {
        ProjectQueryTotalVo projectQueryTotalVo = new ProjectQueryTotalVo();
        Integer checkTotal = 0;
        Integer assessTotal = 0;
        Integer exitTotal = 0;
        Integer renewTotal = 0;
        Integer newBidTotal = 0;
        Integer extensionProjectTotal = 0;

        queryPage.setPageSize(Integer.MAX_VALUE);
        Page<Project> projects = projectExtensionMapper.extensionList(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());

        if (ObjectUtils.isNotEmpty(projects.getRecords())) {
            List<ProjectQueryVo> projectQueryVos = this.page(projects.getRecords());

            if (ObjectUtils.isNotEmpty(projectQueryVos)) {
                exitTotal = Math.toIntExact(projectQueryVos.stream().filter(pro -> ObjectUtils.isNotEmpty(pro.getProcessMode()) && ObjectUtils.isNotEmpty(pro.getProcessState()) && pro.getProcessMode().equals(ProjectExtensionStatus.TUI_CHANG.getCode()) && pro.getProcessState().equals(ProjectProcessStatus.YI_CHU_LI.getCode())).count());

                renewTotal = Math.toIntExact(projectQueryVos.stream().filter(pro -> ObjectUtils.isNotEmpty(pro.getProcessMode()) && ObjectUtils.isNotEmpty(pro.getProcessState()) && pro.getProcessMode().equals(ProjectExtensionStatus.XU_QIAN.getCode()) && pro.getProcessState().equals(ProjectProcessStatus.YI_CHU_LI.getCode())).count());

                newBidTotal = Math.toIntExact(projectQueryVos.stream().filter(pro -> ObjectUtils.isNotEmpty(pro.getProcessMode()) && ObjectUtils.isNotEmpty(pro.getProcessState()) && pro.getProcessMode().equals(ProjectExtensionStatus.CHONG_XIN_TOU_BIAO.getCode()) && pro.getProcessState().equals(ProjectProcessStatus.YI_CHU_LI.getCode())).count());

                checkTotal = Math.toIntExact(projectQueryVos.stream().filter(pro -> pro.getCheckState().equals(ShiFou.SHI.getCode())).count());

                assessTotal = Math.toIntExact(projectQueryVos.stream().filter(pro -> pro.getAssessIsNot().equals(ShiFou.SHI.getCode())).count());
            }
            extensionProjectTotal = Integer.valueOf(String.valueOf(projects.getRecords().size()));
            //退场
//            LambdaQueryWrapper<ProjectExtensionExit> exitWrapper = new LambdaQueryWrapper<ProjectExtensionExit>();
//            exitWrapper.in(ProjectExtensionExit::getProjectId, projectIds);
//            exitWrapper.groupBy(ProjectExtensionExit::getProjectId);
//            exitTotal = projectExtensionExitService.count(exitWrapper);

        }


        //拓后单一项目
        projectQueryTotalVo.setExtensionProjectTotal(extensionProjectTotal);
        //已承接查验
        projectQueryTotalVo.setCheckTotal(checkTotal);
        //已评估
        projectQueryTotalVo.setAssessTotal(assessTotal);
        //已退场
        projectQueryTotalVo.setExitTotal(exitTotal);
        //已续签
        projectQueryTotalVo.setRenewTotal(renewTotal);
        //重新投标
        projectQueryTotalVo.setNewBidTotal(newBidTotal);
        //续签或重新投标
        projectQueryTotalVo.setRenewOrNewBidTotal(renewTotal + newBidTotal);

        return projectQueryTotalVo;
    }

    @DataFill
    @Override
    public IPage<Project> projectPage(QueryPage queryPage) {

        String checkState = "checkState";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(checkState))) {
            queryPage.getDbParams().put("checkState", queryPage.getParams().get(checkState));
            queryPage.getParams().remove(checkState);
        }
        String assessIsNotkey = "assessIsNot";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(assessIsNotkey))) {
            queryPage.getDbParams().put("assessIsNot", queryPage.getParams().get(assessIsNotkey));
            queryPage.getParams().remove(assessIsNotkey);
        }

        String serviceStatus = "serviceStatus";
        if (!ObjectUtil.isEmpty(queryPage.getInParams().get(serviceStatus))) {
//            queryPage.getDbParams().put("serviceStatus", queryPage.getInParams().get(serviceStatus));
//            List<Object> serviceStatuList = new ArrayList<>();
//            serviceStatuList.add("YI_DAO_QI");
//            serviceStatuList.add("ZAI_GUAN");
//            queryPage.getInParams().put("serviceStatus",serviceStatuList);

            queryPage.getDbParams().put("serviceStatus", queryPage.getInParams().get(serviceStatus));
            queryPage.getInParams().remove(serviceStatus);
        }

        String processState = "processState";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(processState))) {
            queryPage.getDbParams().put("processState", queryPage.getInParams().get(processState));
            queryPage.getInParams().remove(processState);
        }
        String processMode = "extension.processMode";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(processMode))) {
            queryPage.getDbParams().put("processMode", queryPage.getInParams().get(processMode));
            queryPage.getInParams().remove(processMode);
        }

        String expandServiceStatus = "expandServiceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandServiceStatus))) {
            queryPage.getDbParams().put("expandServiceStatus", queryPage.getInParams().get(expandServiceStatus));
            queryPage.getInParams().remove(expandServiceStatus);
        }
        String expandTwoServiceStatus = "expandTwoServiceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandTwoServiceStatus))) {
            queryPage.getDbParams().put("expandTwoServiceStatus", queryPage.getInParams().get(expandTwoServiceStatus));
            queryPage.getInParams().remove(expandTwoServiceStatus);
        }

        String relevanceProjectNo = "relevanceProjectNo";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(relevanceProjectNo))) {
            queryPage.getDbParams().put("relevanceProjectNo", queryPage.getParams().get(relevanceProjectNo));
            queryPage.getParams().remove(relevanceProjectNo);
        }

        String totalType = "totalType";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(totalType))) {
            queryPage.getDbParams().put("totalType", queryPage.getParams().get(totalType));
            queryPage.getParams().remove(totalType);
        }

        String contractIsNotExpire = "contractIsNotExpire";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(contractIsNotExpire))) {
            queryPage.getDbParams().put("contractIsNotExpire", queryPage.getParams().get(contractIsNotExpire));
            queryPage.getParams().remove(contractIsNotExpire);
        }

        String serviceContent = "serviceContent";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceContent))) {
            queryPage.getDbParams().put("serviceContent", queryPage.getInParams().get(serviceContent));
            queryPage.getInParams().remove(serviceContent);
        }

        String updateStatusDate = "updateStatusDate";
        if (ObjectUtil.isNotEmpty(queryPage.getGeParams().get(updateStatusDate))) {
            queryPage.getDbParams().put("updateStatusDateStart", queryPage.getGeParams().get(updateStatusDate));
            queryPage.getGeParams().remove(updateStatusDate);
        }

        if (ObjectUtil.isNotEmpty(queryPage.getLeParams().get(updateStatusDate))) {
            queryPage.getDbParams().put("updateStatusDateEnd", queryPage.getLeParams().get(updateStatusDate));
            queryPage.getLeParams().remove(updateStatusDate);
        }

        //增加已过期，不在管项目过滤
        QueryWrapper<Project> wrapper = queryPage.getWrapper();

        IPage<Project> page = projectExtensionMapper.list(queryPage.toPage(), wrapper, queryPage.getDbParams());

        //统一和新拓管理一样
        if (ObjectUtils.isNotEmpty(page.getRecords()) && page.getRecords().size() > 0) {
            for (Project project : page.getRecords()) {
                if (null != project.getContractAmount() && null != project.getProposedServicePeriod() && !project.getProposedServicePeriod().equals(0)) {
                    project.setAnnualConversionAmount(
                            projectService.calculateAnnualConversion(
                                    project.getContractAmount(),
                                    project.getProposedServicePeriod(),
                                    project.getServiceBeginTime(),
                                    project.getServiceEndTime(),
                                    project.getPerformanceConfirmTime()
                            )
                    );
                }
            }
        }

        return page;
    }


    @DataFill
    @Override
    public IPage<Project> extensionProjectPage(QueryPage queryPage) {

        String checkState = "checkState";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(checkState))) {
            queryPage.getDbParams().put("checkState", queryPage.getParams().get(checkState));
            queryPage.getParams().remove(checkState);
        }
        String assessIsNotkey = "assessIsNot";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(assessIsNotkey))) {
            queryPage.getDbParams().put("assessIsNot", queryPage.getParams().get(assessIsNotkey));
            queryPage.getParams().remove(assessIsNotkey);
        }
        String processState = "extension.processState";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(processState))) {
            queryPage.getDbParams().put("processState", queryPage.getInParams().get(processState));
            queryPage.getInParams().remove(processState);
        }
        String processMode = "extension.processMode";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(processMode))) {
            queryPage.getDbParams().put("processMode", queryPage.getInParams().get(processMode));
            queryPage.getInParams().remove(processMode);
        }
        String relevanceProjectNo = "relevanceProjectNo";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(relevanceProjectNo))) {
            queryPage.getDbParams().put("relevanceProjectNo", queryPage.getParams().get(relevanceProjectNo));
            queryPage.getParams().remove(relevanceProjectNo);
        }
        String contractIsNotExpire = "contractIsNotExpire";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(contractIsNotExpire))) {
            queryPage.getDbParams().put("contractIsNotExpire", queryPage.getParams().get(contractIsNotExpire));
            queryPage.getParams().remove(contractIsNotExpire);
        }
        String serviceContent = "serviceContent";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceContent))) {
            queryPage.getDbParams().put("serviceContent", queryPage.getInParams().get(serviceContent));
            queryPage.getInParams().remove(serviceContent);
        }


        IPage<Project> page = projectExtensionMapper.extensionList(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());

        return page;
    }

    @DataFill
    @Override
    public IPage<Project> renewBidPage(QueryPage queryPage) {

        String serviceStatus = "serviceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceStatus))) {
            queryPage.getDbParams().put("serviceStatus", queryPage.getInParams().get(serviceStatus));
            queryPage.getInParams().remove(serviceStatus);
        }

        String processState = "processState";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(processState))) {
            queryPage.getDbParams().put("processState", queryPage.getInParams().get(processState));
            queryPage.getInParams().remove(processState);
        }

        String expandServiceStatus = "expandServiceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandServiceStatus))) {
            queryPage.getDbParams().put("expandServiceStatus", queryPage.getInParams().get(expandServiceStatus));
            queryPage.getInParams().remove(expandServiceStatus);
        }

        String expandProcessMode = "expandProcessMode";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandProcessMode))) {
            queryPage.getDbParams().put("expandProcessMode", queryPage.getInParams().get(expandProcessMode));
            queryPage.getInParams().remove(expandProcessMode);
        }

        String expandTwoServiceStatus = "expandTwoServiceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandTwoServiceStatus))) {
            queryPage.getDbParams().put("expandTwoServiceStatus", queryPage.getInParams().get(expandTwoServiceStatus));
            queryPage.getInParams().remove(expandTwoServiceStatus);
        }

        String expandTwoProcessMode = "expandTwoProcessMode";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandTwoProcessMode))) {
            queryPage.getDbParams().put("expandTwoProcessMode", queryPage.getInParams().get(expandTwoProcessMode));
            queryPage.getInParams().remove(expandTwoProcessMode);
        }

        List<String> expandCompanyIdList = Lists.newArrayList();
        String expandCompanyId = "expandCompanyId";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandCompanyId))) {
            expandCompanyIdList.addAll(queryPage.getInParams().get(expandCompanyId).stream().map(Object::toString).collect(Collectors.toList()));
            queryPage.getInParams().remove(expandCompanyId);
            queryPage.getDbParams().put("expandCompanyId", expandCompanyIdList);
        }

        String updateStatusDate = "updateStatusDate";
        if (ObjectUtil.isNotEmpty(queryPage.getGeParams().get(updateStatusDate))) {
            queryPage.getDbParams().put("updateStatusDateStart", queryPage.getGeParams().get(updateStatusDate));
            queryPage.getGeParams().remove(updateStatusDate);
        }

        if (ObjectUtil.isNotEmpty(queryPage.getLeParams().get(updateStatusDate))) {
            queryPage.getDbParams().put("updateStatusDateEnd", queryPage.getLeParams().get(updateStatusDate));
            queryPage.getLeParams().remove(updateStatusDate);
        }

        List<String> finishStepIdList = Lists.newArrayList();
        String finishStepId = "finishStepId";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(finishStepId))) {
            finishStepIdList.addAll(queryPage.getInParams().get(finishStepId).stream().map(Object::toString).collect(Collectors.toList()));
            queryPage.getInParams().remove(finishStepId);
            queryPage.getDbParams().put("finishStepId", finishStepIdList);
        }

        IPage<Project> projectIPage = projectExtensionMapper.renewBidList(queryPage.toPage(), queryPage.getWrapperByPrefix("project."), queryPage.getDbParams());
        if (ObjectUtils.isNotEmpty(projectIPage.getRecords()) && projectIPage.getRecords().size() > 0) {
            for (Project project : projectIPage.getRecords()) {

                List<ProjectListStepVo> projectListStepVos = projectMapper.getProjectListStep(projectIPage.getRecords().stream().map(Project::getId).collect(Collectors.toList()));
                Map<Long, List<ProjectListStepVo>> mapStepList = projectListStepVos.stream().collect(Collectors.groupingBy(ProjectListStepVo::getProjectId));

                List<ProjectListStepVo> itemSteps = mapStepList.get(project.getId());
                if (!CollectionUtils.isEmpty(itemSteps)) {
                    project.setStepName(itemSteps.get(0).getStepName());
                }
            }
        }

        return projectIPage;
    }

    @DataFill
    @Override
    public IPage<ProjectAchievementBO> pageTwo(QueryPage queryPage) {

        String serviceStatus = "serviceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceStatus))) {
            queryPage.getDbParams().put("serviceStatus", queryPage.getInParams().get(serviceStatus));
            queryPage.getInParams().remove(serviceStatus);
        }

        String processState = "processState";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(processState))) {
            queryPage.getDbParams().put("processState", queryPage.getInParams().get(processState));
            queryPage.getInParams().remove(processState);
        }

        String expandServiceStatus = "expandServiceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandServiceStatus))) {
            queryPage.getDbParams().put("expandServiceStatus", queryPage.getInParams().get(expandServiceStatus));
            queryPage.getInParams().remove(expandServiceStatus);
        }

        String expandProcessMode = "expandProcessMode";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandProcessMode))) {
            queryPage.getDbParams().put("expandProcessMode", queryPage.getInParams().get(expandProcessMode));
            queryPage.getInParams().remove(expandProcessMode);
        }

        String expandTwoServiceStatus = "expandTwoServiceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandTwoServiceStatus))) {
            queryPage.getDbParams().put("expandTwoServiceStatus", queryPage.getInParams().get(expandTwoServiceStatus));
            queryPage.getInParams().remove(expandTwoServiceStatus);
        }

        String expandTwoProcessMode = "expandTwoProcessMode";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandTwoProcessMode))) {
            queryPage.getDbParams().put("expandTwoProcessMode", queryPage.getInParams().get(expandTwoProcessMode));
            queryPage.getInParams().remove(expandTwoProcessMode);
        }

        List<String> expandCompanyIdList = Lists.newArrayList();
        String expandCompanyId = "expandCompanyId";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandCompanyId))) {
            expandCompanyIdList.addAll(queryPage.getInParams().get(expandCompanyId).stream().map(Object::toString).collect(Collectors.toList()));
            queryPage.getInParams().remove(expandCompanyId);
            queryPage.getDbParams().put("expandCompanyId", expandCompanyIdList);
        }

        String updateStatusDate = "updateStatusDate";
        if (ObjectUtil.isNotEmpty(queryPage.getGeParams().get(updateStatusDate))) {
            queryPage.getDbParams().put("updateStatusDateStart", queryPage.getGeParams().get(updateStatusDate));
            queryPage.getGeParams().remove(updateStatusDate);
        }

        if (ObjectUtil.isNotEmpty(queryPage.getLeParams().get(updateStatusDate))) {
            queryPage.getDbParams().put("updateStatusDateEnd", queryPage.getLeParams().get(updateStatusDate));
            queryPage.getLeParams().remove(updateStatusDate);
        }

        IPage<ProjectAchievementBO> projectIPage = projectExtensionMapper.listTwo(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());


        if (ObjectUtils.isNotEmpty(projectIPage.getRecords()) && projectIPage.getRecords().size() > 0) {
            for (ProjectAchievementBO project : projectIPage.getRecords()) {
                if (null != project.getContractAmount() && null != project.getProposedServicePeriod()) {
                    project.setAnnualConversionAmount(
                            this.projectService.calculateAnnualConversion(
                                    project.getContractAmount(),
                                    project.getProposedServicePeriod(),
                                    project.getServiceBeginTime(),
                                    project.getServiceEndTime(),
                                    project.getPerformanceConfirmTime()
                            )
                    );
                }
            }
        }

        projectIPage.setRecords(getYtProjetAchievementList(projectIPage.getRecords()));

        return projectIPage;
    }

    public List<ProjectAchievementBO> getYtProjetAchievementList(List<ProjectAchievementBO> projectList) {

        List<SysDictType> dictTypeList = DictUtils.getDictPtypeCache("XIANG_MU_YE_TAI");
        List<SysDictData> dictDataList = new ArrayList<>();

        dictTypeList.forEach(o -> {
            if (!"XIANG_MU_YE_TAI".equals(o.getDictType())) {
                List<SysDictData> itemList = DictUtils.getDictCache(o.getDictType());
                if (!CollectionUtils.isEmpty(itemList))
                    dictDataList.addAll(itemList);
            }
        });

        //缓存取出所有子业态数据
        Map<String, List<SysDictData>> mapDicData = dictDataList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));

        for (int i = 0; i < projectList.size(); i++) {

            if (StringUtils.isEmpty(projectList.get(i).getBusinessType())) {
                continue;
            }

            String businessType = projectList.get(i).getBusinessType();
            if (StringUtils.isEmpty(businessType))
                continue;

            List<SysDictData> itemList = mapDicData.get(businessType);
            if (CollectionUtils.isEmpty(itemList))
                continue;


            SysDictData sysDictData = itemList.get(0);
            String dictType = sysDictData.getDictType();
            List<SysDictType> itemDictTypeList = dictTypeList.stream().filter(m -> m.getDictType().equals(dictType)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(itemDictTypeList))
                continue;

            String dictStr = itemDictTypeList.get(0).getDictName() + "-" + sysDictData.getDictLabel();

            projectList.get(i).setBusinessTypeStr(dictStr);
        }
        return projectList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String syncOperationProject(Long id) {
        try {
            Project project = projectService.getById(id);
            project.setIsSyncOperation(ShiFou.SHI.getCode());
            projectService.updateById(project);
            return "";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }


    /**
     * 续签关联关系
     *
     * @param processState 处理状态(0未处理,1审批中,2已处理)
     * @param processMode  处理方式(0未续签,1续签,2重新投标,3退场)
     * @return projectExtensionEntity
     */
    public ProjectExtension add(Long projectId, Integer processState, Integer processMode, Project projectAddData) {
        ProjectExtension projectExtensionEntity = new ProjectExtension();

        ProjectExtension projectExtension = projectExtensionMapper.selectOne(Wrappers.<ProjectExtension>lambdaQuery().eq(ProjectExtension::getProjectId, projectId));
        if (projectExtension != null) {
            projectExtensionEntity = projectExtension;
        }

        projectExtensionEntity.setProjectId(projectId);
        projectExtensionEntity.setRelevanceProjectId(projectAddData.getId());
        projectExtensionEntity.setProcessState(processState);
        projectExtensionEntity.setProcessMode(processMode);
        if (projectExtensionEntity.getId() == null)
            this.save(projectExtensionEntity);
        else
            this.updateById(projectExtensionEntity);

        ProjectExtension projectAddExtensionEntity = new ProjectExtension();
        projectAddExtensionEntity.setProjectId(projectAddData.getId());
        projectAddExtensionEntity.setProcessState(0);
        projectAddExtensionEntity.setProcessMode(processMode);
        this.save(projectAddExtensionEntity);

        this.changeExtensionLog(ProjectExtension.class, projectExtensionEntity.getId(), processMode == 1 ? "续签" : "重新投标", projectId);
        return projectExtensionEntity;
    }

    /**
     * 复制项目立项文件信息与团队成员信息
     *
     * @param projectId        需要复制的文件id
     * @param projectAddDataId 复制后的文件id
     */
    public void cloneProjectFile(Long projectId, Long projectAddDataId) {
        //复制文件
        LambdaQueryWrapper<AuditDocument> wrapper = new LambdaQueryWrapper<AuditDocument>();
        wrapper.eq(AuditDocument::getStepMenuId, 13);
        wrapper.eq(AuditDocument::getProjectId, projectId);
        wrapper.eq(AuditDocument::getDeleted, 0);
        List<AuditDocument> projectDocuments = projectDocumentService.list(wrapper);
        for (AuditDocument projectDocument : projectDocuments) {
            projectDocument.setProjectId(projectAddDataId);
            projectDocumentService.save(projectDocument);
        }
        //复制团队成员
        List<ProjectTeam> projectTeams = projectTeamService.list(projectId);
        for (ProjectTeam projectTeam : projectTeams) {
            projectTeam.setRecordId(projectAddDataId);
            projectTeamService.save(projectTeam);
        }
    }


    /**
     * 查询在管项目统计
     * 拼装用户信息
     *
     * @param queryPage
     * @return
     */
    @Override
    @DataFill
    public ProjectTotalVo projectTotal(QueryPage queryPage) {
        ProjectTotalVo ProjectTotalVo = new ProjectTotalVo();
        Integer projectTotal = 0;
        Integer finishTotal = 0;
        Integer exitTotal = 0;
        Integer renewTotal = 0;
        Integer newBidTotal = 0;
        Integer expireTotal = 0;
        queryPage.setPageSize(Integer.MAX_VALUE);


        String checkState = "checkState";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(checkState))) {
            queryPage.getDbParams().put("checkState", queryPage.getParams().get(checkState));
            queryPage.getParams().remove(checkState);
        }
        String assessIsNotkey = "assessIsNot";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(assessIsNotkey))) {
            queryPage.getDbParams().put("assessIsNot", queryPage.getParams().get(assessIsNotkey));
            queryPage.getParams().remove(assessIsNotkey);
        }

        String serviceStatus = "serviceStatus";
        if (!ObjectUtil.isEmpty(queryPage.getInParams().get(serviceStatus))) {
//            queryPage.getDbParams().put("serviceStatus", queryPage.getInParams().get(serviceStatus));
//            List<Object> serviceStatuList = new ArrayList<>();
//            serviceStatuList.add("YI_DAO_QI");
//            serviceStatuList.add("ZAI_GUAN");
//            queryPage.getInParams().put("serviceStatus",serviceStatuList);

            queryPage.getDbParams().put("serviceStatus", queryPage.getInParams().get(serviceStatus));
            queryPage.getInParams().remove(serviceStatus);
        }

        String processState = "processState";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(processState))) {
            queryPage.getDbParams().put("processState", queryPage.getInParams().get(processState));
            queryPage.getInParams().remove(processState);
        }
        String processMode = "extension.processMode";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(processMode))) {
            queryPage.getDbParams().put("processMode", queryPage.getInParams().get(processMode));
            queryPage.getInParams().remove(processMode);
        }

        String expandServiceStatus = "expandServiceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandServiceStatus))) {
            queryPage.getDbParams().put("expandServiceStatus", queryPage.getInParams().get(expandServiceStatus));
            queryPage.getInParams().remove(expandServiceStatus);
        }
        String expandTwoServiceStatus = "expandTwoServiceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandTwoServiceStatus))) {
            queryPage.getDbParams().put("expandTwoServiceStatus", queryPage.getInParams().get(expandTwoServiceStatus));
            queryPage.getInParams().remove(expandTwoServiceStatus);
        }

        String relevanceProjectNo = "relevanceProjectNo";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(relevanceProjectNo))) {
            queryPage.getDbParams().put("relevanceProjectNo", queryPage.getParams().get(relevanceProjectNo));
            queryPage.getParams().remove(relevanceProjectNo);
        }

        String totalType = "totalType";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(totalType))) {
            queryPage.getDbParams().put("totalType", queryPage.getParams().get(totalType));
            queryPage.getParams().remove(totalType);
        }

        String contractIsNotExpire = "contractIsNotExpire";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(contractIsNotExpire))) {
            queryPage.getDbParams().put("contractIsNotExpire", queryPage.getParams().get(contractIsNotExpire));
            queryPage.getParams().remove(contractIsNotExpire);
        }

        String serviceContent = "serviceContent";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceContent))) {
            queryPage.getDbParams().put("serviceContent", queryPage.getInParams().get(serviceContent));
            queryPage.getInParams().remove(serviceContent);
        }

        String updateStatusDate = "updateStatusDate";
        if (ObjectUtil.isNotEmpty(queryPage.getGeParams().get(updateStatusDate))) {
            queryPage.getDbParams().put("updateStatusDateStart", queryPage.getGeParams().get(updateStatusDate));
            queryPage.getGeParams().remove(updateStatusDate);
        }

        if (ObjectUtil.isNotEmpty(queryPage.getLeParams().get(updateStatusDate))) {
            queryPage.getDbParams().put("updateStatusDateEnd", queryPage.getLeParams().get(updateStatusDate));
            queryPage.getLeParams().remove(updateStatusDate);
        }


        List<ProjectTotalDetailVo> projectTotalVos = projectExtensionMapper.projectTotal(queryPage.getWrapper(), queryPage.getDbParams());
        if (!CollectionUtils.isEmpty(projectTotalVos)) {
            projectTotal = projectTotalVos.size();
            //退场
            exitTotal = projectTotalVos.stream().filter(o -> o.getProcessMode() != null && o.getProcessState() != null && o.getProcessMode().equals(3) && o.getProcessState().equals(2)).collect(Collectors.toList()).size();
            //续签
            renewTotal = projectTotalVos.stream().filter(o -> o.getProcessModet() != null && o.getProcessModet().equals(1)).collect(Collectors.toList()).size();
            //重新投标数
            newBidTotal = projectTotalVos.stream().filter(o -> o.getProcessModet() != null && o.getProcessModet().equals(2)).collect(Collectors.toList()).size();
            //有效数量
            //expireTotal = projectTotalVo.getExpireTotal() == null ? 0 : projectTotalVo.getExpireTotal();
            //完结
            finishTotal = projectTotalVos.stream().filter(o -> "YI_WAN_JIE".equals(o.getServiceStatus())).collect(Collectors.toList()).size();
        }
        //项目数量
        ProjectTotalVo.setProjectTotal(projectTotal);
        //已完结
        ProjectTotalVo.setFinishTotal(finishTotal);
        //已退场
        ProjectTotalVo.setExitTotal(exitTotal);
        //已续签
        ProjectTotalVo.setRenewTotal(renewTotal);
        //重新投标
        ProjectTotalVo.setNewBidTotal(newBidTotal);
        //已到期
        ProjectTotalVo.setExpireTotal(expireTotal);
        //续签或重新投标
        //项目保留率=（重新投标项目数+续签项目数）/到期项目数
        double keepTotal = expireTotal == 0 ? 0 : (double) (renewTotal + newBidTotal) * 100 / expireTotal;
        String keepRate = String.format("%.2f%%", keepTotal);
        ProjectTotalVo.setKeepRate(keepRate);

        return ProjectTotalVo;
    }

}