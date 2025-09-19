package com.bytefinger.toutuo.biz.auditdocument.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocumentTemplate;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文件模板 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "文件模板")
@AllArgsConstructor
@RestController
@RequestMapping("/auditDocumentTemplate")
public class AuditDocumentTemplateController extends BaseController {
    private final IAuditDocumentTemplateService projectDocumentTemplateService;

    @ApiOperation(value = "列表-根据项目节点步骤获取文档模板")
    @GetMapping("/list/{stepMenuId}")
    @RequiresPermissions("biz:auditDocumentTemplate:list")
    public AjaxResult list(@PathVariable("stepMenuId") Long stepMenuId) {
        return success(projectDocumentTemplateService.list(Wrappers.<AuditDocumentTemplate>lambdaQuery()
                .eq(AuditDocumentTemplate::getStepMenuId, stepMenuId)
                .eq(AuditDocumentTemplate::getDisabled, 0)));
    }
}
