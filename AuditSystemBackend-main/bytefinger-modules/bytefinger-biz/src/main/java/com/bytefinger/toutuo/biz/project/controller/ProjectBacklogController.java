package com.bytefinger.toutuo.biz.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.enums.ShiFou;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.constants.ProjectConstant;
import com.bytefinger.toutuo.biz.project.domain.*;
import com.bytefinger.toutuo.biz.project.mapper.ProjectBacklogFileMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectBacklogAchievementService;
import com.bytefinger.toutuo.biz.project.service.IProjectBacklogService;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocumentTemplate;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentService;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 补录项目
 *
 */
@Api(tags = "拓后管理")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectBacklog")
public class ProjectBacklogController extends BaseController {

    @Autowired
    private IProjectBacklogService projectBacklogService;

    @Autowired
    private IProjectBacklogAchievementService backlogAchievementService;

    @Autowired
    private ProjectBacklogFileMapper fileMapper;

    private final IAuditDocumentTemplateService projectDocumentTemplateService;
    private final IAuditDocumentService projectDocumentService;


    @ApiOperation(value = "拓展管理-项目补录列表")
    @PostMapping("/list")
    @RequiresPermissions("biz:projectBacklog:list")
    public AjaxResult backlogList(@RequestBody QueryPage queryPage) {

        IPage<ProjectBacklog> result = projectBacklogService.projectBacklogPage(queryPage);

        return success(result);
    }

    @ApiOperation(value = "详情-项目基础资料")
    @GetMapping("/info/{id}")
    @RequiresPermissions("biz:projectBacklog:list")
    public AjaxResult get(@PathVariable("id") Long id) {

        ProjectBacklog project = this.projectBacklogService.getById(id);

        List<ProjectBacklog> yllist = new ArrayList<>();
        yllist.add(project);
        this.projectBacklogService.setYtProjetList(yllist);

        project.setProjectBacklogAchievement(
                this.backlogAchievementService.list(project.getId())
        );

        List<ProjectBacklogFile> backlogFileList = fileMapper.selectList(
                Wrappers.<ProjectBacklogFile>lambdaQuery()
                        .eq(ProjectBacklogFile::getProjectId, project.getId())
                        .eq(ProjectBacklogFile::getDeleted,0)
        );

        project.setProjectBacklogFiles(backlogFileList);
        project.setIsAdministrators(this.projectBacklogService.isAdministrators());

        if(!backlogFileList.isEmpty()){
            List<Long> ids = backlogFileList.stream().map(ProjectBacklogFile::getDocumentTemplateId).collect(Collectors.toList());
            List<AuditDocumentTemplate> templates = this.projectDocumentTemplateService.listByIds(ids);
            templates.forEach(temp->{
                List<ProjectBacklogFile> backlogFiles = new ArrayList<>();
                backlogFileList.forEach(backlog->{
                    if(Objects.equals(temp.getId(), backlog.getDocumentTemplateId())){
                        backlogFiles.add(backlog);
                    }
                });
                //temp.setProjectBacklogDocumentList(backlogFiles);
            });
            project.setProjectBacklogFilesGroup(templates);
        }

        return success(project);
    }

    @ApiOperation(value = "暂存")
    @PostMapping("/add")
    @RequiresPermissions("biz:projectBacklog:list")
    public AjaxResult backlogAdd(@RequestBody ProjectBacklog projectBacklog) {
        return projectBacklogService.add(projectBacklog).toAjaxResult();
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    @RequiresPermissions("biz:projectBacklog:list")
    public AjaxResult submit(@RequestBody ProjectBacklog projectBacklog) {
        return projectBacklogService.submit(projectBacklog).toAjaxResult();
    }

    @ApiOperation(value = "拓展管理-修改补录项目-废弃")
    @PostMapping("/update")
    @RequiresPermissions("biz:projectBacklog:list")
    public AjaxResult backlogUpdate(@RequestBody ProjectBacklog projectBacklog) {
        return projectBacklogService.update(projectBacklog).toAjaxResult();
    }

    @ApiOperation(value = "审批开关获取")
    @GetMapping("/getcheckOa")
    public AjaxResult getcheckOa() {
        return success(this.projectBacklogService.getCheckOa());
    }

    @ApiOperation(value = "开启/关闭审批")
    @GetMapping("/setcheckOa")
    public AjaxResult setcheckOa(Integer checkValue) {
        this.projectBacklogService.setCheckOa(checkValue);
        return success();
    }

    @ApiOperation(value = "动态计算年度转化金额")
    @PostMapping("/annualConversionAmountCalculateNew")
    @RequiresPermissions("biz:project:get")
    public AjaxResult annualConversionAmountCalculate(@RequestBody ProjectBacklog project) {
        // 检查项目类型
        if (ProjectConstant.GU_QUAN_HE_ZUO_XIANG_MU.equals(project.getProjectType())) {
            return success(project);
        }

        String isIncrement = project.getIsIncrement();
        BigDecimal annualConversionAmount;

        //增量取增量合同金额  非增量取合同总金额
        if (ShiFou.SHI.getCode().equals(isIncrement)) {
            annualConversionAmount = projectBacklogService.calculateAnnualConversion(
                    project.getContractAmounts(),
                    project.getProposedServicePeriod(),
                    project.getServiceBeginTime(),
                    project.getServiceEndTime(),
                    null
            );
            project.setAnnualConversionAmounts(annualConversionAmount);
        } else {
            annualConversionAmount = projectBacklogService.calculateAnnualConversion(
                    project.getContractAmount(),
                    project.getProposedServicePeriod(),
                    project.getServiceBeginTime(),
                    project.getServiceEndTime(),
                    null
            );
            project.setAnnualConversionAmount(annualConversionAmount);
        }

        return success(annualConversionAmount);
    }

    @ApiOperation(value = "列表-项目文件")
    @GetMapping("/projectDocument/list")
    @RequiresPermissions("biz:projectDocument:list")
    public AjaxResult list() {
        List<AuditDocumentTemplate> templates = projectDocumentTemplateService.list(Wrappers.<AuditDocumentTemplate>lambdaQuery()
                .eq(AuditDocumentTemplate::getStepMenuId, 18).orderByAsc(AuditDocumentTemplate::getSorts));
        return success(templates);
    }

    @ApiOperation(value = "列表-项目补录业绩分配")
    @GetMapping("/getAchievementList/{projectId}")
    public AjaxResult backlogList(@PathVariable("projectId") Long projectId){
        return success(backlogAchievementService.list(projectId));
    }

}
