package com.bytefinger.toutuo.biz.companydocument.controller;

import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;

/**
 * <p>
 * 文件模板 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "文件模板")
@AllArgsConstructor
@RestController
@RequestMapping("/companyDocumentTemplate")
public class ProjectCompanyDocumentTemplateController extends BaseController {
    private final IProjectCompanyDocumentTemplateService projectCompanyDocumentTemplateService;

    @ApiOperation(value = "新增-文件模板")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyDocumentTemplate projectCompanyDocumentTemplate) {
        projectCompanyDocumentTemplateService.save(projectCompanyDocumentTemplate);
        return success(projectCompanyDocumentTemplate);
    }

    @ApiOperation(value = "修改-文件模板")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyDocumentTemplate projectCompanyDocumentTemplate) {
        return success(projectCompanyDocumentTemplateService.updateById(projectCompanyDocumentTemplate));
    }

    @ApiOperation(value = "删除-文件模板")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyDocumentTemplateService.removeById(id));
    }

    @ApiOperation(value = "详情-文件模板")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyDocumentTemplateService.getById(id));
    }

    @ApiOperation(value = "列表-文件模板")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyDocumentTemplateService.list());
    }

    @ApiOperation(value = "分页（带条件）-文件模板")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyDocumentTemplateService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
