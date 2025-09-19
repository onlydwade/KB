package com.bytefinger.toutuo.biz.project.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.enums.ShiFou;
import com.bytefinger.common.core.service.ThisService;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.DictUtils;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.system.core.domain.SysDictData;
import com.bytefinger.toutuo.api.system.core.domain.SysDictType;
import com.bytefinger.toutuo.biz.interfacelog.enums.InterfaceStatusEnums;
import com.bytefinger.toutuo.biz.interfacelog.service.IInterfaceLogService;
import com.bytefinger.toutuo.biz.oa.service.IOaApprovalService;
import com.bytefinger.toutuo.biz.performance.domain.dto.ProjectDaoQiDto;
import com.bytefinger.toutuo.biz.performance.domain.excel.ActualInExcelSummaryHeadVO;
import com.bytefinger.toutuo.biz.performance.domain.vo.ProjectListStepVo;
import com.bytefinger.toutuo.biz.performance.domain.vo.ProjectOpenMarkExportVo;
import com.bytefinger.toutuo.biz.performance.domain.vo.SelectCommonVo;
import com.bytefinger.toutuo.biz.performance.service.IBudgetInService;
import com.bytefinger.toutuo.biz.project.bo.ProjectAchievementBO;
import com.bytefinger.toutuo.biz.project.constants.ProjectConstant;
import com.bytefinger.toutuo.biz.project.domain.*;
import com.bytefinger.toutuo.biz.project.dto.ProjectNotifyDto;
import com.bytefinger.toutuo.biz.project.dto.SaveClosureInfoDto;
import com.bytefinger.toutuo.biz.project.enums.NotifyTypeEnums;
import com.bytefinger.toutuo.biz.project.enums.ProjectStatus;
import com.bytefinger.toutuo.biz.project.mapper.*;
import com.bytefinger.toutuo.biz.project.service.IProjectBidService;
import com.bytefinger.toutuo.biz.project.service.IProjectEstimatedCostService;
import com.bytefinger.toutuo.biz.project.service.IProjectRiskInspectionService;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionService;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStep;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenu;
import com.bytefinger.toutuo.biz.projectstep.mapper.ProjectStepMapper;
import com.bytefinger.toutuo.biz.projectstep.mapper.ProjectStepMenuMapper;
import com.bytefinger.toutuo.biz.user.mapper.SysUserMapper;
import com.bytefinger.toutuo.common.service.BizService;
import com.bytefinger.toutuo.common.util.ExcelUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目（立项）基础信息 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-06
 */
@Service
@AllArgsConstructor
public class ProjectServiceImpl extends BizService<ProjectMapper, Project> implements IProjectService, ThisService<ProjectServiceImpl> {

    private final ProjectMapper projectMapper;

    private final ProjectStepMenuMapper projectStepMenuMapper;

    private final ProjectStepMapper projectStepMapper;

    private final SysUserMapper userMapper;

    private final IProjectRiskInspectionService riskInspectionService;

    private final IProjectCompanyService projectCompanyService;

    private final IProjectEstimatedCostService projectEstimatedCostService;

    private final ProjectExpandMapper projectExpandMapper;

    private final IInterfaceLogService iInterfaceLogService;

    private final ProjectOperationHistoryMapper projectOperationHistoryMapper;

    private final ProjectNotifyMapper projectNotifyMapper;

    private final IProjectBidService projectBidService;

    private final IBudgetInService budgetInService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Project> add(Project project) {
        project.setProjectNo(super.getNo());
        project.setFollowTime(new Date());
        project.setUpdateStatusDate(new Date());

        //新增默认 是否在管，是否同步  为否
        project.setIsSyncOperation("FOU");
        project.setIsInManagement("FOU");
        project = super.add4Log(project);

        saveProjectExpand(project);
        project = getProjectExpand(project);

        return R.ok(project);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Project> add2(Project project) {
        project = super.add4Log(project);

        saveProjectExpand(project);
        project = getProjectExpand(project);

        return R.ok(project);
    }

    public static boolean areStringsDifferent(String str1, String str2) {
        // 如果两个字符串都为null，则它们没有变化，视为相同
        if (str1 == null && str2 == null) {
            return false;
        }
        // 如果一个字符串为null，另一个不为null，或者它们的值不同
        return str1 == null || str2 == null || !str1.equals(str2);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Project> update(Project project) {

        ProjectOperationHistory operationHistory = new ProjectOperationHistory();

        Project oldProject = this.getById(project.getId());

        project.setFollowTime(new Date());
        project.setUpdateStatusDate(new Date());
        saveProjectExpand(project);
        //如果服务内容不包含其他  则清除掉其他服务内容字段
        if (project.getServiceContent() != null && !project.getServiceContent().contains("QI_TA")) {
            project.setServiceContentOther("");
        }
        Project resultProject = super.update4Log(project);
        resultProject = getProjectExpand(project);
        saveClosureInfo(project.getId(), project.getProjectEstimatedCosts());

        //如果是否续签和是否增量有修改，需要修改统计数据
        if (areStringsDifferent(project.getInStock(), oldProject.getInStock()) || areStringsDifferent(project.getIsPerformanceIncrement(), oldProject.getIsPerformanceIncrement())) {
            budgetInService.calcPerformanceAllocationData(project.getId());
        }

        //通过了项目立项节点，并且修改了参标组织需要推送报销系统
        if (isPassLxsp(project.getId()) && project.getBidPartInCompany() != null && areStringsDifferent(oldProject.getBidPartInCompany(), project.getBidPartInCompany())) {
            //修改投标复盘节点我方的参标单位
            projectBidService.update(new ProjectBid(), new LambdaUpdateWrapper<ProjectBid>()
                    .eq(ProjectBid::getProjectId, project.getId())
                    .eq(ProjectBid::getType, 1)
                    .eq(ProjectBid::getDeleted, 0)
                    .set(ProjectBid::getCompany, project.getBidPartInCompany())
            );
            iInterfaceLogService.sentReimbursementApplication(project.getId().toString(), "YES");
        }

        //废止-调用报销系统作废接口
        if (ProjectStatus.YI_FEI_ZHI.getCode().equals(project.getServiceStatus())) {
            iInterfaceLogService.sentReimbursementApplication(project.getId().toString(), "NO");
        }

        if (ProjectStatus.YI_WAN_JIE.getCode().equals(project.getServiceStatus())) {
            iInterfaceLogService.sentReimbursementApplication(project.getId().toString(), "YES");


            //记录到操作历史
            operationHistory.setOldStatus(oldProject.getServiceStatus());
            operationHistory.setOldProjectId(project.getId());
            operationHistory.setOperationType("YI_WAN_JIE");
            operationHistory.setOperationTime(new Date());
            operationHistory.setOperationMan(SecurityUtils.getUserId());
            operationHistory.setDeleted(0);
            projectOperationHistoryMapper.insert(operationHistory);
        }

        return R.ok();
    }

    //保存拓展表字段数据
    public void saveProjectExpand(Project project) {
        ProjectExpand projectExpand = getProjectExpandById(project.getId());

        if (projectExpand == null) {
            projectExpand = new ProjectExpand();
        }

        projectExpand.setHrOrg(project.getHrOrg());
        projectExpand.setProjectId(project.getId());
        projectExpand.setOperationMode(project.getOperationMode());
        projectExpand.setOutsourcingUnitName(project.getOutsourcingUnitName());
        projectExpand.setRevenueInfo(project.getRevenueInfo());
        projectExpand.setAnnualAvgProfitMargin(project.getAnnualAvgProfitMargin());
        projectExpand.setProjectedProfitMargin(project.getProjectedProfitMargin());
        projectExpand.setIsBranchOffice(project.getIsBranchOffice());
        projectExpand.setProjectManagementUnit(project.getProjectManagementUnit());
        projectExpand.setOpportunityId(project.getOpportunityId());

        projectExpand.setAuthorizedEntity(project.getAuthorizedEntity());
        projectExpand.setAuthorizationContent(project.getAuthorizationContent());
        projectExpand.setDueDiligenceContent(project.getDueDiligenceContent());
        projectExpand.setDueDiligenceMethod(project.getDueDiligenceMethod());
        projectExpand.setBorrowingTime(project.getBorrowingTime());
        projectExpand.setReturnTime(project.getReturnTime());
        projectExpand.setSimilarity(project.getSimilarity());
        projectExpand.setSecondParty(project.getSecondParty());

        if (projectExpand.getId() == null) {
            projectExpandMapper.insert(projectExpand);
        } else {
            //只修改结项节点的字段
            projectExpandMapper.updateById(projectExpand);
        }
    }

    //获取拓展表字段数据
    public Project getProjectExpand(Project project) {
        ProjectExpand projectExpand = getProjectExpandById(project.getId());
        if (projectExpand == null)
            return project;
        project.setOperationMode(projectExpand.getOperationMode());
        project.setOutsourcingUnitName(projectExpand.getOutsourcingUnitName());
        project.setRevenueInfo(projectExpand.getRevenueInfo());
        project.setAnnualAvgProfitMargin(projectExpand.getAnnualAvgProfitMargin());
        project.setProjectedProfitMargin(projectExpand.getProjectedProfitMargin());
        project.setIsBranchOffice(projectExpand.getIsBranchOffice());
        project.setProjectManagementUnit(projectExpand.getProjectManagementUnit());

        project.setAuthorizedEntity(projectExpand.getAuthorizedEntity());
        project.setAuthorizationContent(projectExpand.getAuthorizationContent());
        project.setDueDiligenceContent(projectExpand.getDueDiligenceContent());
        project.setDueDiligenceMethod(projectExpand.getDueDiligenceMethod());

        project.setHrOrg(projectExpand.getHrOrg());
        project.setOpportunityId(projectExpand.getOpportunityId());
        project.setBorrowingTime(projectExpand.getBorrowingTime());
        project.setReturnTime(projectExpand.getReturnTime());
        project.setSimilarity(projectExpand.getSimilarity());
        project.setSecondParty(projectExpand.getSecondParty());
        List<SysDictData> sysDictDataList = DictUtils.getDictCache("YUN_YING_MO_SHI");
        for (int i = 0; i < sysDictDataList.size(); i++) {
            if (sysDictDataList.get(i).getDictValue().equals(projectExpand.getOperationMode())) {
                project.setOperationModeStr(sysDictDataList.get(i).getDictLabel());
            }
        }

        //projectExpand.getOperationMode()
        //project.setOperationModeStr(projectExpand.getOperationModeStr());
        return project;
    }

    @Override
    public Project getEnumCodeToStr(Project project, List<String> DictDataTypeList) {
        for (String dictType : DictDataTypeList) {
            if (StringUtils.isBlank(dictType)) {
                continue;
            }
            List<String> dictName = new ArrayList<>();
            String keys = null;
            List<SysDictData> dictDataList = DictUtils.getDictCache(dictType);
            if (CollectionUtils.isEmpty(dictDataList)) {
                continue;
            }
            // 将List对象集合转换为Map<String, String>
            Map<String, String> dictDataMap = dictDataList.stream()
                    .collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel));
            switch (dictType) {
                //项目评审的重点关注项目枚举
                case "ZHONG_DIAN_GUAN_ZHU_XIANG_MU":
                    keys = project.getKeyFocusProject();
                    break;
                //是否，设置是否涉及成立分公司字段
                case "SHI_FOU":
                    keys = project.getIsBranchOffice();
                    break;
                default:
                    //----后续可以继续添加其他字段的枚举判断
            }
            //获取具体字段的值，用于转换枚举名称
            if (StringUtils.isNotBlank(keys)) {
                if (keys.contains(",")) {
                    List<String> keyFocusProjectList = Arrays.asList(keys.split(","));
                    keyFocusProjectList.forEach(keyFocusProjectCode -> {
                        String dictLabel = dictDataMap.get(keyFocusProjectCode);
                        if (dictLabel != null) {
                            dictName.add(dictLabel);
                        }
                    });
                } else {
                    String dictLabel = dictDataMap.get(keys);
                    if (dictLabel != null) {
                        dictName.add(dictLabel);
                    }
                }
            }
            //项目评审的重点关注项目枚举
            if (!CollectionUtils.isEmpty(dictName) && dictType.equals("ZHONG_DIAN_GUAN_ZHU_XIANG_MU")) {
                project.setKeyFocusProjectStr(String.join(",", dictName));
            }
            //是否，设置是否涉及成立分公司字段
            else if (!CollectionUtils.isEmpty(dictName) && dictType.equals("SHI_FOU")) {
                project.setIsBranchOfficeStr(dictName.get(0));
            }
            //----后续可以继续添加其他字段的枚举判断
        }
        return project;
    }

    public ProjectExpand getProjectExpandById(Long projectId) {
        QueryWrapper<ProjectExpand> queryDataWrapper = new QueryWrapper<>();
        queryDataWrapper.in("project_id", projectId);
        List<ProjectExpand> projectExpandList = projectExpandMapper.selectList(queryDataWrapper);
        if (CollectionUtils.isEmpty(projectExpandList))
            return null;
        return projectExpandList.get(0);
    }

    //保存项目结项信息
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Boolean> saveClosureInfo(Long projectId, List<ProjectEstimatedCost> projectEstimatedCosts) {

        //拓展数据为空
//        if (saveClosureInfoDto.getProjectExpand() == null) {
//            return R.fail("结项拓展数据不能为空");
//        }
//        ProjectExpand projectExpand = saveClosureInfoDto.getProjectExpand();
//
//        QueryWrapper<ProjectExpand> queryDataWrapper = new QueryWrapper<>();
//        queryDataWrapper.in("project_id", saveClosureInfoDto.getProjectId());
//        List<ProjectExpand> projectExpandList = projectExpandMapper.selectList(queryDataWrapper);
//
//        if (CollectionUtils.isEmpty(projectExpandList)) {
//            projectExpandMapper.insert(projectExpand);
//        } else {
//            //只修改结项节点的字段
//            ProjectExpand saveProjectExpand = projectExpandList.get(0);
//            saveProjectExpand.setOperationMode(projectExpand.getOperationMode());
//            saveProjectExpand.setOutsourcingUnitName(projectExpand.getOutsourcingUnitName());
//            saveProjectExpand.setRevenueInfo(projectExpand.getRevenueInfo());
//            saveProjectExpand.setAnnualAvgProfitMargin(projectExpand.getAnnualAvgProfitMargin());
//            saveProjectExpand.setProjectedProfitMargin(projectExpand.getProjectedProfitMargin());
//            projectExpandMapper.updateById(saveProjectExpand);
//        }

        QueryWrapper<ProjectEstimatedCost> queryEsDataWrapper = new QueryWrapper<>();
        queryEsDataWrapper.in("project_id", projectId);
        List<ProjectEstimatedCost> projectEstimatedCostList = projectEstimatedCostService.list(queryEsDataWrapper);
        if (!CollectionUtils.isEmpty(projectEstimatedCostList)) {
            projectEstimatedCostService.removeByIds(projectEstimatedCostList.stream().map(ProjectEstimatedCost::getId).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(projectEstimatedCosts)) {
            projectEstimatedCosts.forEach(o -> {
                o.setId(null);
            });
            projectEstimatedCostService.saveBatch(projectEstimatedCosts);
        }

        return R.ok(true);
    }

    //获取结项信息
    @Override
    public SaveClosureInfoDto getClosureInfo(Long id) {
        SaveClosureInfoDto saveClosureInfoDto = new SaveClosureInfoDto();
        QueryWrapper<ProjectExpand> queryDataWrapper = new QueryWrapper<>();
        queryDataWrapper.in("project_id", id);
        List<ProjectExpand> projectExpandList = projectExpandMapper.selectList(queryDataWrapper);
        if (!CollectionUtils.isEmpty(projectExpandList)) {
            saveClosureInfoDto.setProjectExpand(projectExpandList.get(0));
        }

        QueryWrapper<ProjectEstimatedCost> queryEsDataWrapper = new QueryWrapper<>();
        queryEsDataWrapper.in("project_id", id);
        List<ProjectEstimatedCost> projectEstimatedCostList = projectEstimatedCostService.list(queryEsDataWrapper);
        if (!CollectionUtils.isEmpty(projectEstimatedCostList)) {
            saveClosureInfoDto.setProjectEstimatedCosts(projectEstimatedCostList);
        }

        saveClosureInfoDto.setProjectId(id);
        return saveClosureInfoDto;
    }

    @DataFill
    @Override
    public IPage<Project> page(QueryPage queryPage) {

        List<String> expandServiceStatuList = Lists.newArrayList();
        String expandServiceStatus = "expandServiceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandServiceStatus))) {
            expandServiceStatuList.addAll(queryPage.getInParams().get(expandServiceStatus).stream().map(Object::toString).collect(Collectors.toList()));
            queryPage.getInParams().remove(expandServiceStatus);
            queryPage.getDbParams().put("expandServiceStatus", expandServiceStatuList);
        }

        List<String> serviceStatuList = Lists.newArrayList();
        String serviceStatus = "serviceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceStatus))) {
            serviceStatuList.addAll(queryPage.getInParams().get(serviceStatus).stream().map(Object::toString).collect(Collectors.toList()));
            queryPage.getInParams().remove(serviceStatus);
            queryPage.getDbParams().put("serviceStatus", serviceStatuList);
        }

        List<String> finishStepIdList = Lists.newArrayList();
        String finishStepId = "finishStepId";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(finishStepId))) {
            finishStepIdList.addAll(queryPage.getInParams().get(finishStepId).stream().map(Object::toString).collect(Collectors.toList()));
            queryPage.getInParams().remove(finishStepId);
            queryPage.getDbParams().put("finishStepId", finishStepIdList);
        }

        List<String> expandCompanyIdList = Lists.newArrayList();
        String expandCompanyId = "expandCompanyId";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandCompanyId))) {
            expandCompanyIdList.addAll(queryPage.getInParams().get(expandCompanyId).stream().map(Object::toString).collect(Collectors.toList()));
            queryPage.getInParams().remove(expandCompanyId);
            queryPage.getDbParams().put("expandCompanyId", expandCompanyIdList);
        }

        String isRenewalTender = "isRenewalTender";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(isRenewalTender))) {
            queryPage.getDbParams().put("isRenewalTender", queryPage.getParams().get(isRenewalTender));
            queryPage.getParams().remove(isRenewalTender);
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

        IPage<Project> projectIPage = projectMapper.list(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());

        if (ObjectUtils.isNotEmpty(projectIPage.getRecords()) && projectIPage.getRecords().size() > 0) {

            List<ProjectListStepVo> projectListStepVos = projectMapper.getProjectListStep(projectIPage.getRecords().stream().map(Project::getId).collect(Collectors.toList()));
            Map<Long, List<ProjectListStepVo>> mapStepList = projectListStepVos.stream().collect(Collectors.groupingBy(ProjectListStepVo::getProjectId));

            projectIPage.setRecords(getYtProjetList(projectIPage.getRecords()));

            for (Project project : projectIPage.getRecords()) {
                if (null != project.getContractAmount() && null != project.getProposedServicePeriod() && !project.getProposedServicePeriod().equals(0)) {
                    project.setAnnualConversionAmount(
                            this.calculateAnnualConversion(
                                    project.getContractAmount(),
                                    project.getProposedServicePeriod(),
                                    project.getServiceBeginTime(),
                                    project.getServiceEndTime(),
                                    project.getPerformanceConfirmTime()
                            )
                    );
                }

                List<ProjectListStepVo> itemSteps = mapStepList.get(project.getId());
                if (!CollectionUtils.isEmpty(itemSteps)) {
                    project.setStepName(itemSteps.get(0).getStepName());
                }
            }

        }
        return projectIPage;
//        return projectMapper.list(queryPage.toPage(), queryPage.getWrapperByPrefix("project."), queryPage.getDbParams());
    }


    @DataFill
    @Override
    public IPage<Project> projectPageFromCustomer(QueryPage queryPage) {
        IPage<Project> projectIPage = projectMapper.projectPageFromCustomer(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());

        if (ObjectUtils.isNotEmpty(projectIPage.getRecords()) && projectIPage.getRecords().size() > 0) {

            projectIPage.setRecords(getYtProjetList(projectIPage.getRecords()));

            for (Project project : projectIPage.getRecords()) {
                if (null != project.getContractAmount() && null != project.getProposedServicePeriod() && !project.getProposedServicePeriod().equals(0)) {
                    String isIncrement = project.getIsIncrement();
                    BigDecimal annualConversionAmount;

                    //增量取增量合同金额  非增量取合同总金额
                    if (ShiFou.SHI.getCode().equals(isIncrement)) {
                        annualConversionAmount = this.calculateAnnualConversion(
                                project.getContractAmounts(),
                                project.getProposedServicePeriod(),
                                project.getServiceBeginTime(),
                                project.getServiceEndTime(),
                                project.getPerformanceConfirmTime()
                        );
                        project.setAnnualConversionAmounts(annualConversionAmount);
                    } else {
                        annualConversionAmount = this.calculateAnnualConversion(
                                project.getContractAmount(),
                                project.getProposedServicePeriod(),
                                project.getServiceBeginTime(),
                                project.getServiceEndTime(),
                                project.getPerformanceConfirmTime()
                        );
                        project.setAnnualConversionAmount(annualConversionAmount);
                    }
                }
            }
        }
        return projectIPage;
//        return projectMapper.list(queryPage.toPage(), queryPage.getWrapperByPrefix("project."), queryPage.getDbParams());
    }

    public List<Project> getYtProjetList(List<Project> projectList) {

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

            if ("GU_QUAN_HE_ZUO_XIANG_MU".equals(projectList.get(i).getProjectType())) {
                businessType = projectList.get(i).getTargetBusinessType();
            }

            List<SysDictData> itemList = mapDicData.get(businessType);
            if (CollectionUtils.isEmpty(itemList))
                continue;

//            List<SysDictData> dicDataList = itemList.stream().filter(m -> "XIANG_MU_YE_TAI".equals(m.getDictType())).collect(Collectors.toList());
//            if (CollectionUtils.isEmpty(dicDataList)) {
//                continue;
//            }

            SysDictData sysDictData = itemList.get(0);
            String dictType = sysDictData.getDictType();
            List<SysDictType> itemDictTypeList = dictTypeList.stream().filter(m -> m.getDictType().equals(dictType)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(itemDictTypeList))
                continue;

            String dictStr = itemDictTypeList.get(0).getDictName() + "-" + sysDictData.getDictLabel();

            if ("GU_QUAN_HE_ZUO_XIANG_MU".equals(projectList.get(i).getProjectType())) {
                projectList.get(i).setTargetBusinessTypeStr(dictStr);
            } else {
                projectList.get(i).setBusinessTypeStr(dictStr);
            }
        }
        return projectList;
    }

    public Project getYtProjet(Project project) {
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        projects = getYtProjetList(projects);
        return projects.get(0);
    }

    @DataFill
    @Override
    public IPage<ProjectAchievementBO> pageTwo(QueryPage queryPage) {

        List<String> expandServiceStatuList = Lists.newArrayList();
        String expandServiceStatus = "expandServiceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandServiceStatus))) {
            expandServiceStatuList.addAll(queryPage.getInParams().get(expandServiceStatus).stream().map(Object::toString).collect(Collectors.toList()));
            queryPage.getInParams().remove(expandServiceStatus);
            queryPage.getDbParams().put("expandServiceStatus", expandServiceStatuList);
        }

        List<String> serviceStatuList = Lists.newArrayList();
        String serviceStatus = "serviceStatus";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceStatus))) {
            serviceStatuList.addAll(queryPage.getInParams().get(serviceStatus).stream().map(Object::toString).collect(Collectors.toList()));
            queryPage.getInParams().remove(serviceStatus);
            queryPage.getDbParams().put("serviceStatus", serviceStatuList);
        }

        List<String> expandCompanyIdList = Lists.newArrayList();
        String expandCompanyId = "expandCompanyId";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(expandCompanyId))) {
            expandCompanyIdList.addAll(queryPage.getInParams().get(expandCompanyId).stream().map(Object::toString).collect(Collectors.toList()));
            queryPage.getInParams().remove(expandCompanyId);
            queryPage.getDbParams().put("expandCompanyId", expandCompanyIdList);
        }

        String isRenewalTender = "isRenewalTender";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(isRenewalTender))) {
            queryPage.getDbParams().put("isRenewalTender", queryPage.getParams().get(isRenewalTender));
            queryPage.getParams().remove(isRenewalTender);
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


        IPage<ProjectAchievementBO> projectIPage = projectMapper.listTwo(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());


        if (ObjectUtils.isNotEmpty(projectIPage.getRecords()) && projectIPage.getRecords().size() > 0) {
            for (ProjectAchievementBO project : projectIPage.getRecords()) {
                if (null != project.getContractAmount() && null != project.getProposedServicePeriod()) {
                    project.setAnnualConversionAmount(
                            this.calculateAnnualConversion(
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
//        return projectMapper.list(queryPage.toPage(), queryPage.getWrapperByPrefix("project."), queryPage.getDbParams());
    }

    @Override
    public IPage pageNotify(QueryPage queryPage) {
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get("companyIds"))) {
            queryPage.getDbParams().put("companyIds", queryPage.getInParams().get("companyIds"));
        }
        IPage<ProjectNotifyDto> projectNotifyDtoIPage = projectMapper.pageNotify(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());
        List<ProjectNotifyDto> dtoList = projectNotifyDtoIPage.getRecords();

        if (ObjectUtil.isNotEmpty(dtoList)) {
            for (ProjectNotifyDto dto : dtoList) {
                dto.setId(dto.getProjectId());
                dto.setNotifyTypeName(NotifyTypeEnums.getDescByCode(dto.getNotifyType()));
            }
            projectNotifyDtoIPage.setRecords(dtoList);
        }
        return projectNotifyDtoIPage;
    }

    @Override
    public IPage pageEndNotify(QueryPage queryPage) {
        return projectMapper.pageEndNotify(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());
    }

    //    将40天未维护（最后更新时间40天没改过），并且项目评审节点未完成或者没有到项目评审这个节点的项目，标记为失效
    @Override
    public void handleExpireProject() {
        System.out.println("处理40天未维护，并且项目评审节点未完成或者没有到项目评审这个节点的项目-----开始");
        List<Project> projectList = this.baseMapper.expireProjectList();
        if (ObjectUtil.isEmpty(projectList)) {
            return;
        }
        for (Project pro : projectList) {
            pro.setExpireReason("长期未维护自动标记为失效");
            pro.setExpireTime(new Date());
            pro.setExpire(ProjectStatus.YI_SHI_XIAO.getCode());
        }
        this.updateBatchById(projectList);
        System.out.println("处理40天未维护，并且项目评审节点未完成或者没有到项目评审这个节点的项目-----结束");
    }

    @Override
    public void updateProjectValid(Long id) {
        this.baseMapper.updateProjectValid(id);

        //判断是否需要推送到报销系统
        isSentReimbursementApplication(id, true);
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
    public Project changeAttributorUser(Long id, Long dispatchUserId) {
        Project project = this.getById(id);
        //同类里面，切面不会进入返回切面点方法
        SysUser user = userMapper.selectById(project.getAttributorUserId());
        String bValue = user.getRealname();
        project.setFollowTime(new Date());
        project.setAttributorUserId(dispatchUserId);
//        ((BizBaseEntity) entity).setDispatchType(DataRole.currUserHasShowAllOrSub() ? DispacthType.SHANG_JI_ZHUAN_PAI.getCode() : DispacthType.TONG_JI_ZHUAN_PAI.getCode());
        this.updateById(project);

        project = this.getById(id);
        user = userMapper.selectById(project.getAttributorUserId());
        operLogService.changeLog(project.getClass(), project.getId(), "变更归属人", bValue, user.getRealname());
        return project;
    }

    @Override
    public Project changeBidingAmount(Long projectId, String bidingAmount) {
        Project project = projectMapper.selectById(projectId);
        project.setBidingAmount(bidingAmount);
        projectMapper.updateById(project);
        return project;
    }

    @Override
    public Project changeExpire(Long id, String expireReason) {
        Project project = getById(id);
        String bValue = project.getExpire();
        project.setExpireTime(new Date());
        project.setExpire(ProjectConstant.XIANG_MU_SHI_XIAO_ZHUANG_TAI_YI_SHI_XIAO);
        // project.setServiceStatus(ProjectStatus.YI_SHI_XIAO.getCode()); //已失效 项目业务状态已失效
        project.setExpireReason(expireReason);

        super.updateById(project);
        if (StringUtils.isNotBlank(bValue)) {
            bValue = ProjectStatus.getByCode(bValue).getDesc();
        }
        operLogService.changeLog(project.getClass(), project.getId(), "变更项目为失效状态", bValue, ProjectStatus.getByCode(project.getExpire()).getDesc());
        //调用作废接口
        isSentReimbursementApplication(id, false);
        return project;
    }

    private void isSentReimbursementApplication(Long id, Boolean type) {
        ProjectStepMenu stepMenu = this.projectStepMenuMapper.selectOne(Wrappers.<ProjectStepMenu>lambdaQuery()
                .eq(ProjectStepMenu::getCode, "lxsp")
        );

        if (stepMenu != null) {
            Integer count = this.projectStepMapper.selectCount(Wrappers.<ProjectStep>lambdaQuery()
                    .eq(ProjectStep::getProjectId, id)
                    .eq(ProjectStep::getStepMenuId, stepMenu.getId())
            );
            //已通过立项审批节点  需要推送到报销系统
            if (count > 0) {
                if (type) {
                    iInterfaceLogService.sentReimbursementApplication(id.toString(), "YES");
                } else {
                    iInterfaceLogService.sentReimbursementApplication(id.toString(), "NO");
                }

            }
        }
    }

    /**
     * 是否通过了立项审批节点
     *
     * @param id
     */
    private boolean isPassLxsp(Long id) {
        ProjectStepMenu stepMenu = this.projectStepMenuMapper.selectOne(Wrappers.<ProjectStepMenu>lambdaQuery()
                .eq(ProjectStepMenu::getCode, "lxsp")
        );
        if (stepMenu != null) {
            Integer count = this.projectStepMapper.selectCount(Wrappers.<ProjectStep>lambdaQuery()
                    .eq(ProjectStep::getProjectId, id)
                    .eq(ProjectStep::getStepMenuId, stepMenu.getId())
            );
            //已通过立项审批节点  需要推送到报销系统
            if (count > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Project changeClose(Project project) {
        Project projectDB = getById(project.getId());
        //String bValue = projectDB.getServiceStatus();
        //projectDB.setFollowTime(new Date());
        //projectDB.setServiceStatus(ProjectStatus.YI_ZHONG_ZHI.getCode());
        projectDB.setTerminationTime(new Date());
        projectDB.setTerminationReason(project.getTerminationReason());
        projectDB.setTerminationSponsor(project.getTerminationSponsor());
        projectDB.setTerminationDocument(project.getTerminationDocument());
        projectDB.setTerminationTime(project.getTerminationTime());
        super.updateById(projectDB);
//        if(StringUtils.isNotBlank(bValue)){
//            bValue = ProjectStatus.getByCode(bValue).getDesc();
//        }
        //operLogService.changeLog(project.getClass(), project.getId(), "变更项目为已终止状态", bValue, ProjectStatus.getByCode(project.getServiceStatus()).getDesc());
        return project;
    }

    @Override
    public Project changeServiceStatus(Long id, String serviceStatus) {
        Project project = getById(id);
        project.setServiceStatus(serviceStatus);
        updateById(project);
        return project;
    }

    @Override
    public void projectDaoQiStatusChange() {
        LambdaQueryWrapper<Project> wrapper = Wrappers.<Project>lambdaQuery()
                .le(Project::getServiceEndTime, new Date())
                .in(Project::getServiceStatus, Arrays.asList(ProjectStatus.ZAI_GUAN.getCode(),
                        ProjectStatus.YI_ZHONG_BIAO.getCode(),
                        ProjectStatus.YUN_YING_QI.getCode(),
                        ProjectStatus.TOU_BIAO_ZHONG.getCode(),
                        ProjectStatus.PING_SHEN_ZHONG.getCode(),
                        ProjectStatus.YUN_YING_QI.getCode(),
                        ProjectStatus.TUO_ZHAN_CHENG_GONG.getCode(),
                        ProjectStatus.WANG_CHENG_QIAN_YUE.getCode()));
        Project project = new Project();
        project.setServiceStatus(ProjectStatus.YI_DAO_QI.getCode());
        projectMapper.update(project, wrapper);
        projectDaoqiChange();
    }

    public void projectDaoqiChange() {
        try {
            List<ProjectDaoQiDto> daoQiDtoList = projectMapper.selectYdqList();
            if (CollectionUtils.isEmpty(daoQiDtoList))
                return;

            Map<Long, List<ProjectDaoQiDto>> mapResult = daoQiDtoList.stream().collect(Collectors.groupingBy(ProjectDaoQiDto::getId));
            for (Map.Entry<Long, List<ProjectDaoQiDto>> item : mapResult.entrySet()) {
                List<ProjectDaoQiDto> value = item.getValue();

                for (int i = 0; i < value.size(); i++) {
                    String serverStatus = value.get(i).getServiceStatus();
                    String projectType = value.get(i).getProjectType();

                    if (StringUtils.isEmpty(serverStatus) || StringUtils.isEmpty(projectType))
                        continue;

                    String status = JSONObject.parseObject(serverStatus).getString(projectType);

                    if (status == null)
                        continue;

                    if ("ZHONG_BIAO_ZHUANG_TAI".equals(status)) {
                        status = value.get(i).getBidedResult();
                    }

                    LambdaQueryWrapper<Project> wrapper = Wrappers.<Project>lambdaQuery().eq(Project::getId, value.get(i).getId());
                    Project project = new Project();
                    project.setServiceStatus(status);
                    project.setUpdateStatusDate(new Date());
                    projectMapper.update(project, wrapper);
                    break;
                }

            }
        } catch (Exception ex) {
            System.out.println("已到期状态还原异常：" + ex.getMessage());
        }
    }


    @Override
    public void projectCircularChange() {

        //获取通告记录表所有信息
        QueryWrapper<ProjectNotify> queryDataWrapper = new QueryWrapper<>();
        queryDataWrapper.eq("deleted", 0);
        List<ProjectNotify> projectNotifyList = projectNotifyMapper.selectList(queryDataWrapper);

        //符合要求得通告列表
        List<ProjectNotifyDto> projectNotifyDtoList = projectMapper.allNotify();

        generateProjectNotify(projectNotifyList, projectNotifyDtoList);
    }

    public void generateProjectNotify(List<ProjectNotify> projectNotifyList, List<ProjectNotifyDto> projectNotifyDtoList) {
        Map<Long, List<ProjectNotify>> mapprojectNotify = projectNotifyList.stream().collect(Collectors.groupingBy(ProjectNotify::getProjectId));
        Map<Long, List<ProjectNotifyDto>> mapProjectNotifyDto = projectNotifyDtoList.stream().collect(Collectors.groupingBy(ProjectNotifyDto::getId));

        for (int i = 0; i < projectNotifyList.size(); i++) {
            ProjectNotify item = projectNotifyList.get(i);
            if (item.getProjectId().equals(2987l)) {
                int ii = 1;
            }
            List<ProjectNotifyDto> notifyDtos = mapProjectNotifyDto.get(item.getProjectId());

            //删除操作
            if (CollectionUtils.isEmpty(notifyDtos)) {
                revokeProjectNotify(item);
                continue;
            }

            //删除操作
            List<ProjectNotifyDto> itemNotifyDtos = notifyDtos.stream().filter(m -> m.getNotifyType().equals(item.getNotifyType())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(itemNotifyDtos)) {
                revokeProjectNotify(item);
                continue;
            }

            updateProjectNotify(item, itemNotifyDtos.get(0));
        }

        for (int i = 0; i < projectNotifyDtoList.size(); i++) {
            ProjectNotifyDto item = projectNotifyDtoList.get(i);
            List<ProjectNotify> notifys = mapprojectNotify.get(item.getId());
            //新增操作
            if (CollectionUtils.isEmpty(notifys)) {
                addProjectNotify(item);
                continue;
            }
            //新增操作
            List<ProjectNotify> itemNotifyDtos = notifys.stream().filter(m -> m.getNotifyType().equals(item.getNotifyType())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(itemNotifyDtos)) {
                addProjectNotify(item);
            }
        }
    }

    public void revokeProjectNotify(ProjectNotify projectNotify) {
        Long projectId = 0L;
        try {
            projectId = projectNotify.getProjectId();
            if ("XIANG_MU_WEI_JIN_XING_BIAO_SHU_HE_GUI_PING_SHEN".equals(projectNotify.getNotifyType())) {

                Project project = this.getById(projectNotify.getProjectId());
                //当前时间要比开标时间大3天，才会进入通报；否者撤销通报；
                if (DateUtils.addDays(project.getBidingOpentime(), 3).getTime() >= new Date().getTime()) {
                    projectNotify.setDeleted(1);
                }
            } else {
                projectNotify.setDeleted(1);
            }
            projectNotifyMapper.updateById(projectNotify);
        } catch (Exception exception) {
            System.out.println("生成项目id" + projectId + "异常");
        }
    }

    public void addProjectNotify(ProjectNotifyDto projectNotify) {
        ProjectNotify item = new ProjectNotify();
        item.setDeptId(projectNotify.getDeptId());
        item.setDeptName(projectNotify.getDeptName());
        item.setProjectId(projectNotify.getId());
        item.setProjectName(projectNotify.getProjectName());
        item.setNotifyType(projectNotify.getNotifyType());
        item.setNotifyTypeName(projectNotify.getNotifyTypeName());
        item.setAttributorUserId(projectNotify.getAttributorUserId());
        item.setAttributorUserName(projectNotify.getAttributorUserName());
        item.setOverdueTime(projectNotify.getOverdueTime());
        item.setPushTime(projectNotify.getPushTime());

        projectNotifyMapper.insert(item);
    }

    public void updateProjectNotify(ProjectNotify item, ProjectNotifyDto projectNotify) {

        boolean isUpdate = false;
        if (projectNotify.getDeptId() != null && !projectNotify.getDeptId().equals(item.getDeptId())) {
            isUpdate = true;
        }
        if (projectNotify.getDeptName() != null && !projectNotify.getDeptName().equals(item.getDeptName())) {
            isUpdate = true;
        }
        if (projectNotify.getId() != null && !projectNotify.getId().equals(item.getProjectId())) {
            isUpdate = true;
        }
        if (projectNotify.getProjectName() != null && !projectNotify.getProjectName().equals(item.getProjectName())) {
            isUpdate = true;
        }
        if (projectNotify.getNotifyType() != null && !projectNotify.getNotifyType().equals(item.getNotifyType())) {
            isUpdate = true;
        }
        if (projectNotify.getNotifyTypeName() != null && !projectNotify.getNotifyTypeName().equals(item.getNotifyTypeName())) {
            isUpdate = true;
        }
        if (projectNotify.getAttributorUserId() != null && !projectNotify.getAttributorUserId().equals(item.getAttributorUserId())) {
            isUpdate = true;
        }
        if (projectNotify.getAttributorUserName() != null && !projectNotify.getAttributorUserName().equals(item.getAttributorUserName())) {
            isUpdate = true;
        }
        if (projectNotify.getOverdueTime() != null && !projectNotify.getOverdueTime().equals(item.getOverdueTime())) {
            isUpdate = true;
        }
        if (projectNotify.getPushTime() != null && !projectNotify.getPushTime().equals(item.getPushTime())) {
            isUpdate = true;
        }

        item.setDeptId(projectNotify.getDeptId());
        item.setDeptName(projectNotify.getDeptName());
        item.setProjectId(projectNotify.getId());
        item.setProjectName(projectNotify.getProjectName());
        item.setNotifyType(projectNotify.getNotifyType());
        item.setNotifyTypeName(projectNotify.getNotifyTypeName());
        item.setAttributorUserId(projectNotify.getAttributorUserId());
        item.setAttributorUserName(projectNotify.getAttributorUserName());
        item.setOverdueTime(projectNotify.getOverdueTime());
        item.setPushTime(projectNotify.getPushTime());

        if (isUpdate)
            projectNotifyMapper.updateById(item);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncProjectCompany() {
        LambdaQueryWrapper<Project> wrapper = Wrappers.<Project>lambdaQuery().eq(Project::getSyncCompanyProcess, false).eq(Project::getProjectType, ProjectConstant.GU_QUAN_HE_ZUO_XIANG_MU)
                .eq(Project::getServiceStatus, ProjectStatus.ZAI_GUAN.getCode());
        List<Project> list = this.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(v -> {
            List<ProjectRiskInspection> riskInspections = riskInspectionService.list(Wrappers.<ProjectRiskInspection>lambdaQuery().eq(ProjectRiskInspection::getProjectId, v.getId()));
            projectCompanyService.syncProjectData(v, riskInspections);
            v.setSyncCompanyProcess(true);
            projectMapper.updateById(v);
        });
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Project> clone(Project project) {
        project = this.getById(project.getId());
        //R<Project> projectUpda = projectService.update(project);
        R<Project> projectAdd = this.add(this.cloneBasicInformation(project));
        return projectAdd;
    }

    //复制基本信息
    public Project cloneBasicInformation(Project project) {
        Project projectCopy = new Project();
        projectCopy.setSourceName(project.getProjectName() + "项目合同变更");
        projectCopy.setServiceStatus("LI_XIANG_ZHONG");
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
        if (!"GU_QUAN_HE_ZUO_XIANG_MU".equals(project.getProjectType())) {
            projectCopy.setBusinessType(project.getBusinessType());
            projectCopy.setConstructionArea(project.getConstructionArea());
            projectCopy.setInStock(project.getInStock());
            projectCopy.setServiceContent(project.getServiceContent());
            //单一投标项目 额外招标信息
            if ("DAN_YI_TOU_BIAO_XIANG_MU".equals(project.getProjectType())) {
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
        } else {
            //股权合作项目
            projectCopy.setCooperationType(project.getCooperationType());
            projectCopy.setTargetBusinessType(project.getTargetBusinessType());
            projectCopy.setTargetCompanyName(project.getTargetCompanyName());
            projectCopy.setTargetCompanyNo(project.getTargetCompanyNo());
            projectCopy.setTargetCompanyType(project.getTargetCompanyType());
            projectCopy.setTargetIncorporationTime(project.getTargetIncorporationTime());
            projectCopy.setTargetPersonnel(project.getTargetPersonnel());
            projectCopy.setTargetLegalRepresentative(project.getTargetLegalRepresentative());
            projectCopy.setTargetRegisteredCapital(project.getTargetRegisteredCapital());
        }
        return projectCopy;
    }

    @Override
    public List<SelectCommonVo> getProjectStepMenuList() {
        return projectMapper.getProjectStepMenuList();
    }

    @Override
    public List<ProjectOpenMarkExportVo> getOpenMarkList(QueryPage queryPage) {

        Integer year = queryPage.getYear();
        if (ObjectUtil.isNotEmpty(year)) {
            int currentYear = Year.now().getValue();
            boolean isCurrentYear = false;
            if (year == currentYear) {
                isCurrentYear = true;
            }
            queryPage.getDbParams().put("isCurrentYear", isCurrentYear);
            queryPage.getDbParams().put("currYear", year);
        }

        List<ProjectOpenMarkExportVo> result = projectMapper.getOpenMarkList(queryPage.getDbParams());
        if (CollectionUtils.isEmpty(result))
            return Lists.newArrayList();

        List<ProjectListStepVo> projectListStepVos = projectMapper.getProjectListStep(result.stream().map(ProjectOpenMarkExportVo::getId).collect(Collectors.toList()));
        Map<Long, List<ProjectListStepVo>> mapStepList = projectListStepVos.stream().collect(Collectors.groupingBy(ProjectListStepVo::getProjectId));

        result.forEach(o -> {
            if ("SHI".equals(o.getInStock()))
                o.setInStock("是");
            else
                o.setInStock("否");

            List<ProjectListStepVo> itemSteps = mapStepList.get(o.getId());
            if (!CollectionUtils.isEmpty(itemSteps)) {
                o.setFinishStep(itemSteps.get(0).getStepName());
            }

            //是否已完成投标复盘节点
            if (o.getIsPassTbfp() == 1) {
                if ("YI_ZHONG_BIAO".equals(o.getBidedResult())) {
                    o.setBidedResult("已中标");

                    o.setNotSelectedReason("");
                }
                if ("WEI_ZHONG_BIAO".equals(o.getBidedResult())) {
                    o.setBidedResult("未中标");

                    if ("TOU_BIAO_YOU_XIAO_DAN_PAI_MING_KAO_HOU".equals(o.getNotSelectedReason())) {
                        o.setNotSelectedReason("投标有效但排名靠后");
                    }
                    if ("WEI_CAN_YU_TOU_BIAO".equals(o.getNotSelectedReason())) {
                        o.setNotSelectedReason("未参与投标");
                    }
                    if ("TOU_BIAO_WU_XIAO".equals(o.getNotSelectedReason())) {
                        o.setNotSelectedReason("投标无效");
                    }
                }

            } else {
                o.setBidedResult("");
                o.setNotSelectedReason("");
            }
        });

        return result;
    }

    @Override
    @DataFill
    public IPage<Project> pageAll(QueryPage queryPage) {
        IPage<Project> projectIPage = projectMapper.listAll(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());

        return projectIPage;
    }

    /**
     * 项目相似度匹配  有相似 = true 无 = false
     */
    @DataFill
    @Override
    public R<List<Project>> matchProjectSimilarity(Project project) {
        if (StringUtils.isEmpty(project.getProjectName()) || StringUtils.isEmpty(project.getBusinessType())) {
            return R.ok();
        }
        LambdaQueryWrapper<Project> warp = new LambdaQueryWrapper<Project>()
                .eq(Project::getProvinceCode, project.getProvinceCode())
                .eq(Project::getCityCode, project.getCityCode());
        if (null != project.getProjectNo()) {
            warp.ne(Project::getProjectNo, project.getProjectNo());
        }
        //查询输入省市区下的所有项目
        List<Project> projects = this.list(warp);

        if (projects.isEmpty()) {
            return R.ok();
        }
        List<Project> simiList = new ArrayList<>();
        for (Project oldProj : projects) {
            //名称相似度 >= 80%
            if (StringUtils.strSimilarityByLevenshtein(oldProj.getProjectName(), project.getProjectName(), 80f)) {
                oldProj.setSimilarity("名称相似度 大于等于 80%");
                simiList.add(oldProj);
                continue;
            }
            //名称相似度 >= 50%  且  业态相同
            if (null != oldProj.getBusinessType() && oldProj.getBidingOpentime() != null) {
                if (
                        StringUtils.strSimilarityByLevenshtein(oldProj.getProjectName(), project.getProjectName(), 50f)
                                && oldProj.getBusinessType().equals(project.getBusinessType())
                                && oldProj.getBidingOpentime().equals(project.getBidingOpentime())
                ) {
                    oldProj.setSimilarity("名称相似度 大于等于 50% 且 业态相同 且开标时间相同");
                    simiList.add(oldProj);
                }
            }
        }

        if (!simiList.isEmpty()) {
            return R.ok(simiList);
        }

        return R.ok();
    }


    @Override
    public BigDecimal getCalculateAnnualConversion(Project project) {
        //增量
        if (ShiFou.SHI.getCode().equals(project.getIsIncrement())) {
            return this.calculateAnnualConversion(
                    project.getContractAmounts(),
                    project.getProposedServicePeriod(),
                    project.getServiceBeginTime(),
                    project.getServiceEndTime(),
                    project.getPerformanceConfirmTime()
            );
        }
        //非增量
        return this.calculateAnnualConversion(
                project.getContractAmount(),
                project.getProposedServicePeriod(),
                project.getServiceBeginTime(),
                project.getServiceEndTime(),
                project.getPerformanceConfirmTime()
        );
    }

    // 计算转化金额
    @Override
    public BigDecimal calculateAnnualConversion(BigDecimal contractAmount, Integer servicePeriod, Date serviceBeginTime, Date serviceEndTime, Date performanceConfirmTime) {

        if (contractAmount == null || servicePeriod == null) {
            return BigDecimal.ZERO;
        }

        if (performanceConfirmTime == null) {
            performanceConfirmTime = new Date();
        }

        /**
         * 公式  金额 / 拟服务日期 *  服务开始日期到年底所剩月份
         */
        BigDecimal divide = contractAmount.divide(new BigDecimal(servicePeriod), 4, BigDecimal.ROUND_CEILING);

        Integer month = DateUtils.getCoveredMonths(serviceBeginTime, serviceEndTime, performanceConfirmTime);


        BigDecimal total = divide.multiply(new BigDecimal(month));

        total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
        if (total.compareTo(contractAmount) > 0) {
            total = contractAmount;
        }

        return total;
    }

}
