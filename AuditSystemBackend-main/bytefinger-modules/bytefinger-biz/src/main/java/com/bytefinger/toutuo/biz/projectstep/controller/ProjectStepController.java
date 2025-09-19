package com.bytefinger.toutuo.biz.projectstep.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.interfacelog.service.IInterfaceLogService;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocument;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocumentTemplate;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentService;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentTemplateService;
import com.bytefinger.toutuo.biz.projectstep.constants.ProjectStepConstant;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStep;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.enums.ProjectStatus;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 项目步骤完成状态 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "项目步骤完成状态")
@AllArgsConstructor
@RestController
@RequestMapping("/projectStep")
public class ProjectStepController extends BaseController {
    private final IProjectStepService projectStepService;
    private final IProjectService projectService;
    private final IInterfaceLogService iInterfaceLogService;
    private final IAuditDocumentService projectDocumentService;
    private final IAuditDocumentTemplateService projectDocumentTemplateService;


//    @ApiOperation(value = "新增-项目步骤完成状态")
//    @PutMapping("/add")
//    @Log(title = "操作日志", businessType = BusinessType.INSERT)
//    @RequiresPermissions("biz:projectStep:add")
//    public AjaxResult add(@RequestBody ProjectStep projectStep) {
//        projectStepService.save(projectStep);
//        return success(projectStep);
//    }

    @ApiOperation(value = "修改Or新增-项目步骤完成状态")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectStep:update")
    public AjaxResult update(@RequestBody ProjectStep projectStep) {
        AjaxResult ajaxResult = projectStepService.update(projectStep);
        if (ajaxResult.isError()){
            return ajaxResult;
        }
        if (projectStep.getStepMenuId().equals(Long.valueOf(18)) && projectStep.getStatus().equals(1)){
            Project project = projectService.getById(projectStep.getProjectId());
            project.setUpdateStatusDate(new Date());
            project.setServiceStatus(ProjectStatus.TUO_ZHAN_CHENG_GONG.getCode());
            projectService.updateById(project);
            //拓展成功后调报销接口修改状态
            if( ProjectStepConstant.DAN_YI_TOU_BIAO_XIANG_MU.equals(project.getProjectType())){
                iInterfaceLogService.sentReimbursementApplication( project.getId().toString(),"YES");
            }
        }
        if (projectStep.getStepMenuId().equals(Long.valueOf(40)) && projectStep.getStatus().equals(1)){
            Project project = projectService.getById(projectStep.getProjectId());
            project.setUpdateStatusDate(new Date());
            project.setIsInManagement("SHI");
            projectService.updateById(project);
        }
        //单一投标项目项目立项节点完成的时候将附件代入到标书评审中
        if (projectStep.getStepMenuId().equals(Long.valueOf(13)) && projectStep.getStatus().equals(1)){
            Project project = projectService.getById(projectStep.getProjectId());
            if("DAN_YI_TOU_BIAO_XIANG_MU".equals(project.getProjectType())){
                AuditDocumentTemplate template = projectDocumentTemplateService.getOne(Wrappers.<AuditDocumentTemplate>lambdaQuery()
                        .eq(AuditDocumentTemplate::getStepMenuId,37)
                        .eq(AuditDocumentTemplate::getOperName,"招标文件")
                );
                List<AuditDocument> documents = projectDocumentService.list(Wrappers.<AuditDocument>lambdaQuery()
                        .eq(AuditDocument::getProjectType, "DAN_YI_TOU_BIAO_XIANG_MU")
                        .eq(AuditDocument::getProjectId, projectStep.getProjectId())
                        .eq(AuditDocument::getStepMenuId, 13)
                        .eq(AuditDocument::getDocumentTemplateId,1)
                        .eq(AuditDocument::getDeleted,0));
                List<AuditDocument> newDocumentList = BeanUtil.copyToList(documents, AuditDocument.class);
                newDocumentList.forEach(projectDocument -> {
                    projectDocument.setStepMenuId(37L);
                    projectDocument.setDocumentTemplateId(template.getId());
                });
                projectDocumentService.saveBatch(newDocumentList);
            }
        }
        return success(projectStep);
    }

    @ApiOperation(value = "删除-项目步骤完成状态")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectStep:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectStepService.removeById(id));
    }

    @ApiOperation(value = "详情-项目步骤完成状态")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectStep:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectStepService.getById(id));
    }

    @ApiOperation(value = "列表-项目步骤完成状态")
    @GetMapping("/list")
    @RequiresPermissions("biz:projectStep:list")
    public AjaxResult list() {
        return success(projectStepService.list());
    }

    @ApiOperation(value = "分页（带条件）-项目步骤完成状态")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectStep:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectStepService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation(value = "回退节点-项目步骤完成状态")
    @PostMapping("/callbackProjectStep")
    @RequiresPermissions("biz:projectStep:callbackProjectStep")
    public AjaxResult callbackProjectStep(@RequestBody ProjectStep projectStep) {
        return projectStepService.callbackProjectStep(projectStep);
    }

    @ApiOperation(value = "完成拓后移交节点发送待办测试")
    @PostMapping("/sendMessageTest")
    public void sendMessageTest() {
        projectStepService.sendMessageTest();
    }


}
