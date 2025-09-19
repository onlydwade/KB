package com.bytefinger.toutuo.biz.status.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.status.domain.SysStatusTurn;
import com.bytefinger.toutuo.biz.status.service.ISysStatusTurnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 状态扭转 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Slf4j
@Api(tags = "状态机")
@AllArgsConstructor
@RestController
@RequestMapping("/statusTurn")
public class SysStatusTurnController extends BaseController {
    public final ISysStatusTurnService sysStatusTurnService;

    @ApiOperation(value = "新增-状态扭转")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:statusTurn:add")
    public AjaxResult add(@RequestBody SysStatusTurn sysStatusTurn) {
        sysStatusTurnService.save(sysStatusTurn);
        return success(sysStatusTurn);
    }

    @ApiOperation(value = "修改-状态扭转")
    @PutMapping("/update/{id}")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:statusTurn:update")
    public AjaxResult update(@RequestBody SysStatusTurn sysStatusTurn) {
        sysStatusTurnService.updateById(sysStatusTurn);
        return success("更新成功");
    }

    @ApiOperation(value = "删除-状态扭转")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:statusTurn:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        sysStatusTurnService.removeById(id);
        return success(true);
    }

    @ApiOperation(value = "详情-状态扭转")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:statusTurn:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        SysStatusTurn sysStatusTurn = sysStatusTurnService.getById(id);
        return success(sysStatusTurn);
    }

    @ApiOperation(value = "列表-状态扭转")
    @GetMapping("/list")
    @RequiresPermissions("biz:statusTurn:list")
    public AjaxResult list() {
        List<SysStatusTurn> sysStatusTurnList = sysStatusTurnService.list();
        return success(sysStatusTurnList);
    }

    @ApiOperation(value = "分页（带条件）-状态扭转")
    @PostMapping("/page")
    @RequiresPermissions("biz:statusTurn:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        IPage<SysStatusTurn> page = sysStatusTurnService.page(queryPage.toPage(), queryPage.getWrapper());
        return success(page);
    }

}
