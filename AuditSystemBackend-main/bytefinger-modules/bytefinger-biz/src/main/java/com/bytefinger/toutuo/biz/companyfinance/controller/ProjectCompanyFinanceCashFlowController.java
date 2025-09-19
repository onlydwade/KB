package com.bytefinger.toutuo.biz.companyfinance.controller;

import cn.hutool.core.date.DateUtil;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.toutuo.biz.companyfinance.domain.ProjectCompanyFinanceCashFlow;
import com.bytefinger.toutuo.biz.companyfinance.service.IProjectCompanyFinanceCashFlowService;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <p>
 * 财务现金流信息 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-23
 */
@Slf4j
@Api(tags = "财务现金流信息")
@AllArgsConstructor
@RestController
@RequestMapping("/companyFinanceCashFlow")
public class ProjectCompanyFinanceCashFlowController extends BaseController {
    private final IProjectCompanyFinanceCashFlowService projectCompanyFinanceCashFlowService;

    private final IProjectCompanyService projectCompanyService;

    @ApiOperation(value = "新增-财务现金流信息")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyFinanceCashFlow projectCompanyFinanceCashFlow) {
        //设置财务信息数据名称
        ProjectCompany projectCompany = projectCompanyService.getById(projectCompanyFinanceCashFlow.getCompanyId());
        String financeName = projectCompany.getName() +DateUtil.format(projectCompanyFinanceCashFlow.getBeginTime(), "yyyy年MM月") +"-"+ DateUtil.format(projectCompanyFinanceCashFlow.getEndTime(), "yyyy年MM月") + "累计现金流信息";
        projectCompanyFinanceCashFlow.setName(financeName);
        //设置年份
        String year = DateUtil.year(projectCompanyFinanceCashFlow.getEndTime()) + "";
        projectCompanyFinanceCashFlow.setYear(year);
        projectCompanyFinanceCashFlowService.save(projectCompanyFinanceCashFlow);
        return success(projectCompanyFinanceCashFlow);
    }

    @ApiOperation(value = "修改-财务现金流信息")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyFinanceCashFlow projectCompanyFinanceCashFlow) {
        return success(projectCompanyFinanceCashFlowService.updateById(projectCompanyFinanceCashFlow));
    }

    @ApiOperation(value = "删除-财务现金流信息")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyFinanceCashFlowService.removeById(id));
    }

    @ApiOperation(value = "详情-财务现金流信息")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyFinanceCashFlowService.getById(id));
    }

    @ApiOperation(value = "列表-财务现金流信息")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyFinanceCashFlowService.list());
    }

    @ApiOperation(value = "分页（带条件）-财务现金流信息")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyFinanceCashFlowService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation(value = "分页（带条件）")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) throws IOException, ClassNotFoundException {
        return success(projectCompanyFinanceCashFlowService.page(stepMenuId, queryPage));
    }


}
