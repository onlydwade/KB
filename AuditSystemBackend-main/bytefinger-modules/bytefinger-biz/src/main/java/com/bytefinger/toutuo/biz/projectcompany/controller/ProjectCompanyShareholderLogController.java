package com.bytefinger.toutuo.biz.projectcompany.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyShareholderLog;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyShareholderLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 股权信息变更记录 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "股权信息变更记录")
@AllArgsConstructor
@RestController
@RequestMapping("/projectCompanyShareholderLog")
public class ProjectCompanyShareholderLogController extends BaseController {
    private final IProjectCompanyShareholderLogService projectCompanyShareholderLogService;

    @ApiOperation(value = "新增-股权信息变更记录")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyShareholderLog projectCompanyShareholderLog) {
        projectCompanyShareholderLogService.save(projectCompanyShareholderLog);
        return success(projectCompanyShareholderLog);
    }

    @ApiOperation(value = "修改-股权信息变更记录")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyShareholderLog projectCompanyShareholderLog) {
        return success(projectCompanyShareholderLogService.updateById(projectCompanyShareholderLog));
    }

    @ApiOperation(value = "删除-股权信息变更记录")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyShareholderLogService.removeById(id));
    }

    @ApiOperation(value = "详情-股权信息变更记录")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyShareholderLogService.getById(id));
    }

    @ApiOperation(value = "列表-股权信息变更记录")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyShareholderLogService.list());
    }

    @ApiOperation(value = "分页（带条件）-股权信息变更记录")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        Page<ProjectCompanyShareholderLog> page = projectCompanyShareholderLogService.page(queryPage.toPage(), queryPage.getWrapper());
        return success(page);
    }

    @ApiOperation(value = "分页（带条件）-股权信息变更记录")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) {
        return success(projectCompanyShareholderLogService.page(stepMenuId, queryPage));
    }
}
