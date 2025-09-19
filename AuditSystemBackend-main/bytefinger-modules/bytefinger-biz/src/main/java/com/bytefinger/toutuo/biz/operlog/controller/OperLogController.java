package com.bytefinger.toutuo.biz.operlog.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.Logical;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.operlog.service.IOperLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 业务数据变更记录表 前端控制器
 * </p>
 *
 */
@Slf4j
@Api(tags = {"变更记录"})
@AllArgsConstructor
@RestController
@RequestMapping("/operLog")
public class OperLogController extends BaseController {

    private final IOperLogService operLogService;

    @ApiOperation(value = "列表-变更记录", notes = "moduleName={Lead,Opportunity,Project,Customer}")
    @GetMapping("/list/{moduleName}/{recordId}")
    @RequiresPermissions(value = {"biz:project:operLog:list", "biz:customer:operLog:list"}, logical = Logical.OR)
    public AjaxResult list(@PathVariable("moduleName") String moduleName, @PathVariable("recordId") Long recordId) {
        return success(operLogService.listByModuleAndRecordId(moduleName, recordId));
    }

    @ApiOperation(value = "分页（带条件）-变更记录")
    @PostMapping("/page/{moduleName}/{recordId}")
    @RequiresPermissions(value = {"biz:project:operLog:list", "biz:customer:operLog:list"}, logical = Logical.OR)
    public AjaxResult page(@PathVariable("moduleName") String moduleName, @PathVariable("recordId") Long recordId, @RequestBody QueryPage queryPage) {
        queryPage.getParams().put("module_name", moduleName);
        queryPage.getParams().put("record_id", recordId);
        return success(operLogService.pageData(queryPage.toPage(), queryPage.getWrapper()));
    }

}
