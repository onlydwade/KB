package com.bytefinger.toutuo.biz.projectextension.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.core.web.domain.vo.ProjectStepMenuVO;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExpansionDocument;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExpansionDocumentTemplate;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectStepExpansionMenu;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExpansionDocumentService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExpansionDocumentTemplateService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectStepExpansionMenuService;
import com.bytefinger.toutuo.biz.projectstep.constants.ProjectStepConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 拓后项目文件
 * </p>
 *
 * @author ycj
 * @since 2023-03-23
 */
@Slf4j
@Api(tags = "拓后项目文件")
@AllArgsConstructor
@RestController
@RequestMapping("/projectExpansionDocument")
public class ExpansionDocumentController extends BaseController {
    private final IProjectExpansionDocumentService projectExpansionDocumentService;

    private final IProjectExpansionDocumentTemplateService projectExpansionDocumentTemplateService;

    private final IProjectService iProjectService;

    @Autowired
    private IProjectStepExpansionMenuService projectStepExpansionMenuService;


    @Autowired(required = false)
    private IProjectExtensionService projectExtensionService;

    @ApiOperation(value = "新增-拓后项目文件")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectExpansionDocument projectExpansionDocument) {
        projectExpansionDocumentService.save(projectExpansionDocument);
        return success(projectExpansionDocument);
    }

    @ApiOperation(value = "修改-拓后项目文件")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectExpansionDocument projectExpansionDocument) {
        return success(projectExpansionDocumentService.updateById(projectExpansionDocument));
    }

    @ApiOperation(value = "删除-拓后项目文件")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectExpansionDocumentService.removeById(id));
    }

    @ApiOperation(value = "详情-拓后项目文件")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectExpansionDocumentService.getById(id));
    }

    @ApiOperation(value = "列表-拓后项目文件")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectExpansionDocumentService.list());
    }

    @ApiOperation(value = "列表-项目文件")
    @GetMapping("/list/{projectId}/{stepMenuId}/{recordId}")
    @RequiresPermissions("biz:projectDocument:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId, @PathVariable("stepMenuId") Long stepMenuId, @PathVariable("recordId") Long recordId) {
        Project project = iProjectService.getById(projectId);
        if (ObjectUtils.isEmpty(project)) {
            return success();
        }
        List<ProjectExpansionDocumentTemplate> templates = projectExpansionDocumentTemplateService.list(Wrappers.<ProjectExpansionDocumentTemplate>lambdaQuery()
                .eq(ProjectExpansionDocumentTemplate::getStepMenuId, stepMenuId));
        templates.forEach(v -> {
            try {
                LambdaQueryWrapper<ProjectExpansionDocument> lambdaQueryWrapper = Wrappers.<ProjectExpansionDocument>lambdaQuery()
                        .eq(ProjectExpansionDocument::getDocumentTemplateId, v.getId())
                        .eq(ProjectExpansionDocument::getProjectId, projectId)
                        .eq(ProjectExpansionDocument::getStepMenuId, stepMenuId);
                if(-1 != recordId){
                    lambdaQueryWrapper.eq(ProjectExpansionDocument::getRecordId, recordId);
                }
                List<ProjectExpansionDocument> documents = projectExpansionDocumentService.list(lambdaQueryWrapper);
                // 序列化原列表
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(documents);
                oos.close();
                // 反序列化得到新列表
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                v.setProjectCompanyDocumentList((List<ProjectExpansionDocument>) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return success(templates);
    }


    @ApiOperation(value = "节点树-根据项目类型获取项目步骤树（只含需要上传文件的节点）")
    @GetMapping("/listByProject")
    @RequiresPermissions("biz:projectStepExpansionMenu:listByProject")
    public AjaxResult listByProjectTypeTwo() {
        List<ProjectStepExpansionMenu> list = projectExtensionService.findProjectEXtensionStepMenuBy();
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        List<Long> menuIds = list.stream().map(v -> v.getId()).collect(Collectors.toList());
        List<ProjectStepMenuVO> tree = projectStepExpansionMenuService.tree(Wrappers.<ProjectStepExpansionMenu>lambdaQuery()
                .in(ProjectStepExpansionMenu::getId, menuIds).orderByAsc(ProjectStepExpansionMenu ::getSorts), ProjectStepConstant.PROJECT_STEP_DOCUMENT_YES);
        List<ProjectStepMenuVO> resultList = tree.stream().filter(v -> (ProjectStepConstant.PROJECT_STEP_DOCUMENT_YES == v.getIsDocument()) || v.getChildren().size() > 0).collect(Collectors.toList());
        return success(resultList);
    }

    @ApiOperation(value = "分页（带条件）-项目文件")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectStepExpansionMenu:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        IPage<ProjectExpansionDocument> page = projectExpansionDocumentService.listPage(queryPage);
/*        if(!CollectionUtils.isEmpty(page.getRecords()) && page.getRecords().size() > 0){
            page.getRecords().forEach(v -> {
                ProjectStepExpansionMenu stepMenu = projectStepExpansionMenuService.getById(v.getStepMenuId());
                v.setStepMenuName(stepMenu.getName());
            });
        }*/
        return success(page);
    }
}

