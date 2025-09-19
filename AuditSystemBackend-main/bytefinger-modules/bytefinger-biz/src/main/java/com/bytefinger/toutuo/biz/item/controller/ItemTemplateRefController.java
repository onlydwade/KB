package com.bytefinger.toutuo.biz.item.controller;

import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.item.service.IItemTemplateRefService;
import com.bytefinger.toutuo.biz.item.domain.ItemTemplateRef;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;

/**
 * <p>
 * 审计模版关系表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-05
 */
@Slf4j
@Api(tags = "审计模版关系表")
@AllArgsConstructor
@RestController
@RequestMapping("/itemTemplateRef")
public class ItemTemplateRefController extends BaseController {
    private final IItemTemplateRefService itemTemplateRefService;

    @ApiOperation(value = "新增-审计模版关系表")
    @PutMapping("/add")
    @RequiresPermissions("biz:itemTemplateRef:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ItemTemplateRef itemTemplateRef) {
        return success(itemTemplateRefService.save(itemTemplateRef));
    }

    @ApiOperation(value = "修改-审计模版关系表")
    @PutMapping("/update")
    @RequiresPermissions("biz:itemTemplateRef:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ItemTemplateRef itemTemplateRef) {
        return success(itemTemplateRefService.updateById(itemTemplateRef));
    }

    @ApiOperation(value = "删除-审计模版关系表")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:itemTemplateRef:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(itemTemplateRefService.removeById(id));
    }

    @ApiOperation(value = "详情-审计模版关系表")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:itemTemplateRef:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(itemTemplateRefService.getById(id));
    }

    @ApiOperation(value = "列表-审计模版关系表")
    @GetMapping("/list")
    @RequiresPermissions("biz:itemTemplateRef:list")
    public AjaxResult list() {
        return success(itemTemplateRefService.list());
    }

    @ApiOperation(value = "分页（带条件）-审计模版关系表")
    @PostMapping("/page")
    @RequiresPermissions("biz:itemTemplateRef:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(itemTemplateRefService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
