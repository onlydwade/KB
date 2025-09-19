package com.bytefinger.toutuo.biz.audit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.audit.domain.Audit;
import com.bytefinger.toutuo.biz.audit.service.IAuditService;
import com.bytefinger.toutuo.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审计任务管理 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-04
 */
@Slf4j
@Api(tags = "审计任务管理")
@AllArgsConstructor
@RestController
@RequestMapping("/audit")
public class AuditController extends BaseController {

    private final IAuditService auditService;

    @ApiOperation(value = "新增-审计任务管理")
    @PutMapping("/add")
    @RequiresPermissions("biz:audit:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody Audit audit) {
        return success(auditService.add(audit));
    }

    @ApiOperation(value = "修改-审计任务管理")
    @PutMapping("/update")
    @RequiresPermissions("biz:audit:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody Audit audit) {
        return success(auditService.update(audit));
    }

    @ApiOperation(value = "删除-审计任务管理")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:audit:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(auditService.removeById(id));
    }

    @ApiOperation(value = "详情-审计任务管理")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:audit:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditService.getById(id));
    }

    @ApiOperation(value = "列表-审计任务管理")
    @GetMapping("/list")
    @RequiresPermissions("biz:audit:list")
    public AjaxResult list() {
        return success(auditService.list());
    }

    @ApiOperation(value = "分页（带条件）-审计任务管理")
    @PostMapping("/page")
    @RequiresPermissions("biz:audit:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        IPage<Audit> page = auditService.page(queryPage);
        return success(page);
    }

    @ApiOperation(value = "报告查询-审计任务管理")
    @GetMapping("/report/{id}")
    @RequiresPermissions("biz:audit:get")
    public AjaxResult report(@PathVariable("id") Long id) {
        return success(auditService.getReport(id));
    }

    @ApiOperation(value = "导出-审计数据")
    @PostMapping("/export")
    @RequiresPermissions("biz:audit:export")
    public void export(@RequestBody QueryPage queryPage) {
//        LoginUser loginUser = SecurityUtils.getLoginUser();
//
//        String postCode = loginUser.getPostCode();
//        if (StringUtils.isNotEmpty(postCode) &&
//                postCode.equals("GONG_SHANG_ZHU_CE_/_BIAN_GENG_WEI_HU_YUAN")) {
//            queryPage.getParams().put("projectType", "GU_QUAN_HE_ZUO_XIANG_MU");
//        }

        queryPage.setPageSize(Integer.MAX_VALUE);
        IPage<Audit> page = auditService.page(queryPage);
        com.bytefinger.toutuo.common.util.ExcelUtil<Audit> excelUtil = new ExcelUtil<>(Audit.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), page.getRecords(), "审计任务数据");
    }

}
