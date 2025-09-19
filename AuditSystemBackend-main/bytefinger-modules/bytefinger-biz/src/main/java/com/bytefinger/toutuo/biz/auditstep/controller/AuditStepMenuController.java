package com.bytefinger.toutuo.biz.auditstep.controller;

import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.auditstep.domain.AuditStepMenu;
import com.bytefinger.toutuo.biz.auditstep.service.IAuditStepMenuService;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepMenuService;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenu;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 审计步骤菜单 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "审计步骤菜单")
@AllArgsConstructor
@RestController
@RequestMapping("/auditStepMenu")
public class AuditStepMenuController extends BaseController {

    private final IAuditStepMenuService auditStepMenuService;

    @ApiOperation(value = "详情-审计步骤菜单")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditStepMenu:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditStepMenuService.getById(id));
    }

    @ApiOperation(value = "全部-获取审计步骤树")
    @GetMapping("/tree")
    @RequiresPermissions("biz:auditStepMenu:list")
    public AjaxResult tree() {
        return AjaxResult.success(auditStepMenuService.tree(Wrappers.<AuditStepMenu>lambdaQuery().orderByAsc(AuditStepMenu::getSorts)));
    }

    @ApiOperation(value = "单节点-获取审计步骤树")
    @GetMapping("/treeById/{id}")
    @RequiresPermissions("biz:auditStepMenu:list")
    public AjaxResult treeById(@PathVariable("id") Integer id) {
        return AjaxResult.success(auditStepMenuService.tree(id));
    }

    @ApiOperation(value = "修改-审计节点步骤")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:auditStepMenu:update")
    public AjaxResult update(@RequestBody AuditStepMenu auditStepMenu) {
        auditStepMenuService.updateById(auditStepMenu);
        return success(auditStepMenu);
    }

//    @ApiOperation(value = "分页（带条件）-审计步骤菜单")
//    @PostMapping("/page")
//    public AjaxResult page(@RequestBody QueryPage queryPage) {
//        return success(projectStepMenuService.page(queryPage.toPage(), queryPage.getWrapper()));
//    }

}
