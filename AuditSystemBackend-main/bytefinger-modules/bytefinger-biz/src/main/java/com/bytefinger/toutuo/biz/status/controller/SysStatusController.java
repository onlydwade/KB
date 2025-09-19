package com.bytefinger.toutuo.biz.status.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.status.domain.SysStatus;
import com.bytefinger.toutuo.biz.status.service.ISysStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 状态配置基础表 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Slf4j
@Api(tags = "状态机")
@AllArgsConstructor
@RestController
@RequestMapping("/status")
public class SysStatusController extends BaseController {
    public final ISysStatusService sysStatusService;

    @ApiOperation(value = "新增-状态配置基础表")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:status:add")
    public AjaxResult add(@RequestBody SysStatus sysStatus) {
        sysStatusService.save(sysStatus);
        return success(sysStatus);
    }

    @ApiOperation(value = "修改-状态配置基础表")
    @PutMapping("/update/{id}")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:status:update")
    public AjaxResult update(@RequestBody SysStatus sysStatus) {
        sysStatusService.updateById(sysStatus);
        return success("更新成功");
    }

    @ApiOperation(value = "删除-状态配置基础表")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:status:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        sysStatusService.removeById(id);
        return success(true);
    }

    @ApiOperation(value = "列表-状态配置基础表")
    @GetMapping("/list/{groupId}")
    @RequiresPermissions("biz:status:list")
    public AjaxResult list(@PathVariable("groupId") Integer groupId) {
        return success(sysStatusService.listByGroupId(groupId));
    }

}
