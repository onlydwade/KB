package com.bytefinger.toutuo.biz.projectextension.oaapproval;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytefinger.common.core.utils.CamelUtils;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.project.enums.OAApprovalStatus;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.biz.companyexit.domain.ProjectCompanyExit;
import com.bytefinger.toutuo.biz.companyexit.service.IProjectCompanyExitService;
import com.bytefinger.toutuo.biz.companyhigh.domain.ProjectCompanyHighLevelMeetingApproval;
import com.bytefinger.toutuo.biz.companyhigh.service.IProjectCompanyHighLevelMeetingApprovalService;
import com.bytefinger.toutuo.biz.companyrisk.domain.ProjectCompanyRiskInspectionApproval;
import com.bytefinger.toutuo.biz.companyrisk.service.IProjectCompanyRiskInspectionApprovalService;
import com.bytefinger.toutuo.biz.interfacelog.service.IInterfaceLogService;
import com.bytefinger.toutuo.biz.oa.constants.OaConstant;
import com.bytefinger.toutuo.biz.oa.domain.OaApproval;
import com.bytefinger.toutuo.biz.operlog.service.IOperLogService;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectOperationHistory;
import com.bytefinger.toutuo.biz.project.mapper.ProjectOperationHistoryMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.enums.ProjectCompanyStatus;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCorrelation;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtension;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtensionExit;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectStepExpansion;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectExtensionExitStatus;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectExtensionStatus;
import com.bytefinger.toutuo.biz.projectextension.oaapproval.constant.ProjectExtensionOaConstant;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionExitService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectStepExpansionService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCorrelationVo;
import com.bytefinger.toutuo.biz.projectstep.constants.ProjectStepConstant;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStep;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepService;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import com.bytefinger.toutuo.common.constants.MessageTypeConstant;
import com.bytefinger.toutuo.common.domain.BizUserBaseEntity;
import com.bytefinger.toutuo.common.enums.ModuleType;
import com.bytefinger.toutuo.common.mapper.CommonMapper;
import com.bytefinger.toutuo.common.service.SendMessageService;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author ycj
 * @Date 2023/4/07
 **/
@Service
@AllArgsConstructor
public class ProjectExtensionOaApprovalService {

    private final IProjectExtensionExitService exitService;

    @Autowired
    private IProjectExtensionService extensionService;

    private final CommonMapper commonMapper;

    private final IProjectService projectService;

    private final ISysUserService userService;

    private final SendMessageService sendMessageService;

    private final IOperLogService operLogService;

    private final IProjectStepService projectStepService;
    private final IInterfaceLogService iInterfaceLogService;
    @Autowired
    private IProjectStepExpansionService projectStepExpansionService;

    private final ProjectOperationHistoryMapper projectOperationHistoryMapper;

    public void oaApproval(OaApproval oaApproval) {

        switch (oaApproval.getSubModuleName()) {
            case ProjectExtensionOaConstant.PROJECT_EXTENSION_EXIT:
                this.projectExit(oaApproval);
                break;
            default:
                break;
        }
    }


    private void recordLog(OaApproval oaApproval, Integer beforeValue) {
        operLogService.changeLog(ProjectCompany.class, oaApproval.getRecordId(),
                oaApproval.getSubject() + "审批状态变更，流程链接：" + oaApproval.getApprovalUrl(),
                null == beforeValue ? null : OAApprovalStatus.getByCode(beforeValue).getDesc(),
                OAApprovalStatus.getByCode(oaApproval.getApprovalStatus()).getDesc());

        //发送通知
        Set<Long> ids = Sets.newHashSet();

        Project project = projectService.getById(oaApproval.getRecordId());
        List<BizUserBaseEntity> bizUsers = commonMapper.listBizUser(
                CamelUtils.camelToUndeline(OaConstant.PROJECT),
                Arrays.asList(new Long[]{project.getId()}));
        if (!bizUsers.isEmpty()) {
            ids.addAll(bizUsers.stream().map(BizUserBaseEntity::getUserId).collect(Collectors.toList()));
        }
        if (ProjectExtensionOaConstant.PROJECT_EXTENSION_EXIT.equals(oaApproval.getSubModuleName())) {
            ids.add(project.getAttributorUserId());
            //拓后负责人
            if (ObjectUtils.isNotEmpty(project.getPrincipalId())) {
                ids.add(project.getPrincipalId());
            }

        }
        if (!CollectionUtils.isEmpty(ids)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("approvalStatus", OAApprovalStatus.getByCode(oaApproval.getApprovalStatus()).getDesc());
            jsonObject.put("approvalUrl", oaApproval.getApprovalUrl());
            SysUser user = userService.getById(oaApproval.getSubmitUserId());
            jsonObject.put("submitUserName", user.getRealname());
            jsonObject.put("submitTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, oaApproval.getCreateTime()));
            ids.forEach(v -> {
                sendMessageService.sendMessage(MessageTypeConstant.MESSAGE_APPROVAL, v, project.getId(),
                        oaApproval.getSubject(), jsonObject.toJSONString(), ModuleType.PROJECT_EXTEXSION.getCode(), "XI_TONG_TONG_ZHI" + "," + "DAI_BAN_TONG_ZHI");
            });
        }
    }


    private void projectExit(OaApproval oaApproval) {
        LambdaQueryWrapper<ProjectExtensionExit> queryWrapperOne = new LambdaQueryWrapper<ProjectExtensionExit>();
        queryWrapperOne.eq(ProjectExtensionExit::getProjectId, oaApproval.getRecordId());
        ProjectExtensionExit exit = exitService.getOne(queryWrapperOne);
        LambdaQueryWrapper<ProjectExtension> queryWrapper = new LambdaQueryWrapper<ProjectExtension>();
        queryWrapper.eq(ProjectExtension::getProjectId, exit.getProjectId());
        ProjectExtension projectExtension = extensionService.getOne(queryWrapper);
//        ProjectCompanyExit
        Integer beforeValue = exit.getApprovalStatus();

        recordLog(oaApproval, beforeValue);

        if (projectExtension.getProcessState().equals(ProjectExtensionExitStatus.YI_CHU_LI.getCode())) {

        } else {
            if (oaApproval.getApprovalStatus().equals(OAApprovalStatus.TONG_GUO.getCode())) {
                //通过后节点完成
                ProjectStepExpansion projectStepExpansion = new ProjectStepExpansion();
                projectStepExpansion.setProjectId(oaApproval.getRecordId());
                projectStepExpansion.setStepMenuId(oaApproval.getSubRecordId());
                projectStepExpansion.setStatus(1);
                projectStepExpansionService.update(projectStepExpansion);

                Project project = projectService.getById(exit.getProjectId());

                //记录到操作历史
                ProjectOperationHistory operationHistory = new ProjectOperationHistory();
                operationHistory.setOldStatus(project.getServiceStatus());
                operationHistory.setOldProjectId(project.getId());
                operationHistory.setOperationType("YI_TUI_CHANG");
                operationHistory.setOperationTime(new Date());
                operationHistory.setOperationMan(SecurityUtils.getUserId());
                operationHistory.setDeleted(0);
                projectOperationHistoryMapper.insert(operationHistory);

                project.setServiceStatus("YI_TUI_CHANG");
                projectService.updateById(project);

                //迭代二：退场，发起报销申请流程
                if (ProjectStepConstant.DAN_YI_TOU_BIAO_XIANG_MU.equals(project.getProjectType())) {
                    iInterfaceLogService.sentReimbursementApplication( project.getId().toString(),"YES");
                }
                projectExtension.setProcessState(ProjectExtensionExitStatus.YI_CHU_LI.getCode());
            } else if (oaApproval.getApprovalStatus().equals(OAApprovalStatus.BO_HUI.getCode()) || oaApproval.getApprovalStatus().equals(OAApprovalStatus.ZUO_FEI.getCode())) {
                projectExtension.setProcessState(ProjectExtensionExitStatus.BO_HUI.getCode());
            } else if (oaApproval.getApprovalStatus().equals(OAApprovalStatus.SHEN_PI_ZHONG.getCode())) {
                projectExtension.setProcessState(ProjectExtensionExitStatus.SHEN_PI_ZHONG.getCode());
            }
            exit.setApprovalStatus(oaApproval.getApprovalStatus());
            exit.setApprovalUrl(oaApproval.getApprovalUrl());
            exitService.updateById(exit);
            extensionService.updateById(projectExtension);
        }


    }

}
