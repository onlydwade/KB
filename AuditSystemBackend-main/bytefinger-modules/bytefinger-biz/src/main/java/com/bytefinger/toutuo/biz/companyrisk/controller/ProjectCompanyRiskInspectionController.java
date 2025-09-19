package com.bytefinger.toutuo.biz.companyrisk.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.toutuo.biz.companyrisk.service.IProjectCompanyRiskInspectionService;
import com.bytefinger.toutuo.biz.companyrisk.domain.ProjectCompanyRiskInspection;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 子公司-风险检查 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "子公司-风险检查")
@AllArgsConstructor
@RestController
@RequestMapping("/companyRiskInspection")
public class ProjectCompanyRiskInspectionController extends BaseController {
    private final IProjectCompanyRiskInspectionService projectCompanyRiskInspectionService;

    @ApiOperation(value = "新增-子公司-风险检查")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyRiskInspection projectCompanyRiskInspection) {
        projectCompanyRiskInspectionService.save(projectCompanyRiskInspection);
        return success(projectCompanyRiskInspection);
    }

    @ApiOperation(value = "修改-子公司-风险检查")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyRiskInspection projectCompanyRiskInspection) {
        return success(projectCompanyRiskInspectionService.updateById(projectCompanyRiskInspection));
    }

    @ApiOperation(value = "删除-子公司-风险检查")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyRiskInspectionService.removeById(id));
    }

    @ApiOperation(value = "详情-子公司-风险检查")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        return success(projectCompanyRiskInspectionService.getById(id));
    }

    @ApiOperation(value = "列表-子公司-风险检查")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyRiskInspectionService.list());
    }

    @ApiOperation(value = "分页（带条件）-子公司-风险检查")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        Page<ProjectCompanyRiskInspection> page = projectCompanyRiskInspectionService.page(queryPage);
        if (ObjectUtil.isNotEmpty(page) && ObjectUtil.isNotEmpty(page.getRecords())){
            List<ProjectCompanyRiskInspection> records = page.getRecords();
            for (ProjectCompanyRiskInspection record : records) {
               if (ObjectUtil.isNotEmpty(record) && ObjectUtil.isNotEmpty(record.getMilepostTime())){
                   long l = (record.getMilepostTime().getTime() - new Date().getTime() ) / (24 * 60 * 60 * 1000);
                   if (l< 0){
                       record.setExpired("已过期");
                   }
               }

            }


        }

        return success(page);
    }

    @ApiOperation(value = "", hidden = true)
    @PutMapping("/sendNotifyTask")
    @InnerAuth
    public R sendNotifyTask() {
        projectCompanyRiskInspectionService.sendNotifyTask();
        return R.ok();
    }

}
