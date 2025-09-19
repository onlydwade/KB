package com.bytefinger.toutuo.biz.workbrief.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.workbrief.service.IKeyCustomerFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 重点客户跟进 前端控制器
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
@Slf4j
@Api(tags = "重点客户跟进")
@RequiredArgsConstructor
@RestController
@RequestMapping("/keyCustomer/follow")
public class KeyCustomerFollowController extends BaseController {

    @Autowired
    IKeyCustomerFollowService iKeyCustomerFollowService;

    @ApiOperation(value = "最近2周重点客户跟进数据")
    @PostMapping("/getList")
    public AjaxResult getList() {
        return success(iKeyCustomerFollowService.getList());
    }

    @ApiOperation(value = "根据重点客户id查询工作摘要、跟进状态、拜访人信息")
    @GetMapping("/getListByCustomerId/{id}")
    public AjaxResult view(@PathVariable("id") Long id) {
        return success(iKeyCustomerFollowService.getListByCustomerId(id));
    }

}
