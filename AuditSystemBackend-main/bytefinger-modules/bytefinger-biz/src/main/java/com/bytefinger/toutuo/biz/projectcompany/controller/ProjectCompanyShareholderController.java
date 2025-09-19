package com.bytefinger.toutuo.biz.projectcompany.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyShareholder;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyShareholderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 股权合作项目-公司股东信息 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-01
 */
@Slf4j
@Api(tags = "股权合作项目-公司股东信息")
@AllArgsConstructor
@RestController
@RequestMapping("/projectCompanyShareholder")
public class ProjectCompanyShareholderController extends BaseController {
    private final IProjectCompanyShareholderService projectCompanyShareholderService;

    @ApiOperation(value = "新增-股权合作项目-公司股东信息")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectCompanyShareholder:add")
    public AjaxResult add(@RequestBody ProjectCompanyShareholder projectCompanyShareholder) {
        projectCompanyShareholderService.save(projectCompanyShareholder);
        return success(projectCompanyShareholder);
    }

    @ApiOperation(value = "修改-股权合作项目-公司股东信息")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectCompanyShareholder:update")
    public AjaxResult update(@RequestBody ProjectCompanyShareholder projectCompanyShareholder) {
        projectCompanyShareholderService.updateById(projectCompanyShareholder);
        return success(projectCompanyShareholder);
    }

    @ApiOperation(value = "删除-股权合作项目-公司股东信息")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectCompanyShareholder:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyShareholderService.removeById(id));
    }

    @ApiOperation(value = "详情-股权合作项目-公司股东信息")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectCompanyShareholder:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyShareholderService.getById(id));
    }

    @ApiOperation(value = "列表-股权合作项目-公司股东信息")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectCompanyShareholder:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId) {
        return success(projectCompanyShareholderService.list(Wrappers.<ProjectCompanyShareholder>lambdaQuery().eq(ProjectCompanyShareholder::getProjectId, projectId)
                .orderByDesc(ProjectCompanyShareholder::getCreateTime)));
    }
    @ApiOperation(value = "分页（带条件）-股权合作项目-公司股东信息")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectCompanyShareholder:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyShareholderService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
