package com.bytefinger.toutuo.biz.auditdocument.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.audit.domain.Audit;
import com.bytefinger.toutuo.biz.audit.service.IAuditService;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocument;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocumentTemplate;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentService;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentTemplateService;
import com.bytefinger.toutuo.biz.auditstep.domain.AuditStepMenu;
import com.bytefinger.toutuo.biz.auditstep.service.IAuditStepMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 审计文件 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "审计文件")
@AllArgsConstructor
@RestController
@RequestMapping("/auditDocument")
public class AuditDocumentController extends BaseController {
    
    private final IAuditDocumentService auditDocumentService;
    
    private final IAuditService auditService;
    
    private final IAuditStepMenuService auditStepMenuService;
    
    private final IAuditDocumentTemplateService auditDocumentTemplateService;

    @ApiOperation(value = "新增-审计文件")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:auditDocument:add")
    public AjaxResult add(@RequestBody AuditDocument auditDocument) {
        auditDocumentService.save(auditDocument);
        return success(auditDocument);
    }

    @ApiOperation(value = "修改-审计文件")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:auditDocument:update")
    public AjaxResult update(@RequestBody AuditDocument auditDocument) {
        auditDocumentService.updateById(auditDocument);
        return success(auditDocument);
    }

    @ApiOperation(value = "删除-审计文件")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:auditDocument:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(auditDocumentService.removeById(id));
    }

    @ApiOperation(value = "详情-审计文件")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditDocument:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditDocumentService.getById(id));
    }

    @ApiOperation(value = "列表-审计文件")
    @GetMapping("/list/{auditId}/{stepMenuId}")
    @RequiresPermissions("biz:auditDocument:list")
    public AjaxResult list(@PathVariable("auditId") Long auditId, @PathVariable("stepMenuId") Integer stepMenuId) {
        Audit audit = auditService.getById(auditId);
        if (ObjectUtils.isEmpty(audit)) {
            return success();
        }
        List<AuditDocumentTemplate> templates = auditDocumentTemplateService.list(Wrappers.<AuditDocumentTemplate>lambdaQuery()
                .eq(AuditDocumentTemplate::getStepMenuId, stepMenuId).orderByAsc(AuditDocumentTemplate::getSorts));
        templates.forEach(v -> {
            List<AuditDocument> documents = auditDocumentService.list(Wrappers.<AuditDocument>lambdaQuery()
                    .eq(AuditDocument::getDocumentTemplateId, v.getId())
                    .eq(AuditDocument::getAuditId, audit)
                    .eq(AuditDocument::getStepMenuId, stepMenuId));
            v.setAuditDocumentList(documents);
        });

        return success(templates);
    }

    @ApiOperation(value = "分页（带条件）-审计文件")
    @PostMapping("/page")
    @RequiresPermissions("biz:auditDocument:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        Page<AuditDocument> page = auditDocumentService.page(queryPage.toPage(), queryPage.getWrapper());

        //Page<AuditDocument> page = auditDocumentService.pageTwo(queryPage);


        if(!CollectionUtils.isEmpty(page.getRecords()) && page.getRecords().size() > 0){
            page.getRecords().forEach(v -> {
                AuditStepMenu stepMenu = auditStepMenuService.getById(v.getStepMenuId());
                v.setStepMenuName(stepMenu.getName());
            });
        }
        return success(page);
    }

    @ApiOperation(value = "分页（带条件）-审计文件")
    @PostMapping("/pageTwo")
    @RequiresPermissions("biz:auditDocument:list")
    public AjaxResult pageTwo(@RequestBody QueryPage queryPage) {
        Page<AuditDocument> page = auditDocumentService.page(queryPage);
        return success(page);
    }

}
