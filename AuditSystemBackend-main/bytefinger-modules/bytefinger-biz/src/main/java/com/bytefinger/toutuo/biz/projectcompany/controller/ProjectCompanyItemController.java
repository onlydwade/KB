package com.bytefinger.toutuo.biz.projectcompany.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.toutuo.api.biz.user.domain.IntMainDept;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.biz.project.domain.ProjectPool;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyItem;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyItemService;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 子项目 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-22
 */
@Slf4j
@Api(tags = "子项目")
@AllArgsConstructor
@RestController
@RequestMapping("/projectCompanyItem")
public class ProjectCompanyItemController extends BaseController {
    private final IProjectCompanyService projectCompanyService;
    private final IProjectCompanyItemService projectCompanyItemService;

    @ApiOperation(value = "新增-子项目")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyItem projectCompanyItem) {
        projectCompanyItemService.save(projectCompanyItem);
        return success(projectCompanyItem);
    }

    @ApiOperation(value = "修改-子项目")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyItem projectCompanyItem) {
        return success(projectCompanyItemService.updateById(projectCompanyItem));
    }

    @ApiOperation(value = "删除-子项目")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyItemService.removeById(id));
    }

    @ApiOperation(value = "详情-子项目")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyItemService.getById(id));
    }

    @ApiOperation(value = "列表-子项目")
    @GetMapping("/list/{projectId}")
    public AjaxResult list(@PathVariable("projectId") Long projectId) {
        List<ProjectCompanyItem> projectCompanyItems = projectCompanyItemService.list(Wrappers.<ProjectCompanyItem>lambdaQuery().eq(ProjectCompanyItem::getProjectId, projectId)
                .orderByDesc(ProjectCompanyItem::getCreateTime));
        if(CollectionUtils.isEmpty(projectCompanyItems)){
            return success();
        }
        List<Long> companyIds = projectCompanyItems.stream().map(ProjectCompanyItem::getCompanyId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(companyIds)) {
            List<ProjectCompany> list = projectCompanyService.list(Wrappers.<ProjectCompany>lambdaQuery().in(ProjectCompany::getId, companyIds));

            if (!list.isEmpty()) {
                for (ProjectCompanyItem record : projectCompanyItems) {
                    for (ProjectCompany company : list) {
                        if (company.getId().equals(record.getCompanyId())) {
                            record.setCompanyName(company.getName());
                            record.setCompanyNo(company.getCompanyNo());
                        }
                    }
                }
            }
        }
        List<Long> sonCompanyIds = projectCompanyItems.stream().map(ProjectCompanyItem::getSonCompanyId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(sonCompanyIds)) {
            List<ProjectCompany> list = projectCompanyService.list(Wrappers.<ProjectCompany>lambdaQuery().in(ProjectCompany::getId, sonCompanyIds));

            if (!list.isEmpty()) {
                for (ProjectCompanyItem record : projectCompanyItems) {
                    for (ProjectCompany company : list) {
                        if (company.getId().equals(record.getSonCompanyId())) {
                            record.setSonCompanyName(company.getName());
                            record.setCompanyNo(company.getCompanyNo());
                        }
                    }
                }
            }
        }
        return success(projectCompanyItems);
    }

    @ApiOperation(value = "分页（带条件）-子项目")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        Page<ProjectCompanyItem> page = projectCompanyItemService.page(queryPage.toPage(), queryPage.getWrapper());
        List<Long> companyIds = page.getRecords().stream().map(ProjectCompanyItem::getCompanyId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(companyIds)) {
            List<ProjectCompany> list = projectCompanyService.list(Wrappers.<ProjectCompany>lambdaQuery().in(ProjectCompany::getId, companyIds));

            if (!list.isEmpty()) {
                for (ProjectCompanyItem record : page.getRecords()) {
                    for (ProjectCompany company : list) {
                        if (company.getId().equals(record.getCompanyId())) {
                            record.setCompanyName(company.getName());
                            record.setCompanyNo(company.getCompanyNo());
                        }
                    }
                }
            }
        }
        List<Long> sonCompanyIds = page.getRecords().stream().map(ProjectCompanyItem::getSonCompanyId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(sonCompanyIds)) {
            List<ProjectCompany> list = projectCompanyService.list(Wrappers.<ProjectCompany>lambdaQuery().in(ProjectCompany::getId, sonCompanyIds));

            if (!list.isEmpty()) {
                for (ProjectCompanyItem record : page.getRecords()) {
                    for (ProjectCompany company : list) {
                        if (company.getId().equals(record.getSonCompanyId())) {
                            record.setSonCompanyName(company.getName());
                            record.setCompanyNo(company.getCompanyNo());
                        }
                    }
                }
            }
        }
        return success(page);
    }

}
