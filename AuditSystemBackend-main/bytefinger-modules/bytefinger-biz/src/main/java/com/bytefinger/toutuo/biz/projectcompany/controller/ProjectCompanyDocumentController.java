package com.bytefinger.toutuo.biz.projectcompany.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
/*import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyDocumentService;*/
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 项目文件 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-11
 */
@Slf4j
@Api(tags = "项目股权公司文件")
@AllArgsConstructor
@RestController
@RequestMapping("/projectCompanyDocument")
public class ProjectCompanyDocumentController extends BaseController {
    private final IProjectCompanyDocumentService projectCompanyDocumentService;

    @ApiOperation(value = "新增-项目文件")
    @PutMapping("/add")
    @RequiresPermissions("biz:projectCompanyDocument:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyDocument projectCompanyDocument) {
        return success(projectCompanyDocumentService.save(projectCompanyDocument));
    }

    @ApiOperation(value = "修改-项目文件")
    @PutMapping("/update")
    @RequiresPermissions("biz:projectCompanyDocument:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyDocument projectCompanyDocument) {
        return success(projectCompanyDocumentService.updateById(projectCompanyDocument));
    }

    @ApiOperation(value = "删除-项目文件")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:projectCompanyDocument:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyDocumentService.removeById(id));
    }

    @ApiOperation(value = "详情-项目文件")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectCompanyDocument:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyDocumentService.getById(id));
    }

    @ApiOperation(value = "列表-股权合作项目-文件")
    @GetMapping("/list/{companyId}")
    @RequiresPermissions("biz:projectCompanyDocument:list")
    public AjaxResult list(@PathVariable("companyId") Long companyId) {
        return success(projectCompanyDocumentService.list(Wrappers.<ProjectCompanyDocument>lambdaQuery()
                .eq(ProjectCompanyDocument::getCompanyId, companyId).orderByDesc(ProjectCompanyDocument::getCreateTime)));
    }

    @ApiOperation(value = "分页（带条件）-项目文件")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectCompanyDocument:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyDocumentService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
