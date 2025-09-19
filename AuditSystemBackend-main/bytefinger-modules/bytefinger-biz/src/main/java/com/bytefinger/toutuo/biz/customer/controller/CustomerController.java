package com.bytefinger.toutuo.biz.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.dto.ChangeDTO;
import com.bytefinger.common.core.web.domain.dto.ChangeIdsDTO;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.customer.constants.CustomerConstant;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import com.bytefinger.toutuo.biz.customer.service.ICustomerService;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 客户 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-25
 */
@Slf4j
@Api(tags = "客户")
@AllArgsConstructor
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    private final ICustomerService customerService;

    private final IProjectService projectService;

    @ApiOperation(value = "新增-客户")
    @PutMapping("/add")
    @RequiresPermissions("biz:customer:add")
    public AjaxResult add(@RequestBody @Validated Customer customer) {
        if (CustomerConstant.KE_HU_LEI_XING_QI_YE_JI_KE_HU.equals(customer.getCustomerType()) && customerService.count(Wrappers.<Customer>lambdaQuery()
                .eq(Customer::getCustomerCompanyNo, customer.getCustomerCompanyNo())) > 0) {
            return AjaxResult.error("统一社会信用代码存在重复");
        }
        if(customerService.count(Wrappers.<Customer>lambdaQuery()
                .eq(Customer::getCustomerName, customer.getCustomerName())) > 0){
            return AjaxResult.error("客户名称存在重复");
        }
        return customerService.add(customer).toAjaxResult();
    }

    @ApiOperation(value = "修改-客户")
    @PutMapping("/update")
    @RequiresPermissions("biz:customer:update")
    public AjaxResult update(@RequestBody @Validated Customer customer) {
        if (CustomerConstant.KE_HU_LEI_XING_QI_YE_JI_KE_HU.equals(customer.getCustomerType()) && customerService.count(Wrappers.<Customer>lambdaQuery()
                .eq(Customer::getCustomerCompanyNo, customer.getCustomerCompanyNo())
                .ne(Customer::getId, customer.getId())) > 0) {
            return AjaxResult.error("统一社会信用代码存在重复");
        }
        if(customerService.count(Wrappers.<Customer>lambdaQuery()
                .eq(Customer::getCustomerName, customer.getCustomerName())
                .ne(Customer::getId, customer.getId())) > 0) {
            return AjaxResult.error("客户名称存在重复");
        }
        return customerService.update(customer).toAjaxResult();
    }


    @ApiOperation(value = "认领-客户")
    @PutMapping("/claim")
    @RequiresPermissions("biz:customer:update")
    public AjaxResult claim(@RequestBody @Validated Customer customer) {
        if (CustomerConstant.KE_HU_LEI_XING_QI_YE_JI_KE_HU.equals(customer.getCustomerType()) && customerService.count(Wrappers.<Customer>lambdaQuery()
                .eq(Customer::getCustomerCompanyNo, customer.getCustomerCompanyNo())
                .ne(Customer::getId, customer.getId())) > 0) {
            return AjaxResult.error("统一社会信用代码存在重复");
        }
        if(customerService.count(Wrappers.<Customer>lambdaQuery()
                .eq(Customer::getCustomerName, customer.getCustomerName())
                .ne(Customer::getId, customer.getId())) > 0) {
            return AjaxResult.error("客户名称存在重复");
        }
        return customerService.claim(customer).toAjaxResult();
    }

    @ApiOperation(value = "删除-客户")
    @DeleteMapping("/delete")
    @RequiresPermissions("biz:customer:delete")
    public AjaxResult delete(@RequestBody @Validated ChangeDTO changeStatusDTO) {
        if (projectService.count(Wrappers.<Project>lambdaQuery()
                .eq(Project::getCustomerId, changeStatusDTO.getId())) > 0) {
            return error("客户已关联数据，不允许删除");
        }
        return customerService.deleteById(changeStatusDTO.getId(), changeStatusDTO.getRemarks()).toAjaxResult();
    }

    @ApiOperation(value = "详情-客户")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:customer:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(customerService.getById(id));
    }

    @ApiOperation(value = "分页（带条件）-客户")
    @PostMapping("/page")
    @RequiresPermissions("biz:customer:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(customerService.page(queryPage));
    }

    @ApiOperation(value = "导出-客户数据")
    @PostMapping("/export")
    @RequiresPermissions("biz:project:export")
    public void export(@RequestBody QueryPage queryPage) {
        queryPage.setPageSize(Integer.MAX_VALUE);
        IPage page = customerService.page(queryPage);
        com.bytefinger.toutuo.common.util.ExcelUtil<Customer> excelUtil = new ExcelUtil<>(Customer.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), page.getRecords(), "客户数据");
    }

    /*
    * 客户删除列表
    * */
    @ApiOperation(value = "分页（带条件）-客户删除列表")
    @PostMapping("/list")
    @RequiresPermissions("biz:customer:list")
    public AjaxResult list(@RequestBody QueryPage queryPage) {
        return success(customerService.page(queryPage));
    }

    @ApiOperation(value = "删除-客户")
    @PostMapping("/deleteCustomer")
    @RequiresPermissions("biz:customer:delete")
    public AjaxResult deleteCustomer(@RequestBody @Validated ChangeIdsDTO changeIdsDTO) {
        if (changeIdsDTO.getIds().length<=0) {
            return error("请选择删除数据");
        }
        @NotNull(message = "请选择数据") Long[] ids = changeIdsDTO.getIds();
        for (int i = 0; i < changeIdsDTO.getIds().length; i++) {
            if (projectService.count(Wrappers.<Project>lambdaQuery()
                    .eq(Project::getCustomerId,changeIdsDTO.getIds()[i])) > 0) {
                int i1 = i+1;
                return error("选择数据的第"+ i1 +"条客户已关联数据，不允许删除");
            }
        }

        return customerService.deleteByIds(changeIdsDTO.getIds()).toAjaxResult();
    }

}
