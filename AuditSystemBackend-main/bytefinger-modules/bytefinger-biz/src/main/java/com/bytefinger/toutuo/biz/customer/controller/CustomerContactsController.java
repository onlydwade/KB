package com.bytefinger.toutuo.biz.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.customer.domain.CustomerContacts;
import com.bytefinger.toutuo.biz.customer.mapper.CustomerMapper;
import com.bytefinger.toutuo.biz.customer.service.ICustomerContactsService;
import com.bytefinger.toutuo.biz.project.mapper.ProjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 联系人 前端控制器
 * </p>
 *
 * @author chengwei
 * @since 2022-10-25
 */
@Slf4j
@Api(tags = "联系人")
@AllArgsConstructor
@RestController
@RequestMapping("/customerContacts")
public class CustomerContactsController extends BaseController {

    private final ICustomerContactsService customerContactsService;

    @ApiOperation(value = "新增")
    @PutMapping("/add")
//    @RequiresPermissions("biz:CustomerContacts:add")
    public AjaxResult add(@RequestBody @Validated CustomerContacts customerContactsList) {
        return customerContactsService.add(customerContactsList).toAjaxResult();
    }

    @ApiOperation(value = "修改")
    @PutMapping("/update")
//    @RequiresPermissions("biz:CustomerContacts:update")
    public AjaxResult update(@RequestBody @Validated CustomerContacts customer) {
        return customerContactsService.update(customer).toAjaxResult();
    }

//    @ApiOperation(value = "删除-客户")
//    @DeleteMapping("/delete")
////    @RequiresPermissions("biz:CustomerContacts:delete")
//    public AjaxResult delete(@RequestBody @Validated ChangeDTO changeStatusDTO) {
//        if (projectService.count(Wrappers.<Project>lambdaQuery()
//                .eq(Project::getCustomerId, changeStatusDTO.getId())) > 0) {
//            return error("客户已关联数据，不允许删除");
//        }
//        return customerService.deleteById(changeStatusDTO.getId(), changeStatusDTO.getRemarks()).toAjaxResult();
//    }
//
    @ApiOperation(value = "详情-联系人列表")
    @GetMapping("/get/{customerId}")
    public AjaxResult get(@PathVariable("customerId") String customerId) {
        return success(customerContactsService.getList(customerId));
    }

}
