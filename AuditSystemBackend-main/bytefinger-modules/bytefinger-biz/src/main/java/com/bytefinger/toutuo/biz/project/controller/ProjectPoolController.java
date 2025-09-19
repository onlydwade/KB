package com.bytefinger.toutuo.biz.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.domain.ProjectEntrust;
import com.bytefinger.toutuo.biz.project.domain.ProjectPool;
import com.bytefinger.toutuo.biz.project.service.IProjectPoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 项目池 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-01
 */
@Slf4j
@Api(tags = "项目池")
@AllArgsConstructor
@RestController
@RequestMapping("/projectPool")
public class ProjectPoolController extends BaseController {
    private final IProjectPoolService projectPoolService;

    @ApiOperation(value = "新增-项目池")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectPool:add")
    public AjaxResult add(@RequestBody ProjectPool projectPool) {
        projectPoolService.save(projectPool);
        return success(projectPool);
    }

    @ApiOperation(value = "修改-项目池")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectPool:update")
    public AjaxResult update(@RequestBody ProjectPool projectPool) {
        projectPoolService.updateById(projectPool);
        return success(projectPool);
    }

    @ApiOperation(value = "删除-项目池")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectPool:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectPoolService.removeById(id));
    }

    @ApiOperation(value = "详情-项目池")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectPool:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectPoolService.getById(id));
    }

    @ApiOperation(value = "列表-项目池")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectPool:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId) {
        return success(projectPoolService.list(Wrappers.<ProjectPool>lambdaQuery().eq(ProjectPool::getProjectId, projectId)
                .orderByDesc(ProjectPool::getCreateTime)));
    }
    @ApiOperation(value = "分页（带条件）-项目池")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectPool:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectPoolService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
