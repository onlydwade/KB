package com.bytefinger.toutuo.biz.companyoperate.controller;

import com.bytefinger.toutuo.biz.companyoperate.service.IProjectCompanyOperateReportExecutivesService;
import com.bytefinger.toutuo.biz.companyoperate.domain.ProjectCompanyOperateReportExecutives;
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
 * 经营报告-公司高管信息 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "经营报告-公司高管信息")
@AllArgsConstructor
@RestController
@RequestMapping("/companyOperateReportExecutives")
public class ProjectCompanyOperateReportExecutivesController extends BaseController {
    private final IProjectCompanyOperateReportExecutivesService projectCompanyOperateReportExecutivesService;

    @ApiOperation(value = "新增-经营报告-公司高管信息")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyOperateReportExecutives projectCompanyOperateReportExecutives) {
        projectCompanyOperateReportExecutivesService.save(projectCompanyOperateReportExecutives);
        return success(projectCompanyOperateReportExecutives);
    }

    @ApiOperation(value = "修改-经营报告-公司高管信息")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyOperateReportExecutives projectCompanyOperateReportExecutives) {
        return success(projectCompanyOperateReportExecutivesService.updateById(projectCompanyOperateReportExecutives));
    }

    @ApiOperation(value = "删除-经营报告-公司高管信息")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyOperateReportExecutivesService.removeById(id));
    }

    @ApiOperation(value = "详情-经营报告-公司高管信息")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyOperateReportExecutivesService.getById(id));
    }

    @ApiOperation(value = "列表-经营报告-公司高管信息")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyOperateReportExecutivesService.list());
    }

    @ApiOperation(value = "分页（带条件）-经营报告-公司高管信息")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyOperateReportExecutivesService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
