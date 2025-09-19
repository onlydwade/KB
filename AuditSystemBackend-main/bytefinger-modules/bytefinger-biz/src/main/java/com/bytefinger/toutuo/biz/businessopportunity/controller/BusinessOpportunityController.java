package com.bytefinger.toutuo.biz.businessopportunity.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.businessopportunity.domain.BusinessOpportunity;
import com.bytefinger.toutuo.biz.businessopportunity.domain.BusinessOpportunityUnlock;
import com.bytefinger.toutuo.biz.businessopportunity.domain.vo.BusinessOpportunityRequestParam;
import com.bytefinger.toutuo.biz.businessopportunity.domain.vo.BusinessOpportunityRespone;
import com.bytefinger.toutuo.biz.businessopportunity.domain.vo.BusinessOpportunityResponeData;
import com.bytefinger.toutuo.biz.businessopportunity.service.IBusinessOpportunityService;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import com.bytefinger.toutuo.biz.companyexit.service.IProjectCompanyExitService;
import com.bytefinger.toutuo.biz.companystep.domain.ProjectCompanyStepMenu;
import com.bytefinger.toutuo.biz.companystep.service.IProjectCompanyStepMenuService;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import com.bytefinger.toutuo.biz.customer.mapper.CustomerMapper;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyService;
import com.bytefinger.toutuo.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 商机库 前端控制器
 * </p>
 *
 * @author chengwei
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "商机库")
@AllArgsConstructor
@RestController
@RequestMapping("/businessOpportunity")
public class BusinessOpportunityController extends BaseController {

    private final IBusinessOpportunityService businessOpportunityService;
    private final CustomerMapper customerMapper;

    @ApiOperation(value = "分页（带条件）-实时商机数据")
    @PostMapping("/getQlmRealTimeList")
    public AjaxResult getQlmRealTimeList(@RequestBody BusinessOpportunityRequestParam param) {
        log.info("实时商机数据");
//        return success(businessOpportunityService.getQlmRealTimeList(queryPage));
        return success(businessOpportunityService.getQlmRealTimeList(param));
    }

    @ApiOperation(value = "分页（带条件）-实时商机数据")
    @PostMapping("/getQlmStockList")
    public AjaxResult getQlmStockList(@RequestBody BusinessOpportunityRequestParam param) {
        log.info("实时商机数据");
        return  success(businessOpportunityService.getQlmStockList(param));
    }

    @ApiOperation(value = "分页（带条件）-实时商机明细")
    @GetMapping("/getQlmRealTimeDetail/{id}/{lockStatus}")
    public AjaxResult getQlmRealTimeDetail(@PathVariable("id") String id,@PathVariable("lockStatus")String lockStatus) {
        log.info("实时商机数据");

        return success(businessOpportunityService.getQlmRealTimeDetail(id,lockStatus));
    }

    @ApiOperation(value = "分页（带条件）-存量商机明细")
    @GetMapping("/getQlmStockDetail/{id}/{lockStatus}")
    public AjaxResult getQlmStockDetail(@PathVariable("id") String id,@PathVariable("lockStatus")String lockStatus) {
        log.info("实时商机数据");
        ;
        return success(businessOpportunityService.getQlmStockDetail(id,lockStatus));
    }


    @ApiOperation(value = "获取千里马接口单位信息数据")
    @GetMapping("/getQlmCompany/{name}")
    public AjaxResult getQlmCompany(@PathVariable("name") String name) {
        log.info("实时商机数据");
//        businessOpportunityService.getQlmCompany(name);
        return success(businessOpportunityService.getQlmCompany(name));
    }


    @ApiOperation(value = "保存千里马接口单位信息数据")
    @GetMapping("/saveQlmCompanyCustomer/{name}")
    public AjaxResult saveQlmCompanyCustomer(@PathVariable("name") String name) {
        log.info("实时商机数据");
        Customer customer= businessOpportunityService.saveQlmCompanyCustomer(name);

        return success(customer);
    }


    @ApiOperation(value = "获取千里马接口获取token")
    @GetMapping("/getQlmToken/{dataId}")
    public AjaxResult getQlmToken(@PathVariable("dataId") String dataId) {
        log.info("获取千里马接口获取token");
         String userCode="";
        return success(businessOpportunityService.getQlmToken(userCode,dataId));
    }


    /**
     * 单位是否已经解锁
     *
     * @return true/false
     */
    @ApiOperation(value = "单位是否已经解锁")
    @GetMapping("/isUnLock/{company}")
    public AjaxResult isLock(@PathVariable("company") String company){
        return success(businessOpportunityService.isUnLock(company ));
    }
    /**
     * 获取解锁统计
     *
     * @return
     */
    @ApiOperation(value = "获取解锁统计")
    @GetMapping("/getUnlockData")
    public AjaxResult getUnlockData(){
        return success(businessOpportunityService.getUnlockData());
    }

    @ApiOperation(value = "详情-商机数据")
    @GetMapping("/get/{id}")
//    @RequiresPermissions("biz:businessOpportunity:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(businessOpportunityService.getById(id));
    }


    /**
     * 获取千里马省行政图
     *
     * @return
     */
    @ApiOperation(value = "获取千里马省行政图（type:Province，City，空返回全部）")
    @GetMapping("/getQlmAreaList/{type}")
    public AjaxResult getQlmAreaList(@PathVariable("type") String type){
        return success(businessOpportunityService.getQlmAreaList( type));
    }

    /**
     * 获取千里马省行政图
     *
     * @return
     */
    @ApiOperation(value = "获取千里马省行政图（parentId:0,返回省份，其他传父级，空：返回全部）")
    @GetMapping("/getQlmAreaListByParentId/{parentId}")
    public AjaxResult getQlmAreaListByParentId(@PathVariable("parentId")String parentId){
        return success(businessOpportunityService.getQlmAreaListByParentId( parentId));
    }

    @ApiOperation(value = "获取千里马省行政图")
    @PostMapping("/getQlmAreaListByParentIds")
    public AjaxResult SetIndicatorDisplayLevel(@RequestBody List<String> ids) {
        return success(businessOpportunityService.getQlmAreaListByParentIds( ids));
    }

    @ApiOperation(value = "分页（带条件）-商机数据")
    @PostMapping("/page")
//    @RequiresPermissions("biz:businessOpportunity:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(businessOpportunityService.page(queryPage));
    }

}
