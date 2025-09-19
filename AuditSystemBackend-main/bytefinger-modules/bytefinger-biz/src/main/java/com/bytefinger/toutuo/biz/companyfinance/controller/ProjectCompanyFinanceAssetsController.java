package com.bytefinger.toutuo.biz.companyfinance.controller;

import cn.hutool.core.date.DateUtil;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.toutuo.biz.companyfinance.domain.ProjectCompanyFinanceAssets;
import com.bytefinger.toutuo.biz.companyfinance.service.IProjectCompanyFinanceAssetsService;
import com.bytefinger.toutuo.biz.companyfinance.service.IProjectCompanyPaymentService;
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
 * 财务资产负债信息 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-23
 */
@Slf4j
@Api(tags = "财务资产负债信息")
@AllArgsConstructor
@RestController
@RequestMapping("/companyFinanceAssets")
public class ProjectCompanyFinanceAssetsController extends BaseController {
    private final IProjectCompanyFinanceAssetsService projectCompanyFinanceAssetsService;

    private final IProjectCompanyService projectCompanyService;

    @ApiOperation(value = "新增-财务资产负债信息")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyFinanceAssets projectCompanyFinanceAssets) {
        //设置财务信息数据名称
        ProjectCompany projectCompany = projectCompanyService.getById(projectCompanyFinanceAssets.getCompanyId());
        String financeName = projectCompany.getName() +DateUtil.format(projectCompanyFinanceAssets.getBeginTime(), "yyyy年MM月") +"-"+ DateUtil.format(projectCompanyFinanceAssets.getEndTime(), "yyyy年MM月") + "累计资产负债信息";
        projectCompanyFinanceAssets.setName(financeName);
        //设置年份
        String year = DateUtil.year(projectCompanyFinanceAssets.getEndTime()) + "";
        projectCompanyFinanceAssets.setYear(year);
        projectCompanyFinanceAssetsService.save(projectCompanyFinanceAssets);
        return success(projectCompanyFinanceAssets);
    }

    @ApiOperation(value = "修改-财务资产负债信息")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyFinanceAssets projectCompanyFinanceAssets) {
        return success(projectCompanyFinanceAssetsService.updateById(projectCompanyFinanceAssets));
    }

    @ApiOperation(value = "删除-财务资产负债信息")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyFinanceAssetsService.removeById(id));
    }

    @ApiOperation(value = "详情-财务资产负债信息")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyFinanceAssetsService.getById(id));
    }

    @ApiOperation(value = "列表-财务资产负债信息")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyFinanceAssetsService.list());
    }

    @ApiOperation(value = "分页（带条件）-财务资产负债信息")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyFinanceAssetsService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation(value = "分页（带条件）")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) throws IOException, ClassNotFoundException {
        return success(projectCompanyFinanceAssetsService.page(stepMenuId, queryPage));
    }





}


