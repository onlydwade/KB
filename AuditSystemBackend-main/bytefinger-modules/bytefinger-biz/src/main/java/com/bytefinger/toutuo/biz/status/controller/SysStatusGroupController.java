package com.bytefinger.toutuo.biz.status.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.status.domain.SysStatusGroup;
import com.bytefinger.toutuo.biz.status.service.ISysStatusGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 状态机 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Slf4j
@Api(tags = "状态机")
@AllArgsConstructor
@RestController
@RequestMapping("/statusGroup")
public class SysStatusGroupController extends BaseController {
    public final ISysStatusGroupService sysStatusGroupService;

    @ApiOperation(value = "新增-状态机分组")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:statusGroup:add")
    public AjaxResult add(@RequestBody SysStatusGroup sysStatusGroup) {
        sysStatusGroupService.save(sysStatusGroup);
        return success(sysStatusGroup);
    }

    @ApiOperation(value = "修改-状态机分组")
    @PutMapping("/update/{id}")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:statusGroup:update")
    public AjaxResult update(@RequestBody SysStatusGroup sysStatusGroup) {
        sysStatusGroupService.updateById(sysStatusGroup);
        return success("更新成功");
    }

    @ApiOperation(value = "删除-状态机分组")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:statusGroup:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        sysStatusGroupService.removeById(id);
        return success(true);
    }

    @ApiOperation(value = "列表-状态机分组")
    @GetMapping("/list")
    @RequiresPermissions("biz:statusGroup:list")
    public AjaxResult list() {
        List<SysStatusGroup> sysStatusGroupList = sysStatusGroupService.list();
        return success(sysStatusGroupList);
    }

}
