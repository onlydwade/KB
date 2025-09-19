package com.bytefinger.toutuo.biz.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.domain.ProjectContract;
import com.bytefinger.toutuo.biz.project.domain.ProjectEntrust;
import com.bytefinger.toutuo.biz.project.service.IProjectEntrustService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 项目委托 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "项目委托")
@AllArgsConstructor
@RestController
@RequestMapping("/projectEntrust")
public class ProjectEntrustController extends BaseController {
    private final IProjectEntrustService projectEntrustService;

    @ApiOperation(value = "新增-项目委托")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectEntrust:add")
    public AjaxResult add(@RequestBody ProjectEntrust projectEntrust) {
        projectEntrustService.save(projectEntrust);
        return success(projectEntrustService);
    }

    @ApiOperation(value = "修改-项目委托")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectEntrust:update")
    public AjaxResult update(@RequestBody ProjectEntrust projectEntrust) {
        projectEntrustService.updateById(projectEntrust);
        return success(projectEntrust);
    }

    @ApiOperation(value = "删除-项目委托")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectEntrust:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectEntrustService.removeById(id));
    }

    @ApiOperation(value = "详情-项目委托")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectEntrust:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectEntrustService.getById(id));
    }

    @ApiOperation(value = "列表-项目委托")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectEntrust:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId) {
        return success(projectEntrustService.list(Wrappers.<ProjectEntrust>lambdaQuery().eq(ProjectEntrust::getProjectId, projectId)
                .orderByDesc(ProjectEntrust::getCreateTime)));
    }
    @ApiOperation(value = "分页（带条件）-项目委托")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectEntrust:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectEntrustService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
