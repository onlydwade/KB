package com.bytefinger.toutuo.biz.companyfinance.controller;

import cn.hutool.core.date.DateUtil;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.toutuo.biz.companyfinance.domain.ProjectCompanyFinanceProfit;
import com.bytefinger.toutuo.biz.companyfinance.service.IProjectCompanyFinanceProfitService;
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
 * 财务利润信息 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-23
 */
@Slf4j
@Api(tags = "财务利润信息")
@AllArgsConstructor
@RestController
@RequestMapping("/companyFinanceProfit")
public class ProjectCompanyFinanceProfitController extends BaseController {
    private final IProjectCompanyFinanceProfitService projectCompanyFinanceProfitService;

    private final IProjectCompanyService projectCompanyService;

    @ApiOperation(value = "新增-财务利润信息")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyFinanceProfit projectCompanyFinanceProfit) {
        //设置财务信息数据名称
        ProjectCompany projectCompany = projectCompanyService.getById(projectCompanyFinanceProfit.getCompanyId());
        String financeName = projectCompany.getName() +DateUtil.format(projectCompanyFinanceProfit.getBeginTime(), "yyyy年MM月") +"-"+ DateUtil.format(projectCompanyFinanceProfit.getEndTime(), "yyyy年MM月") + "累计利润信息";
        projectCompanyFinanceProfit.setName(financeName);
        //设置年份
        String year = DateUtil.year(projectCompanyFinanceProfit.getEndTime()) + "";
        projectCompanyFinanceProfit.setYear(year);
        projectCompanyFinanceProfitService.save(projectCompanyFinanceProfit);
        return success(projectCompanyFinanceProfit);
    }

    @ApiOperation(value = "修改-财务利润信息")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyFinanceProfit projectCompanyFinanceProfit) {
        return success(projectCompanyFinanceProfitService.updateById(projectCompanyFinanceProfit));
    }

    @ApiOperation(value = "删除-财务利润信息")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyFinanceProfitService.removeById(id));
    }

    @ApiOperation(value = "详情-财务利润信息")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyFinanceProfitService.getById(id));
    }

    @ApiOperation(value = "列表-财务利润信息")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyFinanceProfitService.list());
    }

    @ApiOperation(value = "分页（带条件）-财务利润信息")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyFinanceProfitService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation(value = "分页（带条件）")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) throws IOException, ClassNotFoundException {
        return success(projectCompanyFinanceProfitService.page(stepMenuId, queryPage));
    }

}
