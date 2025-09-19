package com.bytefinger.toutuo.biz.companyoperate.controller;

import com.bytefinger.toutuo.biz.companyoperate.service.IProjectCompanyOperateReportItemService;
import com.bytefinger.toutuo.biz.companyoperate.domain.ProjectCompanyOperateReportItem;
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
 * 经营报表-重大事项 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "经营报表-重大事项")
@AllArgsConstructor
@RestController
@RequestMapping("/companyOperateReportItem")
public class ProjectCompanyOperateReportItemController extends BaseController {
    private final IProjectCompanyOperateReportItemService projectCompanyOperateReportItemService;

    @ApiOperation(value = "新增-经营报表-重大事项")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyOperateReportItem projectCompanyOperateReportItem) {
        projectCompanyOperateReportItemService.save(projectCompanyOperateReportItem);
        return success(projectCompanyOperateReportItem);
    }

    @ApiOperation(value = "修改-经营报表-重大事项")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyOperateReportItem projectCompanyOperateReportItem) {
        return success(projectCompanyOperateReportItemService.updateById(projectCompanyOperateReportItem));
    }

    @ApiOperation(value = "删除-经营报表-重大事项")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyOperateReportItemService.removeById(id));
    }

    @ApiOperation(value = "详情-经营报表-重大事项")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyOperateReportItemService.getById(id));
    }

    @ApiOperation(value = "列表-经营报表-重大事项")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyOperateReportItemService.list());
    }

    @ApiOperation(value = "分页（带条件）-经营报表-重大事项")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyOperateReportItemService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
