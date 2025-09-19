package com.bytefinger.toutuo.biz.companyfinance.controller;

import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.companycoordination.domain.ProjectCompanyDailyCoordination;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import com.bytefinger.toutuo.biz.companyfinance.service.IProjectCompanyPaymentService;
import com.bytefinger.toutuo.biz.companyfinance.domain.ProjectCompanyPayment;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.project.domain.Project;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;

/**
 * <p>
 * 付款记录 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "付款记录")
@AllArgsConstructor
@RestController
@RequestMapping("/companyPayment")
public class ProjectCompanyPaymentController extends BaseController {
    private final IProjectCompanyPaymentService projectCompanyPaymentService;



    @ApiOperation(value = "新增-付款记录")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyPayment projectCompanyPayment) {
        projectCompanyPaymentService.save(projectCompanyPayment);
        return success(projectCompanyPayment);
    }

    @ApiOperation(value = "修改-付款记录")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyPayment projectCompanyPayment) {
        return success(projectCompanyPaymentService.updateById(projectCompanyPayment));
    }

    @ApiOperation(value = "删除-付款记录")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyPaymentService.removeById(id));
    }

    @ApiOperation(value = "详情-付款记录")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyPaymentService.getById(id));
    }

    @ApiOperation(value = "列表-付款记录")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyPaymentService.list());
    }

    @ApiOperation(value = "分页（带条件）-付款记录")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyPaymentService.page(queryPage));
    }

    @ApiOperation(value = "分页（带条件）")
    @PostMapping("/page/{stepMenuId}")
    public AjaxResult page(@PathVariable("stepMenuId") Long stepMenuId, @RequestBody QueryPage queryPage) throws IOException, ClassNotFoundException {
        return success(projectCompanyPaymentService.page(stepMenuId, queryPage));
    }



    @ApiOperation(value = "标记已处理")
    @PutMapping("/changeProcess")
    public AjaxResult changeProcess(@RequestBody ProjectCompanyPayment companyPayment) {
        AjaxResult success = AjaxResult.success(projectCompanyPaymentService.changeProcess(companyPayment.getId(), companyPayment.getProcessUserId(), companyPayment.getProcessDescription()));
        return success;
    }

    @ApiOperation(value = "统计")
    @GetMapping("/total/{companyId}")
    public AjaxResult total(@PathVariable("companyId") Long companyId) {
        List<ProjectCompanyPayment> list = projectCompanyPaymentService.list(Wrappers.<ProjectCompanyPayment>lambdaQuery().eq(ProjectCompanyPayment::getCompanyId, companyId));
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("count", list.size());
        map.put("amount", list.stream().filter(m->m.getAmount()!=null).map(v -> v.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        return success(map);
    }
}
