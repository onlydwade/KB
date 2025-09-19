package com.bytefinger.toutuo.biz.projectextension.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectOperation;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectOperationService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCollectionRateVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectOperationVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 拓后运营管理
 *
 * @author ycj
 * @email 
 * @date 2023-03-14 11:05:23
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectOperation")
public class ProjectOperationController extends BaseController {
    @Autowired
    private IProjectOperationService projectOperationService;

    @ApiOperation(value = "修改-运营管理基础")
    @PostMapping("/update")
    @RequiresPermissions("biz:projectOperation:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectOperation projectOperation) {
        projectOperationService.update(projectOperation);
        return success(projectOperation);
    }


    @ApiOperation(value = "添加-运营管理基础")
    @PostMapping("/add")
    @RequiresPermissions("biz:projectOperation:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectOperation projectOperation) {
        projectOperationService.add(projectOperation);
        return success(projectOperation);
    }

    @ApiOperation(value = "查询-运营管理基础")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectOperation:get")
    public AjaxResult get(@PathVariable("id") Long id) {
        ProjectOperationVo projectOperationVo =  projectOperationService.get(id);
        return success(projectOperationVo);
    }

    @ApiOperation(value = "立项收缴率查询")
    @GetMapping("/collectionRateMessage/{projectId}")
    @RequiresPermissions("biz:projectOperation:collectionRateMessage")
    public AjaxResult collectionRateMessage(@PathVariable("projectId") Long projectId) {
        ProjectCollectionRateVo projectCollectionRateVo = projectOperationService.collectionRateMessage(projectId);
        List<ProjectCollectionRateVo>  projectCollectionRateVos = Lists.newArrayList(projectCollectionRateVo);
        return success(projectCollectionRateVos);
    }


}
