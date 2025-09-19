package com.bytefinger.toutuo.biz.rules.controller;

import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.rules.domain.Rules;
import com.bytefinger.toutuo.biz.rules.service.IRulesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 规则管理 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-11-01
 */
@Slf4j
@Api(tags = "规则管理")
@AllArgsConstructor
@RestController
@RequestMapping("/rules")
public class RulesController extends BaseController {

    private final IRulesService rulesService;

    @ApiOperation(value = "新增-规则管理")
    @PutMapping("/add")
    @RequiresPermissions("biz:rules:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody @Validated Rules rules) {
        return success(rulesService.add(rules));
    }

    @ApiOperation(value = "修改-规则管理")
    @PutMapping("/update")
    @RequiresPermissions("biz:rules:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody @Validated Rules rules) {
        return success(rulesService.update(rules));
    }

    @ApiOperation(value = "删除-规则管理")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:rules:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(rulesService.removeById(id));
    }

    @ApiOperation(value = "详情-规则管理")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:rules:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(rulesService.getById(id));
    }

    @ApiOperation(value = "分页（带条件）-规则管理")
    @PostMapping("/page")
    @RequiresPermissions("biz:rules:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(rulesService.page(queryPage));
    }

    @ApiOperation(value = "规则管理枚举{GUI_ZE_CHU_LI_DUI_XIANG=规则对象类型, GUI_ZE_FA_SONG_QU_DAO=发送渠道（可多选）, GUI_ZE_FU_HAO=规则符号，SHI_JIAN_ZHOU_QI=时间周期}")
    @GetMapping("/rulesEnums")
    @RequiresPermissions("biz:rules:list")
    public AjaxResult rulesEnums() {
        return AjaxResult.success(rulesService.rulesEnums());
    }

    @ApiOperation(value = "", hidden = true)
    @PutMapping("/rulesTask")
    @InnerAuth
    public R rulesTask() {
        rulesService.ruleTask();
        return R.ok();
    }
}
