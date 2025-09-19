package com.bytefinger.toutuo.biz.companyfinance.controller;

import com.bytefinger.toutuo.biz.companyfinance.service.IProjectCompanyFinanceService;
import com.bytefinger.toutuo.biz.companyfinance.domain.ProjectCompanyFinance;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;

/**
 * <p>
 * 财务信息 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "财务信息")
@AllArgsConstructor
@RestController
@RequestMapping("/companyFinance")
public class ProjectCompanyFinanceController extends BaseController {
    private final IProjectCompanyFinanceService projectCompanyFinanceService;

    @ApiOperation(value = "新增-财务信息")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyFinance projectCompanyFinance) {
        projectCompanyFinanceService.save(projectCompanyFinance);
        return success(projectCompanyFinance);
    }

    @ApiOperation(value = "修改-财务信息")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyFinance projectCompanyFinance) {
        return success(projectCompanyFinanceService.updateById(projectCompanyFinance));
    }

    @ApiOperation(value = "删除-财务信息")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyFinanceService.removeById(id));
    }

    @ApiOperation(value = "详情-财务信息")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyFinanceService.getById(id));
    }

    @ApiOperation(value = "列表-财务信息")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyFinanceService.list());
    }

    @ApiOperation(value = "分页（带条件）-财务信息")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyFinanceService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation(value = "分页（带条件）-财务信息")
    @PostMapping("/totalByCompanyId/{companyId}")
    public AjaxResult totalByCompanyId(@PathVariable("companyId") Long companyId) {
        return success(projectCompanyFinanceService.totalByCompanyId(companyId));
    }

//    @ApiOperation(value = "同步财务数据")
//    @PostMapping("/sync")
//    public AjaxResult sync(@RequestBody ProjectCompanyFinance companyFinance) {
//        String result = projectCompanyFinanceService.sync(companyFinance);
//        if(null != result && result.contains("成功")){
//            return success();
//        }
//        return AjaxResult.error("同步失败，请稍后再试!");
//    }

}
