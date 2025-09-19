package com.bytefinger.toutuo.biz.project.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.enums.ShiFou;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.utils.bean.BeanUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.api.system.file.domain.SysFile;
import com.bytefinger.toutuo.biz.oa.domain.OaApproval;
import com.bytefinger.toutuo.biz.oa.service.IOaApprovalService;
import com.bytefinger.toutuo.biz.performance.domain.BudgetPerformanceAllocationData;
import com.bytefinger.toutuo.biz.performance.domain.vo.ProjectOpenMarkExportVo;
import com.bytefinger.toutuo.biz.performance.mapper.BudgetPerformanceAllocationDataMapper;
import com.bytefinger.toutuo.biz.performance.service.IBudgetInService;
import com.bytefinger.toutuo.biz.project.bo.ProjectAchievementBO;
import com.bytefinger.toutuo.biz.project.constants.ProjectConstant;
import com.bytefinger.toutuo.biz.project.domain.*;
import com.bytefinger.toutuo.biz.project.dto.ProjectNotifyDto;
import com.bytefinger.toutuo.biz.project.dto.EndProjectNotifyDto;
import com.bytefinger.toutuo.biz.project.mapper.ProjectMapper;
import com.bytefinger.toutuo.biz.project.mapper.ProjectRecodingBakMapper;
import com.bytefinger.toutuo.biz.project.service.*;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.mapper.ProjectCompanyMapper;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocument;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocumentTemplate;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentService;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentTemplateService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtension;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionService;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStep;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepService;
import com.bytefinger.toutuo.biz.user.mapper.UserMapper;
import com.bytefinger.toutuo.biz.user.service.ISysDeptService;
import com.bytefinger.toutuo.biz.workbrief.enums.ApproveStatusEnum;
import com.bytefinger.toutuo.common.util.ExcelUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-06
 */
@Slf4j
@Api(tags = "项目")
@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {

    @Autowired(required = false)
    private IProjectExtensionService projectExtensionService;

    public final IProjectService projectService;

    public final IProjectStepService projectStepService;

    public final UserMapper userMapper;

    public final ISysDeptService sysDeptService;

    private final IProjectBidService projectBidService;

    private final IAuditDocumentService projectDocumentService;

    private final IAuditDocumentTemplateService projectDocumentTemplateService;

    private final IProjectEstimatedCostService projectEstimatedCostService;

    private final ProjectRecodingBakMapper projectRecodingBakMapper;

    private final IProjectCompanyLogService projectCompanyLogService;

    private final long StepMenuId = 18L;
    private final String OperName = "合同（盖章版）扫描件上传";

    private final BudgetPerformanceAllocationDataMapper budgetPerformanceAllocationDataMapper;
    private final IBudgetInService budgetInYearService;

    private final ProjectMapper projectMapper;
    private final ProjectCompanyMapper projectCompanyMapper;
    private final IOaApprovalService oaApprovalService;

    /**
     * 下载服务器主机（域名）
     */
    @Value("${downLoad.host}")
    private String downLoadHost;

    @ApiOperation(value = "新增-项目")
    @PutMapping("/add")
    @RequiresPermissions("biz:project:add")
    public AjaxResult add(@RequestBody Project project) {
        //判断是否为股权项目，如果是股权项目则设置续签默认为否
        if ("GU_QUAN_HE_ZUO_XIANG_MU".equals(project.getProjectType())) {
            project.setInStock(ShiFou.FOU.getCode());
        }
        return projectService.add(project).toAjaxResult();
    }

    @ApiOperation(value = "修改-项目")
    @PutMapping("/update")
    @RequiresPermissions("biz:project:update")
    public AjaxResult update(@RequestBody Project project) {
//        if(!ObjectUtils.isEmpty(project.getContractAmount())){
//            project.setPerformanceConfirmTime(new Date());
//        }
        R<Project> projectR = projectService.update(project);
        return projectR.toAjaxResult();
    }


    //    @ApiOperation(value = "修改-项目结项信息")
//    @PutMapping("/saveClosureInfo")
//    @RequiresPermissions("biz:project:update")
//    public AjaxResult saveClosureInfo(@RequestBody SaveClosureInfoDto saveClosureInfoDto) {
//        return projectService.saveClosureInfo(saveClosureInfoDto).toAjaxResult();
//    }

    @ApiOperation(value = "获取-项目结项信息")
    @GetMapping("/getClosureInfo/{id}")
    @RequiresPermissions("biz:project:get")
    public AjaxResult getClosureInfo(@PathVariable("id") Long id) {
        return success(projectService.getClosureInfo(id));
    }


    @ApiOperation(value = "动态计算年度转化金额")
    @PostMapping("/getAnnualConversionAmountCalculate")
    @RequiresPermissions("biz:project:get")
    public AjaxResult getAnnualConversionAmountCalculate(@RequestBody Project project) {
        // 检查项目类型
        if (ProjectConstant.GU_QUAN_HE_ZUO_XIANG_MU.equals(project.getProjectType())) {
            return success(project);
        }
        return success(this.getAnnualConversionAmountCalculate(project));
    }

    @ApiOperation(value = "动态计算年度转化金额-新")
    @PostMapping("/annualConversionAmountCalculateNew")
    @RequiresPermissions("biz:project:get")
    public AjaxResult annualConversionAmountCalculateNew(@RequestBody Project project) {

        // 检查项目类型
        if (ProjectConstant.GU_QUAN_HE_ZUO_XIANG_MU.equals(project.getProjectType())) {
            return success(project);
        }

        String isIncrement = project.getIsIncrement();
        BigDecimal annualConversionAmount;

        //增量取增量合同金额  非增量取合同总金额
        if (ShiFou.SHI.getCode().equals(isIncrement)) {
            annualConversionAmount = this.projectService.calculateAnnualConversion(
                    project.getContractAmounts(),
                    project.getProposedServicePeriod(),
                    project.getServiceBeginTime(),
                    project.getServiceEndTime(),
                    project.getPerformanceConfirmTime()
            );
            project.setAnnualConversionAmounts(annualConversionAmount);
        } else {
            annualConversionAmount = this.projectService.calculateAnnualConversion(
                    project.getContractAmount(),
                    project.getProposedServicePeriod(),
                    project.getServiceBeginTime(),
                    project.getServiceEndTime(),
                    project.getPerformanceConfirmTime()
            );
            project.setAnnualConversionAmount(annualConversionAmount);
        }

        // 更新数据库和计算业绩
        projectService.update(project);
        budgetInYearService.calcPerformanceAllocationData(project.getId());

        if (!ShiFou.SHI.getCode().equals(isIncrement)) {
            // 仅非增量时更新业绩分配数据
            budgetPerformanceAllocationDataMapper.update(
                    new BudgetPerformanceAllocationData(),
                    new LambdaUpdateWrapper<BudgetPerformanceAllocationData>()
                            .eq(BudgetPerformanceAllocationData::getProjectId, project.getId())
                            .eq(BudgetPerformanceAllocationData::getDeleted, 0)
                            .set(BudgetPerformanceAllocationData::getUpdateTime, new Date())
                            .set(BudgetPerformanceAllocationData::getUpdateUserId, SecurityUtils.getUserId())
                            .set(BudgetPerformanceAllocationData::getCurrentConvertIncome, annualConversionAmount)
            );
        }

        return success(annualConversionAmount);
    }

    @ApiOperation(value = "详情-项目基础资料")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:project:get")
    public AjaxResult get(@PathVariable("id") Long id) {

        Project project = projectService.getById(id);

        project = projectService.getYtProjet(project);
        project = projectService.getProjectExpand(project);
        //设置项目评审重点关注项目选项中文返回，后续可以增加其他枚举Code转中文返回
        project = projectService.getEnumCodeToStr(project, Lists.newArrayList("ZHONG_DIAN_GUAN_ZHU_XIANG_MU", "SHI_FOU"));
        if (StringUtils.isNotBlank(project.getStampCategory())) {
            project.setStampCategoryStr("加盖公司公章、法人章");
        }

        QueryWrapper<ProjectStep> queryDataWrapper = new QueryWrapper<>();
        queryDataWrapper.in("project_id", id);
        queryDataWrapper.in("step_menu_id", 12);
        queryDataWrapper.in("status", 1);
        List<ProjectStep> xmpsStepList = projectStepService.list(queryDataWrapper);
        project.setIsXmps(false);
        if (!CollectionUtils.isEmpty(xmpsStepList)) {
            project.setIsXmps(true);
        }

        QueryWrapper<ProjectEstimatedCost> queryEsDataWrapper = new QueryWrapper<>();
        queryEsDataWrapper.in("project_id", id);
        List<ProjectEstimatedCost> projectEstimatedCostList = projectEstimatedCostService.list(queryEsDataWrapper);
        if (!CollectionUtils.isEmpty(projectEstimatedCostList)) {
            project.setProjectEstimatedCosts(projectEstimatedCostList);
        }
        List<Integer> projectIdList = Lists.newArrayList();
        projectIdList.add(1);
        projectIdList.add(2);
        ProjectExtension projectExtension = projectExtensionService.getOne(Wrappers.<ProjectExtension>lambdaQuery()
                .eq(ProjectExtension::getRelevanceProjectId, id).in(ProjectExtension::getProcessMode, projectIdList));
        if (Objects.nonNull(projectExtension)) {
            project.setRenewBid(true);
        } else {
            project.setRenewBid(false);
        }

        project.setStampCategory("companyStamp");
        if (ProjectConstant.GU_QUAN_HE_ZUO_XIANG_MU.equals(project.getProjectType())) {
            return success(project);
        }
        if (null == project.getContractAmount() || null == project.getProposedServicePeriod() ) {
            return success(project);
        }
        project.setAnnualConversionAmount(new BigDecimal(0.00));
        if (project.getProposedServicePeriod().equals(0)){
            return success(project);
        }

        //增量取增量合同金额  非增量取合同总金额
        BigDecimal annualConversionAmount;
        if (ShiFou.SHI.getCode().equals(project.getIsIncrement())) {
            annualConversionAmount = this.projectService.calculateAnnualConversion(
                    project.getContractAmounts(),
                    project.getProposedServicePeriod(),
                    project.getServiceBeginTime(),
                    project.getServiceEndTime(),
                    project.getPerformanceConfirmTime()
            );
            project.setAnnualConversionAmounts(annualConversionAmount);
        } else {
            annualConversionAmount = this.projectService.calculateAnnualConversion(
                    project.getContractAmount(),
                    project.getProposedServicePeriod(),
                    project.getServiceBeginTime(),
                    project.getServiceEndTime(),
                    project.getPerformanceConfirmTime()
            );
            project.setAnnualConversionAmount(annualConversionAmount);
        }

        return success(project);

        //return success(projectService.getProjectInfoById(id));
    }

    @ApiOperation(value = "项目-录入最终报价")
    @PutMapping("/changeBidingAmount/{projectId}/{bidingAmount}")
    @RequiresPermissions("biz:project:changeBidingAmount")
    public AjaxResult changeBidingAmount(@PathVariable("projectId") Long projectId, @PathVariable("bidingAmount") String bidingAmount) {
        Project project = projectService.changeBidingAmount(projectId, bidingAmount);
        //我方竞标记录入库
        ProjectBid projectBid = projectBidService.getOne(Wrappers.<ProjectBid>lambdaQuery()
                .eq(ProjectBid::getProjectId, projectId)
                .eq(ProjectBid::getType, ProjectConstant.PROJECT_BID_SELF));
        if (ObjectUtils.isEmpty(projectBid)) {
            projectBid = new ProjectBid();
        }
        projectBid.setProjectId(projectId);
        projectBid.setCompany(project.getBidPartInCompany());
        projectBid.setBidingAmount(bidingAmount);
        projectBid.setType(ProjectConstant.PROJECT_BID_SELF);
        projectBidService.saveOrUpdate(projectBid);
        return AjaxResult.success(project);
    }

    @ApiOperation(value = "项目-标记失效")
    @PutMapping("/changeExpire")
    @RequiresPermissions("biz:project:changeExpire")
    public AjaxResult changeExpire(@RequestBody Project project) {
        AjaxResult success = AjaxResult.success(projectService.changeExpire(project.getId(), project.getExpireReason()));
        return success;
    }

    @ApiOperation(value = "项目-标记有效")
    @PutMapping("/changeValid")
    @RequiresPermissions("biz:project:changeValid")
    public AjaxResult changeValid(@RequestBody Project project) {
        projectService.updateProjectValid(project.getId());
        return AjaxResult.success();
    }

    @ApiOperation(value = "项目-项目终止")
    @PutMapping("/changeClose")
    @RequiresPermissions("biz:project:changeClose")
    public AjaxResult changeClose(@RequestBody Project project) {
        AjaxResult success = AjaxResult.success(projectService.changeClose(project));
        return success;
    }

    @ApiOperation(value = "项目-变更归属人")
    @PutMapping("/changeAttributor/{projectId}/{dispatchUserId}")
    @RequiresPermissions("biz:project:changeAttributor")
    public AjaxResult changeAttributor(@PathVariable("projectId") Long projectId, @PathVariable("dispatchUserId") Long dispatchUserId) {
        return AjaxResult.success(projectService.changeAttributorUser(projectId, dispatchUserId));
    }


    @ApiOperation(value = "分页（带条件）-项目")
    @PostMapping("/page")
    @RequiresPermissions("biz:project:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        String postCode = loginUser.getPostCode();
        if (StringUtils.isNotEmpty(postCode) && postCode.equals("GONG_SHANG_ZHU_CE_/_BIAN_GENG_WEI_HU_YUAN")) {
            queryPage.getParams().put("projectType", "GU_QUAN_HE_ZUO_XIANG_MU");
        }
        return success(projectService.page(queryPage));
    }

    @ApiOperation(value = "分页（带条件）-项目")
    @PostMapping("/pageAll")
   // @RequiresPermissions("biz:project:list")
    public AjaxResult pageTwo(@RequestBody QueryPage queryPage) {
      //  LoginUser loginUser = SecurityUtils.getLoginUser();

        IPage<Project> page =  projectService.pageAll(queryPage);
        return success(page);
    }


    @ApiOperation(value = "分页（带条件）-项目-客户管理")
    @PostMapping("/projectPageFromCustomer")
//    @RequiresPermissions("biz:project:list")
    public AjaxResult projectPageFromCustomer(@RequestBody QueryPage queryPage) {
//        LoginUser loginUser = SecurityUtils.getLoginUser();
//        String postCode = loginUser.getPostCode();
//        if (StringUtils.isNotEmpty(postCode) && postCode.equals("GONG_SHANG_ZHU_CE_/_BIAN_GENG_WEI_HU_YUAN")) {
//            queryPage.getParams().put("projectType", "GU_QUAN_HE_ZUO_XIANG_MU");
//        }
        return success(projectService.projectPageFromCustomer(queryPage));
    }


    @ApiOperation(value = "导出-项目数据")
    @PostMapping("/export")
    @RequiresPermissions("biz:project:export")
    public void export(@RequestBody QueryPage queryPage) {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        String postCode = loginUser.getPostCode();
        if (StringUtils.isNotEmpty(postCode) && postCode.equals("GONG_SHANG_ZHU_CE_/_BIAN_GENG_WEI_HU_YUAN")) {
            queryPage.getParams().put("projectType", "GU_QUAN_HE_ZUO_XIANG_MU");
        }


        queryPage.setPageSize(Integer.MAX_VALUE);
        IPage page = projectService.page(queryPage);
        com.bytefinger.toutuo.common.util.ExcelUtil<Project> excelUtil = new ExcelUtil<>(Project.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), page.getRecords(), "项目数据");
    }

    @ApiOperation(value = "导出-导出业绩台账")
    @PostMapping("/exportProjectAchievement")
    @RequiresPermissions("biz:project:export")
    public void exportProjectAchievement(@RequestBody QueryPage queryPage) {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        String postCode = loginUser.getPostCode();
        if (StringUtils.isNotEmpty(postCode) && postCode.equals("GONG_SHANG_ZHU_CE_/_BIAN_GENG_WEI_HU_YUAN")) {
            queryPage.getParams().put("projectType", "GU_QUAN_HE_ZUO_XIANG_MU");
        }

        queryPage.setPageSize(Integer.MAX_VALUE);
        IPage page = projectService.pageTwo(queryPage);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<ProjectAchievementBO> records = page.getRecords();
            //根据项目id查询出下载合同扫描件

            List<Long> projectIds = records.stream().map(t -> t.getId()).collect(Collectors.toList());
            List<AuditDocumentTemplate> templates = projectDocumentTemplateService.list(Wrappers.<AuditDocumentTemplate>lambdaQuery()
                    .eq(AuditDocumentTemplate::getStepMenuId, StepMenuId).eq(AuditDocumentTemplate::getOperName, OperName));
            List<AuditDocument> documentLists = new ArrayList<>();
            templates.forEach(v -> {
                List<AuditDocument> documents = projectDocumentService.list(Wrappers.<AuditDocument>lambdaQuery()
                        .eq(AuditDocument::getDocumentTemplateId, v.getId())
                        .in(AuditDocument::getProjectId, projectIds)
                        .eq(AuditDocument::getStepMenuId, StepMenuId));
                documentLists.addAll(documents);
            });


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (ProjectAchievementBO record : records) {
                if (!ObjectUtils.isEmpty(record.getServiceBeginTime())) {
                    String format = simpleDateFormat.format(record.getServiceBeginTime());
                    record.setServiceBeginTimeStr(format);
                }
                if (!ObjectUtils.isEmpty(record.getServiceEndTime())) {
                    String format = simpleDateFormat.format(record.getServiceEndTime());
                    record.setServiceEndTimeStr(format);
                }
                if (!ObjectUtils.isEmpty(record.getSignTime())) {
                    String format = simpleDateFormat.format(record.getSignTime());
                    record.setSignTimeStr(format);
                }

                List<String> pathString = new ArrayList<>();

                documentLists.forEach(t -> {
                    if (record.getId().equals(t.getProjectId()) && Objects.nonNull(t.getDocmentObject())) {
                        SysFile file = JSON.parseObject(t.getDocmentObject(), SysFile.class);
                        String path = "";
                        if (Objects.nonNull(file)) {
                            path = downLoadHost + "/system/download3?name=" + file.getName() + "&ossPath=" + file.getOssPath();
                        }
                        if (StringUtils.isNotEmpty(path))
                            pathString.add(path);
                    }
                });

                record.setDownloadPath(String.join(";", pathString));
            }

        }
        com.bytefinger.toutuo.common.util.ExcelUtil<ProjectAchievementBO> excelUtil = new ExcelUtil<>(ProjectAchievementBO.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), page.getRecords(), "项目数据");
    }

    @ApiOperation(value = "拓展导出-导出业绩台账")
    @PostMapping("/exportProjectAchievementTwo")
    @RequiresPermissions("biz:project:export")
    public void exportProjectAchievementTwo(@RequestBody QueryPage queryPage) {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        String postCode = loginUser.getPostCode();
        if (StringUtils.isNotEmpty(postCode) && postCode.equals("GONG_SHANG_ZHU_CE_/_BIAN_GENG_WEI_HU_YUAN")) {
            queryPage.getParams().put("projectType", "GU_QUAN_HE_ZUO_XIANG_MU");
        }

        queryPage.setPageSize(Integer.MAX_VALUE);
        IPage page = projectExtensionService.pageTwo(queryPage);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<ProjectAchievementBO> records = page.getRecords();
            //根据项目id查询出下载合同扫描件

            List<Long> projectIds = records.stream().map(t -> t.getId()).collect(Collectors.toList());
            List<AuditDocumentTemplate> templates = projectDocumentTemplateService.list(Wrappers.<AuditDocumentTemplate>lambdaQuery()
                    .eq(AuditDocumentTemplate::getStepMenuId, StepMenuId).eq(AuditDocumentTemplate::getOperName, OperName));
            List<AuditDocument> documentLists = new ArrayList<>();
            templates.forEach(v -> {
                List<AuditDocument> documents = projectDocumentService.list(Wrappers.<AuditDocument>lambdaQuery()
                        .eq(AuditDocument::getDocumentTemplateId, v.getId())
                        .in(AuditDocument::getProjectId, projectIds)
                        .eq(AuditDocument::getStepMenuId, StepMenuId));
                documentLists.addAll(documents);
            });


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (ProjectAchievementBO record : records) {
                if (!ObjectUtils.isEmpty(record.getServiceBeginTime())) {
                    String format = simpleDateFormat.format(record.getServiceBeginTime());
                    record.setServiceBeginTimeStr(format);
                }
                if (!ObjectUtils.isEmpty(record.getServiceEndTime())) {
                    String format = simpleDateFormat.format(record.getServiceEndTime());
                    record.setServiceEndTimeStr(format);
                }
                if (!ObjectUtils.isEmpty(record.getSignTime())) {
                    String format = simpleDateFormat.format(record.getSignTime());
                    record.setSignTimeStr(format);
                }
                documentLists.forEach(t -> {
                    if (record.getId().equals(t.getProjectId()) && Objects.nonNull(t.getDocmentObject())) {
                        SysFile file = JSON.parseObject(t.getDocmentObject(), SysFile.class);
                        String path = "";
                        if (Objects.nonNull(file)) {
                            path = downLoadHost + "/system/download3?name=" + file.getName() + "&ossPath=" + file.getOssPath();
                            record.setDownloadPath(path);
                        }

                    }
                });
            }

        }
        com.bytefinger.toutuo.common.util.ExcelUtil<ProjectAchievementBO> excelUtil = new ExcelUtil<>(ProjectAchievementBO.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), page.getRecords(), "项目数据");
    }

    @ApiOperation(value = "", hidden = true)
    @PutMapping("/syncProjectCompany")
    @InnerAuth
    public R syncProjectCompany() {
        projectService.syncProjectCompany();
        return R.ok();
    }

    @ApiOperation(value = "", hidden = true)
    @PutMapping("/projectDaoQiStatusChange")
    @InnerAuth
    public R projectDaoQiStatusChange(@RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        projectService.projectDaoQiStatusChange();
        return R.ok();
    }

    @GetMapping("/projectDaoqiChange")
    public R projectDaoqiChange() {
        projectService.projectDaoqiChange();
        return R.ok();
    }

    @ApiOperation(value = "", hidden = true)
    @PutMapping("/projectCircularChange")
    @InnerAuth
    public R projectCircularChange(@RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        projectService.projectCircularChange();
        return R.ok();
    }

    @GetMapping("/circularChange")
    public R circularChange() {
        projectService.projectCircularChange();
        return R.ok();
    }

    @ApiOperation(value = "克隆项目")
    @PutMapping("/clone")
    @RequiresPermissions("biz:project:clone")
    public AjaxResult clone(@RequestBody Project project) {
        return projectService.clone(project).toAjaxResult();
    }

    @ApiOperation(value = "分页（带条件）-项目通告")
    @PostMapping("/pageNotify")
    public AjaxResult pageNotify(@RequestBody QueryPage queryPage) {
        return success(projectService.pageNotify(queryPage));
    }

    @ApiOperation(value = "分页（带条件）-项目丢盘监控通告")
    @PostMapping("/pageEndNotify")
    public AjaxResult pageEndNotify(@RequestBody QueryPage queryPage) {
        return success(projectService.pageEndNotify(queryPage));
    }

    @ApiOperation(value = "导出-项目通告")
    @DataFill
    @PostMapping("/exportNotify")
    public void exportNotify(@RequestBody QueryPage queryPage) {
        queryPage.setPageNo(1);
        queryPage.setPageSize(Integer.MAX_VALUE);
        com.bytefinger.common.security.utils.poi.ExcelUtil<ProjectNotifyDto> excelUtil = new com.bytefinger.common.security.utils.poi.ExcelUtil<>(ProjectNotifyDto.class);
        IPage iPage = projectService.pageNotify(queryPage);
        excelUtil.exportExcel(ServletUtils.getResponse(), iPage.getRecords(), "项目通告");
    }

    @ApiOperation(value = "导出-项目丢盘情况监管")
    @DataFill
    @PostMapping("/exportEndNotify")
    public void exportEndNotify(@RequestBody QueryPage queryPage) {
        queryPage.setPageSize(Integer.MAX_VALUE);
        com.bytefinger.common.security.utils.poi.ExcelUtil<EndProjectNotifyDto> excelUtil = new com.bytefinger.common.security.utils.poi.ExcelUtil<>(EndProjectNotifyDto.class);
        IPage iPage = projectService.pageEndNotify(queryPage);
        excelUtil.exportExcel(ServletUtils.getResponse(), iPage.getRecords(), "项目丢盘情况监管");
    }

    @ApiOperation(value = "获取节点枚举集合")
    @GetMapping("/getProjectStepMenuList")
    public AjaxResult getProjectStepMenuList() {
        return success(projectService.getProjectStepMenuList());
    }

    @ApiOperation(value = "导出开标信息列表")
    @DataFill
    @PostMapping("/getOpenMarkList")
    public void getOpenMarkList(@RequestBody QueryPage queryPage, HttpServletResponse response) {
        List<ProjectOpenMarkExportVo> result = projectService.getOpenMarkList(queryPage);
        com.bytefinger.toutuo.common.util.ExcelUtil<ProjectOpenMarkExportVo> excelUtil = new ExcelUtil<>(ProjectOpenMarkExportVo.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), result, "投标数据");
    }


    @ApiOperation(value = "项目删除")
    @PostMapping("/deleteProject")
    public AjaxResult deleteProject(@RequestBody List<Long> projectIds) {
        Long userid = SecurityUtils.getLoginUser().getUserid();
        List<Project> projectList = projectService.listByIds(projectIds);
        StringBuilder returnMessage=new StringBuilder("");

        for (Project project : projectList) {
            if ("SHI".equals(project.getIsSyncOperation())){
                returnMessage = returnMessage.append(project.getProjectName()).append("项目").append("已同步至拓后，不允许删除！\n");
            }

        }

        List<Long> projectIdList = projectList.stream().map(Project::getId).collect(Collectors.toList());
        //判断是否有进入投后的项目
        List<ProjectCompany> projectCompanyList = projectCompanyMapper.selectList(Wrappers.<ProjectCompany>lambdaQuery().in(ProjectCompany::getProjectId, projectIdList));
        if (CollectionUtil.isNotEmpty(projectCompanyList)){
            for (ProjectCompany projectCompany : projectCompanyList) {
                List<Project> collect = projectList.stream().filter(item -> item.getId().equals(projectCompany.getProjectId())).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(collect)){
                    returnMessage =   returnMessage.append(collect.get(0).getProjectName()).append("项目").append("进入了投后，不允许删除！\n");
                }
            }

        }
        //判断是否有OA审批未完成的项目
        List<Integer> approvalStatus = new ArrayList<>();
        approvalStatus.add(ApproveStatusEnum.UNAPPROVED.getCode());
        approvalStatus.add(ApproveStatusEnum.APPROVAL_DAI_QUE_REN.getCode());
        approvalStatus.add(ApproveStatusEnum.IN_APPROVAL.getCode());
        List<OaApproval> approvalList = oaApprovalService.list(Wrappers.<OaApproval>lambdaQuery()
                .in(OaApproval::getRecordId, projectIdList)
                .eq(OaApproval::getModuleName, "Project")
                .in(OaApproval::getApprovalStatus, approvalStatus));

        if (CollectionUtil.isNotEmpty(approvalList)){
            for (OaApproval oaApproval : approvalList) {
                List<Project> collect = projectList.stream().filter(item -> item.getId().equals(oaApproval.getRecordId())).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(collect)){
                    returnMessage =   returnMessage.append(collect.get(0).getProjectName()).append("项目").append("有未完成的OA审批，不允许删除！\n");

                }

            }
        }
        System.out.println(returnMessage);


        if (StringUtils.isNotBlank(returnMessage.toString())){
            return AjaxResult.error(" "+returnMessage.toString());
        }

        for (Project project : projectList) {

            ProjectRecodingBak projectRecodingBak = new ProjectRecodingBak();
            BeanUtils.copyBeanProp(projectRecodingBak,project);
            BeanUtil.copyProperties(project,projectRecodingBak);
            projectRecodingBak.setId(project.getId());
            projectRecodingBakMapper.insertProjectBak(projectRecodingBak);
            projectRecodingBakMapper.updateTime(projectRecodingBak.getId(),project.getCreateTime(),project.getUpdateTime());

            ProjectCompanyLog projectCompanyLog = new ProjectCompanyLog();
            projectCompanyLog.setRecordId(project.getId());
            projectCompanyLog.setRecordCode(project.getProjectNo());
            projectCompanyLog.setRecordName(project.getProjectName());
            projectCompanyLog.setRecordType("PROJECT_DELETE");
            projectCompanyLog.setRecordTypeName("项目删除");
            projectCompanyLog.setCreateUserId(userid);
            projectCompanyLog.setUpdateUserId(userid);
            projectCompanyLog.setIsReset(0);
            projectCompanyLogService.save(projectCompanyLog);
            projectService.removeById(project.getId());
        }



        return AjaxResult.success();
    }

    @ApiOperation(value = "项目查重")
    @PostMapping("/projectDuplicateCheck")
    public AjaxResult projectDuplicateCheck(@RequestBody Project project) {
        return projectService.matchProjectSimilarity(project).toAjaxResult();
    }

}
