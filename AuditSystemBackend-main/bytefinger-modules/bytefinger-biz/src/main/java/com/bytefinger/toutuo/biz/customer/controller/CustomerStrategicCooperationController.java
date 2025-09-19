package com.bytefinger.toutuo.biz.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import com.bytefinger.toutuo.biz.customer.domain.CustomerStrategicCooperation;
import com.bytefinger.toutuo.biz.customer.service.ICustomerService;
import com.bytefinger.toutuo.biz.customer.service.ICustomerStrategicCooperationService;
import com.bytefinger.toutuo.biz.keyword.domain.Keywords;
import com.bytefinger.toutuo.biz.operlog.domain.OperLog;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyItem;
import com.bytefinger.toutuo.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户战略合作 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "客户战略合作")
@AllArgsConstructor
@RestController
@RequestMapping("/customerStrategicCooperation")
public class CustomerStrategicCooperationController extends BaseController {
    private final ICustomerStrategicCooperationService customerStrategicCooperationService;

    private final ICustomerService customerService;

    @ApiOperation(value = "新增-客户战略合作")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
//    @RequiresPermissions("biz:customerStrategicCooperation:add")
    public AjaxResult add(@RequestBody CustomerStrategicCooperation customerStrategicCooperation) {
        customerStrategicCooperationService.save(customerStrategicCooperation);
        //customerService.addKeywords(customerStrategicCooperation.getCustomerId());
        return success(customerStrategicCooperation);
    }

    @ApiOperation(value = "修改-客户战略合作")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:customerStrategicCooperation:update")
    public AjaxResult update(@RequestBody CustomerStrategicCooperation customerStrategicCooperation) {
        customerStrategicCooperationService.updateById(customerStrategicCooperation);
        customerService.addKeywords(customerStrategicCooperation.getCustomerId());
        return success(customerStrategicCooperation);
    }

    @ApiOperation(value = "删除-客户战略合作")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:customerStrategicCooperation:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(customerStrategicCooperationService.removeById(id));
    }

    @ApiOperation(value = "详情-客户战略合作")
    @GetMapping("/get/{customerId}")
    @RequiresPermissions("biz:customerStrategicCooperation:get")
    public AjaxResult get(@PathVariable("customerId") Long customerId) {
        CustomerStrategicCooperation customerStrategicCooperation = customerStrategicCooperationService.getById(customerId);//.getOne(Wrappers.<CustomerStrategicCooperation>lambdaQuery().eq(CustomerStrategicCooperation::, customerId));
        return success(customerStrategicCooperation);
    }

    @ApiOperation(value = "分页（带条件）-客户战略合作")
    @PostMapping("/page")
//    @RequiresPermissions("biz:customerStrategicCooperation:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(customerStrategicCooperationService.page(queryPage));
    }

    @ApiOperation(value = "导出-客户战略合作")
    @PostMapping("/export")
    @RequiresPermissions("biz:project:export")
    public void export(@RequestBody QueryPage queryPageCust) {
        queryPageCust.setPageSize(Integer.MAX_VALUE);
        IPage pageCust = customerService.page(queryPageCust);
        List<Customer> custRecords= pageCust.getRecords();
        List<CustomerStrategicCooperation> list=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(custRecords) && custRecords.size()>0){
            List<Long> ids = custRecords.stream().map(p -> p.getId()).collect(Collectors.toList());
            list = customerStrategicCooperationService.list( Wrappers.<CustomerStrategicCooperation>lambdaQuery().in(CustomerStrategicCooperation::getCustomerId, ids));
            if(CollectionUtils.isNotEmpty(list) && list.size()>0){
                list.forEach(t->{
                    Customer customer  = custRecords.stream().filter(p->t.getCustomerId().equals(p.getId())).collect(Collectors.toList()).get(0);
                    t.setCustomerCode(customer.getCustomerNo());
                    t.setCustomerName(customer.getCustomerName());
                });
            }
        }
        com.bytefinger.toutuo.common.util.ExcelUtil<CustomerStrategicCooperation> excelUtil = new ExcelUtil<>(CustomerStrategicCooperation.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), list, "客户战略合作");
    }



}
