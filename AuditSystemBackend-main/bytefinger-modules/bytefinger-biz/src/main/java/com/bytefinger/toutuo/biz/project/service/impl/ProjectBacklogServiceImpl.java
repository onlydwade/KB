package com.bytefinger.toutuo.biz.project.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.DictUtils;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.api.system.core.domain.SysDictData;
import com.bytefinger.toutuo.api.system.core.domain.SysDictType;
import com.bytefinger.toutuo.biz.performance.service.IBudgetInService;
import com.bytefinger.toutuo.biz.project.domain.*;
import com.bytefinger.toutuo.biz.project.mapper.ProjectBacklogFileMapper;
import com.bytefinger.toutuo.biz.project.mapper.ProjectBacklogMapper;
import com.bytefinger.toutuo.biz.project.mapper.ProjectExpandMapper;
import com.bytefinger.toutuo.biz.project.service.*;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocument;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentService;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStep;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenu;
import com.bytefinger.toutuo.biz.projectstep.enums.ProjectApprovalStatus;
import com.bytefinger.toutuo.biz.projectstep.mapper.ProjectStepMenuMapper;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepService;
import com.bytefinger.toutuo.biz.workbrief.enums.ApproveStatusEnum;
import com.bytefinger.toutuo.common.service.BizService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectBacklogServiceImpl extends BizService<ProjectBacklogMapper, ProjectBacklog> implements IProjectBacklogService {

    @Autowired
    private ProjectBacklogMapper projectBacklogMapper;

    private final ProjectBacklogFileMapper fileMapper;

    private final IProjectBacklogAchievementService backlogAchievementService;

    private final IProjectAchievementService projectAchievementService;

    private final IProjectService projectService;

    private final ProjectStepMenuMapper projectStepMenuMapper;

    private final IProjectStepService projectStepService;

    private final IBudgetInService budgetInService;

    private final IAuditDocumentService projectDocumentService;

    private final ProjectExpandMapper projectExpandMapper;

    @DataFill
    @Override
    public IPage<ProjectBacklog> projectBacklogPage(QueryPage queryPage) {
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

        QueryWrapper<ProjectBacklog> wrapper = queryPage.getWrapper();

        IPage<ProjectBacklog> page = projectBacklogMapper.list(queryPage.toPage(), wrapper, queryPage.getDbParams());
        Boolean isAdministrator = isAdministrators();
        page.getRecords().forEach(projectBacklog -> {
            projectBacklog.setBackfillStatusStr(ApproveStatusEnum.getEnumName(projectBacklog.getBackfillStatus()));
            projectBacklog.setIsAdministrators(isAdministrator);
        });

        setYtProjetList(page.getRecords());

        return page;

    }

    @Override
    public R<Object> add(ProjectBacklog projectBacklog) {
        return saveInfo(projectBacklog, false);
    }

    @Override
    public R<Object> submit(ProjectBacklog projectBacklog) {
        return saveInfo(projectBacklog, true);
    }

    public R<Object> saveInfo(ProjectBacklog projectBacklog, boolean isSave) {
        projectBacklog.setFollowTime(new Date());

        if (ObjectUtil.isEmpty(projectBacklog.getId())) {
            projectBacklog.setProjectNo(super.getNo());
            if(isSave){
                //提交  审批待确认
                projectBacklog.setBackfillStatus(ApproveStatusEnum.APPROVAL_DAI_QUE_REN.getCode());
            }else {
                //暂存  未发起
                projectBacklog.setBackfillStatus(ApproveStatusEnum.UNAPPROVED.getCode());
            }
            projectBacklog.setValidityStatus("SHI");
            projectBacklog = super.add4Log(projectBacklog);
        }else {
            super.update4Log(projectBacklog);
        }

        ProjectBacklog finalProjectBacklog = projectBacklog;

        if(projectBacklog.getProjectBacklogAchievement() != null){
            projectBacklog.getProjectBacklogAchievement().forEach(achievement->{
                if(achievement.getId() != null){
                    if(achievement.getDeleted() == 1){
                        this.backlogAchievementService.removeById(achievement.getId());
                    }else {
                        this.backlogAchievementService.updateById(achievement);
                    }
                }else {
                    achievement.setProjectId(finalProjectBacklog.getId());
                    this.backlogAchievementService.save(achievement);
                }
            });
        }

        //附件
        if(projectBacklog.getProjectBacklogFiles() != null){

            //查询已有的附件做删除对比
            List<ProjectBacklogFile> existList = this.fileMapper.selectList(
                    new LambdaQueryWrapper<ProjectBacklogFile>()
                            .eq(ProjectBacklogFile::getProjectId, finalProjectBacklog.getId())
                            .eq(ProjectBacklogFile::getDeleted, 0)
            );
            List<Long> fileIds = finalProjectBacklog.getProjectBacklogFiles().stream().map(file -> file.getId()).collect(Collectors.toList());
            List<Long> deletedIds = new ArrayList<>();
            existList.forEach(existFile->{
                if(!fileIds.contains(existFile.getId())) {
                    deletedIds.add(existFile.getId());
                }
            });
            if(!deletedIds.isEmpty()){
                this.fileMapper.deleteBatchIds(deletedIds);
            }

            projectBacklog.getProjectBacklogFiles().forEach(file-> {
                if(file.getId() == null) {
                    file.setProjectId(finalProjectBacklog.getId());
                    file.setDeleted(0);
                    this.fileMapper.insert(file);
                }
            });
        }


        //是提交  需要判断是否线上/线下  直接完成oa审批通过后的逻辑
        if(isSave){
            //走线下
            if(this.getCheckOa() == 0){
                this.oaApprovalPass(projectBacklog.getId(), ApproveStatusEnum.APPROVAL_REJECTION_XIAN_XIA.getCode());
            }
        }

        return R.ok(projectBacklog);
    }

    @Override
    public void oaApprovalPass(Long projectId,Integer approveStatus){
        //修改项目补录审批状态
        ProjectBacklog projectBacklog = this.getById(projectId);
        projectBacklog.setBackfillStatus(approveStatus);
        this.updateById(projectBacklog);

        //审批通过 或  线下审批通过
        if(approveStatus == 2 || approveStatus == 100){
            //复制数据到project表
            Project project = new Project();
            BeanUtils.copyProperties(projectBacklog,project);
            project.setExpire("SHI".equals(projectBacklog.getValidityStatus()) ? "YOU_XIAO" : "YI_SHI_XIAO");
            project.setServiceStatus("ZAI_GUAN");
            project.setPerformanceConfirmTime(new Date());
            project.setUpdateStatusDate(new Date());
            project.setBidedResult("YI_ZHONG_BIAO");
            this.projectService.add2(project);
            //线上审批单独更新一次创建人id
            if(approveStatus == 2){
                project.setCreateUserId(projectBacklog.getCreateUserId());
                this.projectService.update(project);
            }

            //保存乙方单位名称
            ProjectExpand projectExpand = getProjectExpandById(project.getId());
            if (projectExpand == null) {
                projectExpand = new ProjectExpand();
            }
            projectExpand.setProjectId(project.getId());
            projectExpand.setSecondParty(projectBacklog.getSecondParty());
            if (projectExpand.getId() == null) {
                projectExpandMapper.insert(projectExpand);
            } else {
                //只修改结项节点的字段
                projectExpandMapper.updateById(projectExpand);
            }

            //补录审批完成之后 biz_project_step表，添加相关的节点信息，并且完成
            List<ProjectStepMenu> list = projectStepMenuMapper.listbyProjectType(projectBacklog.getProjectType());
            List<ProjectStepMenu> level2List = list.stream().filter(projectStepMenu -> projectStepMenu.getLevel() == 2).collect(Collectors.toList());
            level2List.forEach(step->{
                ProjectStep projectStep = new ProjectStep();
                projectStep.setProjectId(project.getId());
                projectStep.setApprovalStatus(ProjectApprovalStatus.XIAN_XIA_SHEN_PI_TONG_GUO.getCode());
                projectStep.setStatus(1);
                projectStep.setStepMenuId(step.getId());
                projectStep.setHandleTime(new Date());
                projectStepService.save(projectStep);
            });

            //复制附件到项目表
            List<ProjectBacklogFile> projectBacklogFiles = this.fileMapper.selectList(
                    new LambdaQueryWrapper<ProjectBacklogFile>()
                            .eq(ProjectBacklogFile::getProjectId, projectId)
                            .eq(ProjectBacklogFile::getDeleted, 0)
            );
            projectBacklogFiles.forEach(file -> {
                AuditDocument projectDocument = new AuditDocument();
                BeanUtils.copyProperties(file,projectDocument);
                projectDocument.setProjectId(project.getId());
                projectDocument.setDocmentObject(file.getDocumentObject());
                projectDocument.setStepMenuId(18L);//业绩确认节点
                projectDocumentService.save(projectDocument);
            });

            List<ProjectBacklogAchievement> backlogAchievementList = this.backlogAchievementService.list(projectId);
            backlogAchievementList.forEach(backlogAchievement->{
                ProjectAchievement projectAchievement = new ProjectAchievement();
                BeanUtils.copyProperties(backlogAchievement,projectAchievement);
                projectAchievement.setProjectId(project.getId());
                projectAchievementService.save(projectAchievement);
            });

            //补录审批完成之后 是否计算业绩为是：biz_budget_performance_allocation_data，
            if("SHI".equals(projectBacklog.getIsCountPerformance())){
                budgetInService.calcPerformanceAllocationData(project.getId());
            }

        }

    }

    public ProjectExpand getProjectExpandById(Long projectId) {
        QueryWrapper<ProjectExpand> queryDataWrapper = new QueryWrapper<>();
        queryDataWrapper.in("project_id", projectId);
        List<ProjectExpand> projectExpandList = projectExpandMapper.selectList(queryDataWrapper);
        if (CollectionUtils.isEmpty(projectExpandList))
            return null;
        return projectExpandList.get(0);
    }

    @Override
    public R<Object> update(ProjectBacklog projectBacklog) {

        projectBacklog.setFollowTime(new Date());
        ProjectBacklog resultProject = super.update4Log(projectBacklog);

        if(projectBacklog.getProjectBacklogAchievement() != null){
            projectBacklog.getProjectBacklogAchievement().forEach(achievement->{
                if(achievement.getId() != null){
                    if(achievement.getDeleted() == 1){
                        this.backlogAchievementService.removeById(achievement.getId());
                    }else {
                        this.backlogAchievementService.updateById(achievement);
                    }
                }else {
                    achievement.setProjectId(resultProject.getId());
                    this.backlogAchievementService.save(achievement);
                }
            });
            backlogAchievementService.updateBatchById(projectBacklog.getProjectBacklogAchievement());
        }

        //附件
        if(projectBacklog.getProjectBacklogFiles() != null){
            projectBacklog.getProjectBacklogFiles().forEach(file-> {
                if(file.getId() != null) {
                    this.fileMapper.updateById(file);
                }else {
                    file.setProjectId(resultProject.getId());
                    this.fileMapper.insert(file);
                }
            });
        }

        return R.ok(resultProject);
    }

    @Override
    public Integer getCheckOa() {
        return this.baseMapper.getGroupConfig();
    }

    @Override
    public void setCheckOa(Integer checkValue) {
        this.baseMapper.setGroupConfig(checkValue);
    }

    @Override
    public Boolean isAdministrators(){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String postCodes = this.baseMapper.getPostCode(loginUser.getPostId());
        if (StringUtils.isEmpty(postCodes))
        {
            return false;
        }
        if ("CHAO_JI_GUAN_LI_YUAN".equals(postCodes)){
            return true;
        }
        return false;
    }

    @Override
    public void setYtProjetList(List<ProjectBacklog> projectList) {

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
    }

    @Override
    public BigDecimal calculateAnnualConversion(BigDecimal contractAmount, Integer servicePeriod, Date serviceBeginTime, Date serviceEndTime, Date performanceConfirmTime) {
        if (contractAmount == null || servicePeriod == null) {
            return BigDecimal.ZERO;
        }

        /**
         * 公式  金额 / 拟服务日期 *  服务开始日期到年底所剩月份
         */
        BigDecimal divide = contractAmount.divide(new BigDecimal(servicePeriod), 4, BigDecimal.ROUND_CEILING);

        int month = DateUtils.getCoveredMonths(serviceBeginTime,serviceEndTime,performanceConfirmTime);

        BigDecimal total = divide.multiply(new BigDecimal(month));

        total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
        if (total.compareTo(contractAmount) > 0) {
            total = contractAmount;
        }

        return total;
    }
}
