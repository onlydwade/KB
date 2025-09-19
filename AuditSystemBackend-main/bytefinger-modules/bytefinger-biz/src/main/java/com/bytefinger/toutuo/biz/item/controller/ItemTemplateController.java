package com.bytefinger.toutuo.biz.item.controller;

import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.item.service.IItemTemplateService;
import com.bytefinger.toutuo.biz.item.domain.ItemTemplate;
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
 * 审计模版 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-05
 */
@Slf4j
@Api(tags = "审计模版")
@AllArgsConstructor
@RestController
@RequestMapping("/itemTemplate")
public class ItemTemplateController extends BaseController {
    private final IItemTemplateService itemTemplateService;

    @ApiOperation(value = "新增-审计模版")
    @PutMapping("/add")
    @RequiresPermissions("biz:itemTemplate:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ItemTemplate itemTemplate) {
        return success(itemTemplateService.save(itemTemplate));
    }

    @ApiOperation(value = "修改-审计模版")
    @PutMapping("/update")
    @RequiresPermissions("biz:itemTemplate:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ItemTemplate itemTemplate) {
        return success(itemTemplateService.updateById(itemTemplate));
    }

    @ApiOperation(value = "删除-审计模版")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:itemTemplate:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(itemTemplateService.removeById(id));
    }

    @ApiOperation(value = "详情-审计模版")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:itemTemplate:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(itemTemplateService.getById(id));
    }

    @ApiOperation(value = "列表-审计模版")
    @GetMapping("/list")
    @RequiresPermissions("biz:itemTemplate:list")
    public AjaxResult list() {
        return success(itemTemplateService.list());
    }

    @ApiOperation(value = "分页（带条件）-审计模版")
    @PostMapping("/page")
    @RequiresPermissions("biz:itemTemplate:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(itemTemplateService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
