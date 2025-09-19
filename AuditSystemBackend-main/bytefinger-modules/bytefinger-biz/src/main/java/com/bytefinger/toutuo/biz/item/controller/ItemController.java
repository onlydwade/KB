package com.bytefinger.toutuo.biz.item.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.api.system.file.domain.SysFile;
import com.bytefinger.toutuo.biz.audit.domain.Audit;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocument;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocumentTemplate;
import com.bytefinger.toutuo.biz.item.domain.ItemCriteria;
import com.bytefinger.toutuo.biz.item.service.IItemCriteriaService;
import com.bytefinger.toutuo.biz.item.service.IItemService;
import com.bytefinger.toutuo.biz.item.domain.Item;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.project.bo.ProjectAchievementBO;
import com.bytefinger.toutuo.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 审计项目 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-05
 */
@Slf4j
@Api(tags = "审计项目")
@AllArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController extends BaseController {

    private final IItemService itemService;

    private final IItemCriteriaService itemCriteriaService;

    @ApiOperation(value = "新增-审计项目")
    @PutMapping("/add")
    @RequiresPermissions("biz:item:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody Item item) {
        itemService.add(item);
        return success();
    }

    @ApiOperation(value = "修改-审计项目")
    @PutMapping("/update")
    @RequiresPermissions("biz:item:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody Item item) {
        return success(itemService.updateById(item));
    }

    @ApiOperation(value = "删除-审计项目")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:item:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(itemService.removeById(id));
    }

    @ApiOperation(value = "详情-审计项目")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:item:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(itemService.getById(id));
    }

    @ApiOperation(value = "列表-审计项目")
    @GetMapping("/list")
    @RequiresPermissions("biz:item:list")
    public AjaxResult list() {
        return success(itemService.list());
    }

    @ApiOperation(value = "分页（带条件）-审计项目")
    @PostMapping("/page")
    @RequiresPermissions("biz:item:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(itemService.page(queryPage.toPage(), queryPage.getWrapper()));
    }


    @ApiOperation(value = "导入-审计项目")
    @PostMapping("/importItem")
    @RequiresPermissions("biz:item:import")
    @Transactional
    public AjaxResult importItem(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            com.bytefinger.toutuo.common.util.ExcelUtil<Item> excelUtil = new ExcelUtil<>(Item.class);
            List<Item> items = excelUtil.importExcel(inputStream);

            itemService.saveBatch(items);
            items.forEach(item -> {
                List<ItemCriteria> itemCriterias = item.getItemCriterias();
                itemCriterias.forEach(itemCriteria -> itemCriteria.setItemId(item.getId()));
                itemCriteriaService.saveBatch(itemCriterias);
            });


        } catch (Exception e) {
            return error("Import error");
        }

        return success();
    }

    @ApiOperation(value = "导出-审计项目")
    @PostMapping("/export")
    @RequiresPermissions("biz:item:export")
    public AjaxResult export(@RequestBody QueryPage queryPage) {
        queryPage.setPageSize(Integer.MAX_VALUE);
        Page<Item> page = itemService.page(queryPage.toPage(), queryPage.getWrapper());
        List<Item> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            records.forEach(item -> {
                List<ItemCriteria> itemCriterias = itemCriteriaService.list(Wrappers.<ItemCriteria>lambdaQuery()
                        .in(ItemCriteria::getItemId, item.getId()));
                item.setItemCriterias(itemCriterias);
            });
        }

        com.bytefinger.toutuo.common.util.ExcelUtil<Item> excelUtil = new ExcelUtil<>(Item.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), records, "Item Data");
        return success();
    }

}
