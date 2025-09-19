package com.bytefinger.toutuo.biz.followlog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.Logical;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.followlog.domain.BizFollowLog;
import com.bytefinger.toutuo.biz.followlog.domain.FollowLogListVo;
import com.bytefinger.toutuo.biz.followlog.service.FollowLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 客户跟踪记录 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-25
 */
@Slf4j
@Api(tags = {"跟踪记录"})
@AllArgsConstructor
@RestController
@RequestMapping("/followLog")
public class FollowLogController extends BaseController {

    private final FollowLogService followLogService;

    @ApiOperation(value = "新增-跟踪记录")
    @PutMapping("/add")
    @RequiresPermissions(value = {"biz:project:followLog:add", "biz:customer:followLog:add"}, logical = Logical.OR)
    public AjaxResult add(@RequestBody @Validated BizFollowLog bizFollowLog) {
        return success(followLogService.add(bizFollowLog));
    }

    @ApiOperation(value = "新增-跟踪记录-客户")
    @PutMapping("/addCustomerLog")
    @RequiresPermissions(value = {"biz:project:followLog:add", "biz:customer:followLog:add"}, logical = Logical.OR)
    public AjaxResult addCustomerLog(@RequestBody @Validated BizFollowLog bizFollowLog) {
        return success(followLogService.addCustomerLog(bizFollowLog));
    }
    @ApiOperation(value = "删除-客户跟踪记录")
    @DeleteMapping("/deleteCustomerLogByIds/{modelName}/{recordId}/{ids}")
    @RequiresPermissions(value = {"biz:project:followLog:delete", "biz:customer:followLog:delete"}, logical = Logical.OR)
    public AjaxResult deleteCustomerLogByIds(@PathVariable("modelName") String modelName, @PathVariable("recordId") Long recordId, @PathVariable("ids") String ids) {
        followLogService.deleteCustomerLogByIds(modelName, ids);
        return success(true);
    }

    @ApiOperation(value = "修改-跟踪记录")
    @PutMapping("/update")
    @RequiresPermissions(value = {"biz:project:followLog:update", "biz:customer:followLog:update"}, logical = Logical.OR)
    public AjaxResult update(@RequestBody @Validated BizFollowLog bizFollowLog) {
        return success(followLogService.update(bizFollowLog));
    }

    @ApiOperation(value = "删除-客户跟踪记录")
    @DeleteMapping("/delete/{modelName}/{recordId}/{ids}")
    @RequiresPermissions(value = {"biz:project:followLog:delete", "biz:customer:followLog:delete"}, logical = Logical.OR)
    public AjaxResult deleteByIds(@PathVariable("modelName") String modelName, @PathVariable("recordId") Long recordId, @PathVariable("ids") String ids) {
        followLogService.deleteByIds(modelName, ids);
        return success(true);
    }

    @ApiOperation(value = "详情-客户跟踪记录")
    @GetMapping("/get/{modelName}/{id}")
    @RequiresPermissions(value = {"biz:project:followLog:get", "biz:customer:followLog:get"}, logical = Logical.OR)
    public AjaxResult get(@PathVariable("modelName") String modelName, @PathVariable("id") Long id) {
        return success(followLogService.getById(modelName, id));
    }

    @ApiOperation(value = "分页（带条件）-客户跟踪记录")
    @PostMapping("/page/{modelName}/{recordId}")
    @RequiresPermissions(value = {"biz:project:followLog:list", "biz:customer:followLog:list"}, logical = Logical.OR)
    public AjaxResult page(@PathVariable("modelName") String modelName, @PathVariable("recordId") Long recordId, @RequestBody QueryPage queryPage) {
        queryPage.getParams().put("record_id", recordId);
        return success(followLogService.page(modelName, queryPage));
    }



    @ApiOperation(value = "分页（带条件）-客户跟踪记录")
    @PostMapping("/followList/{modelName}/{recordId}")
    @RequiresPermissions(value = {"biz:project:followLog:list", "biz:customer:followLog:list"}, logical = Logical.OR)
    public AjaxResult getfollowList(@PathVariable("modelName") String modelName, @PathVariable("recordId") Long recordId, @RequestBody QueryPage queryPage) {
        queryPage.getParams().put("record_id", recordId);
        return success(followLogService.getfollowList(modelName, queryPage));
    }
}
