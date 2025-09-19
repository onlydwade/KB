package com.bytefinger.toutuo.biz.item.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.item.domain.ItemCriteria;
import com.bytefinger.toutuo.biz.item.service.IItemCriteriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审计项目标准表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-07
 */
@Slf4j
@Api(tags = "审计项目标准表")
@AllArgsConstructor
@RestController
@RequestMapping("/itemCriteria")
public class ItemCriteriaController extends BaseController {
    private final IItemCriteriaService itemCriteriaService;

    @ApiOperation(value = "新增-审计项目标准表")
    @PutMapping("/add")
    @RequiresPermissions("biz:itemCriteria:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ItemCriteria itemCriteria) {
        return success(itemCriteriaService.save(itemCriteria));
    }

    @ApiOperation(value = "修改-审计项目标准表")
    @PutMapping("/update")
    @RequiresPermissions("biz:itemCriteria:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ItemCriteria itemCriteria) {
        return success(itemCriteriaService.updateById(itemCriteria));
    }

    @ApiOperation(value = "删除-审计项目标准表")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:itemCriteria:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(itemCriteriaService.removeById(id));
    }

    @ApiOperation(value = "详情-审计项目标准表")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:itemCriteria:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(itemCriteriaService.getById(id));
    }

    @ApiOperation(value = "列表-审计项目标准表")
    @GetMapping("/list")
    @RequiresPermissions("biz:itemCriteria:list")
    public AjaxResult list() {
        return success(itemCriteriaService.list());
    }

    @ApiOperation(value = "分页（带条件）-审计项目标准表")
    @PostMapping("/page")
    @RequiresPermissions("biz:itemCriteria:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(itemCriteriaService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
