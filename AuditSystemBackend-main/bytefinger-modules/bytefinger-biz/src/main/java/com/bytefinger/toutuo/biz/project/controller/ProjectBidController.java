package com.bytefinger.toutuo.biz.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectBid;
import com.bytefinger.toutuo.biz.project.service.IProjectBidService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 项目参标单位 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "项目参标单位")
@AllArgsConstructor
@RestController
@RequestMapping("/projectBid")
public class ProjectBidController extends BaseController {
    private final IProjectBidService projectBidService;

    @ApiOperation(value = "新增-项目参标单位")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectBid:add")
    public AjaxResult add(@RequestBody ProjectBid projectBid) {
        projectBidService.save(projectBid);
        return success(projectBid);
    }

    @ApiOperation(value = "修改-项目参标单位")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectBid:update")
    public AjaxResult update(@RequestBody ProjectBid projectBid) {
        projectBidService.updateById(projectBid);
        return success(projectBid);
    }

    @ApiOperation(value = "删除-项目参标单位")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectBid:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectBidService.removeById(id));
    }

    @ApiOperation(value = "详情-项目参标单位")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectBid:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectBidService.getById(id));
    }

    @ApiOperation(value = "列表-项目参标单位")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectBid:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId) {
        return success(projectBidService.list(Wrappers.<ProjectBid>lambdaQuery().eq(ProjectBid::getProjectId, projectId).orderByDesc(ProjectBid::getCreateTime)));
    }

    @ApiOperation(value = "分页（带条件）-项目参标单位")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectBid:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectBidService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
