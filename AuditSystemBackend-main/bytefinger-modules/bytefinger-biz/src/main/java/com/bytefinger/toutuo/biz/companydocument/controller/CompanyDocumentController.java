package com.bytefinger.toutuo.biz.companydocument.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import com.bytefinger.toutuo.biz.companystep.domain.ProjectCompanyStepMenu;
import com.bytefinger.toutuo.biz.companystep.service.IProjectCompanyStepMenuService;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 子公司文件 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "子公司文件")
@AllArgsConstructor
@RestController
@RequestMapping("/companyDocument")
public class CompanyDocumentController extends BaseController {
    private final IProjectCompanyDocumentService projectCompanyDocumentService;

    private final IProjectCompanyDocumentTemplateService projectCompanyDocumentTemplateService;

    private final IProjectCompanyStepMenuService projectCompanyStepMenuService;

    private final IProjectCompanyService projectCompanyService;

    @ApiOperation(value = "新增-子公司文件")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyDocument projectCompanyDocument) {
        projectCompanyDocumentService.save(projectCompanyDocument);
        return success(projectCompanyDocument);
    }

    @ApiOperation(value = "修改-子公司文件")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyDocument projectCompanyDocument) {
        return success(projectCompanyDocumentService.updateById(projectCompanyDocument));
    }

    @ApiOperation(value = "删除-子公司文件")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyDocumentService.removeById(id));
    }

    @ApiOperation(value = "详情-子公司文件")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyDocumentService.getById(id));
    }

    @ApiOperation(value = "列表-子公司文件")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyDocumentService.list());
    }

    @ApiOperation(value = "列表-项目文件")
    @GetMapping("/list/{companyId}/{stepMenuId}/{recordId}")
    @RequiresPermissions("biz:projectDocument:list")
    public AjaxResult list(@PathVariable("companyId") Long companyId, @PathVariable("stepMenuId") Long stepMenuId, @PathVariable("recordId") Long recordId) {
        ProjectCompany company = projectCompanyService.getById(companyId);
        if (ObjectUtils.isEmpty(company)) {
            return success();
        }
        Set<Long> stepMenuIds = new HashSet<>();
        stepMenuIds.add(stepMenuId);
        List<ProjectCompanyStepMenu> list = projectCompanyStepMenuService.list(Wrappers.<ProjectCompanyStepMenu>lambdaQuery().eq(ProjectCompanyStepMenu::getParentId, stepMenuId));
        if(!CollectionUtils.isEmpty(list)){
            stepMenuIds.addAll(list.stream().map(v -> v.getId()).collect(Collectors.toList()));
        }

        List<ProjectCompanyDocumentTemplate> templates = projectCompanyDocumentTemplateService.list(Wrappers.<ProjectCompanyDocumentTemplate>lambdaQuery()
                .eq(ProjectCompanyDocumentTemplate::getStepMenuId, stepMenuId));
        templates.forEach(v -> {
            try {
                LambdaQueryWrapper<ProjectCompanyDocument> lambdaQueryWrapper = Wrappers.<ProjectCompanyDocument>lambdaQuery()
                        .eq(ProjectCompanyDocument::getDocumentTemplateId, v.getId())
                        .eq(ProjectCompanyDocument::getCompanyId, companyId)
                        .in(ProjectCompanyDocument::getStepMenuId, stepMenuIds);
                if(-1 != recordId){
                    lambdaQueryWrapper.eq(ProjectCompanyDocument::getRecordId, recordId);
                }
                List<ProjectCompanyDocument> documents = projectCompanyDocumentService.list(lambdaQueryWrapper);
                // 序列化原列表
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(documents);
                oos.close();
                // 反序列化得到新列表
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                v.setProjectCompanyDocumentList((List<ProjectCompanyDocument>) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return success(templates);
    }


    @ApiOperation(value = "分页（带条件）-项目文件")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectDocument:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        Page<ProjectCompanyDocument> page = projectCompanyDocumentService.page(queryPage);
/*        if(!CollectionUtils.isEmpty(page.getRecords()) && page.getRecords().size() > 0){
            page.getRecords().forEach(v -> {
                ProjectCompany company = projectCompanyService.getById(v.getCompanyId());
                v.setCompanyName(company.getName());
            });
        }*/
        return success(page);
    }
}
