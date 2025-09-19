package com.bytefinger.toutuo.biz.projectcompany.oaapproval;

import com.alibaba.fastjson2.JSONObject;
import com.bytefinger.common.core.utils.CamelUtils;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.toutuo.api.biz.project.enums.OAApprovalStatus;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.biz.companyexit.domain.ProjectCompanyExit;
import com.bytefinger.toutuo.biz.companyexit.service.IProjectCompanyExitService;
import com.bytefinger.toutuo.biz.companyhigh.domain.ProjectCompanyHighLevelMeetingApproval;
import com.bytefinger.toutuo.biz.companyhigh.service.IProjectCompanyHighLevelMeetingApprovalService;
import com.bytefinger.toutuo.biz.companyrisk.domain.ProjectCompanyRiskInspectionApproval;
import com.bytefinger.toutuo.biz.companyrisk.service.IProjectCompanyRiskInspectionApprovalService;
import com.bytefinger.toutuo.biz.oa.constants.OaConstant;
import com.bytefinger.toutuo.biz.oa.domain.OaApproval;
import com.bytefinger.toutuo.biz.operlog.domain.vo.UpdateLog;
import com.bytefinger.toutuo.biz.operlog.service.IOperLogService;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.enums.ProjectCompanyStatus;
import com.bytefinger.toutuo.biz.projectcompany.oaapproval.constant.CompanyOaConstant;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyService;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import com.bytefinger.toutuo.common.constants.MessageTypeConstant;
import com.bytefinger.toutuo.common.domain.BizUserBaseEntity;
import com.bytefinger.toutuo.common.enums.ModuleType;
import com.bytefinger.toutuo.common.mapper.CommonMapper;
import com.bytefinger.toutuo.common.service.SendMessageService;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Jone Ying
 * @Date 2023/3/20
 **/
@Service
@AllArgsConstructor
public class CompanyOaApprovalService {

    private final IProjectCompanyRiskInspectionApprovalService riskInspectionApprovalService;

    private final IProjectCompanyHighLevelMeetingApprovalService highLevelMeetingApprovalService;

    private final IProjectCompanyExitService exitService;

    private final IProjectCompanyService companyService;

    private final CommonMapper commonMapper;

    private final IProjectService projectService;

    private final ISysUserService userService;

    private final SendMessageService sendMessageService;

    private final IOperLogService operLogService;

    public void oaApproval(OaApproval oaApproval){

        switch (oaApproval.getSubModuleName()){
            case CompanyOaConstant.RISK_INSPECTION :
                this.riskInspectionApproval(oaApproval);
                break;
            case CompanyOaConstant.HIGH_LEVEL_MEETING :
                this.highLevelMeetingApproval(oaApproval);
                break;
            case CompanyOaConstant.PROJECT_EXIT :
                this.projectExit(oaApproval);
                break;
            default :
                break;
        }
    }

    private void riskInspectionApproval(OaApproval oaApproval){
        if(CompanyOaConstant.RISK_INSPECTION_PROCESS.equals(oaApproval.getSubRecordAction())){
            this.riskInspectionProcessApproval(oaApproval);
        }else if(CompanyOaConstant.RISK_INSPECTION_RELIEVE.equals(oaApproval.getSubRecordAction())){
            this.riskInspectionRelieveApproval(oaApproval);
        }
    }

    private void riskInspectionProcessApproval(OaApproval oaApproval){
        ProjectCompanyRiskInspectionApproval riskInspectionApproval = riskInspectionApprovalService.getById(oaApproval.getSubRecordId());
        Integer beforeValue = riskInspectionApproval.getProcessApprovalStatus();
        riskInspectionApproval.setProcessApprovalStatus(oaApproval.getApprovalStatus());
        riskInspectionApproval.setProcessApprovalSendTime(oaApproval.getCreateTime());
        riskInspectionApproval.setProcessApprovalUrl(oaApproval.getApprovalUrl());
        riskInspectionApproval.setProcessApprovalUserId(oaApproval.getSubmitUserId());
        riskInspectionApprovalService.updateById(riskInspectionApproval);
        recordLog(oaApproval, beforeValue);
    }

    private void recordLog(OaApproval oaApproval, Integer beforeValue) {
        UpdateLog log = UpdateLog.builder()
                .fieldName(oaApproval.getSubject() + "审批状态变更，流程链接：" + oaApproval.getApprovalUrl())
                .valueBefore(null == beforeValue ? null : OAApprovalStatus.getByCode(beforeValue).getDesc())
                .valueAfter(OAApprovalStatus.getByCode(oaApproval.getApprovalStatus()).getDesc())
                .build();
        operLogService.ruleLogBySystem(ProjectCompany.class.getSimpleName(), oaApproval.getRecordId(), log);

        //发送通知
        Set<Long> ids = Sets.newHashSet();
        ProjectCompany company = companyService.getById(oaApproval.getRecordId());
        ids.add(company.getPrincipalId());
        Project project = projectService.getById(company.getProjectId());
        List<BizUserBaseEntity> bizUsers = commonMapper.listBizUser(
                CamelUtils.camelToUndeline(OaConstant.PROJECT),
                Arrays.asList(new Long[]{project.getId()}));
        if (!bizUsers.isEmpty()) {
            ids.addAll(bizUsers.stream().map(BizUserBaseEntity::getUserId).collect(Collectors.toList()));
        }
        if(CompanyOaConstant.PROJECT_EXIT.equals(oaApproval.getSubModuleName())){
            ids.add(project.getAttributorUserId());
        }
        if(!CollectionUtils.isEmpty(ids)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("approvalStatus",OAApprovalStatus.getByCode(oaApproval.getApprovalStatus()).getDesc());
            jsonObject.put("approvalUrl",oaApproval.getApprovalUrl());
            SysUser user = userService.getById(oaApproval.getSubmitUserId());
            jsonObject.put("submitUserName",user.getRealname());
            jsonObject.put("submitTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, oaApproval.getCreateTime()));
            ids.forEach(v -> {
                sendMessageService.sendMessage(MessageTypeConstant.MESSAGE_APPROVAL, v, project.getId(),
                         oaApproval.getSubject(), jsonObject.toJSONString(), ModuleType.COMPANY.getCode(), "XI_TONG_TONG_ZHI" + "," + "DAI_BAN_TONG_ZHI");
            });
        }
    }

    private void riskInspectionRelieveApproval(OaApproval oaApproval){
        ProjectCompanyRiskInspectionApproval riskInspectionApproval = riskInspectionApprovalService.getById(oaApproval.getSubRecordId());
        Integer beforeValue = riskInspectionApproval.getRelieveApprovalStatus();
        riskInspectionApproval.setRelieveApprovalStatus(oaApproval.getApprovalStatus());
        riskInspectionApproval.setRelieveApprovalSendTime(oaApproval.getCreateTime());
        riskInspectionApproval.setRelieveApprovalUrl(oaApproval.getApprovalUrl());
        riskInspectionApproval.setRelieveApprovalUserId(oaApproval.getSubmitUserId());
        riskInspectionApprovalService.updateById(riskInspectionApproval);
        recordLog(oaApproval, beforeValue);
    }

    private void highLevelMeetingApproval(OaApproval oaApproval){
        ProjectCompanyHighLevelMeetingApproval highLevelMeetingApproval = highLevelMeetingApprovalService.getById(oaApproval.getSubRecordId());
        Integer beforeValue = highLevelMeetingApproval.getApprovalStatus();
        highLevelMeetingApproval.setApprovalStatus(oaApproval.getApprovalStatus());
        highLevelMeetingApproval.setApprovalSendTime(oaApproval.getCreateTime());
        highLevelMeetingApproval.setApprovalUrl(oaApproval.getApprovalUrl());
        highLevelMeetingApproval.setApprovalUserId(oaApproval.getSubmitUserId());
        highLevelMeetingApprovalService.updateById(highLevelMeetingApproval);
        recordLog(oaApproval, beforeValue);
    }

    private void projectExit(OaApproval oaApproval){
        ProjectCompanyExit exit = exitService.getById(oaApproval.getSubRecordId());
        Integer beforeValue = exit.getApprovalStatus();
        exit.setApprovalStatus(oaApproval.getApprovalStatus());
        exit.setApprovalSendTime(oaApproval.getCreateTime());
        exit.setApprovalUserId(oaApproval.getSubmitUserId());
        exit.setApprovalUrl(oaApproval.getApprovalUrl());
        exitService.updateById(exit);
        recordLog(oaApproval, beforeValue);
        if (oaApproval.getApprovalStatus() == OAApprovalStatus.TONG_GUO.getCode()) {
            ProjectCompany company = companyService.getById(exit.getCompanyId());
            company.setExitTime(exit.getExitTime());
            company.setServiceStatus(ProjectCompanyStatus.YI_TUI_CHU.getCode());
            companyService.updateById(company);
        }
    }

}
