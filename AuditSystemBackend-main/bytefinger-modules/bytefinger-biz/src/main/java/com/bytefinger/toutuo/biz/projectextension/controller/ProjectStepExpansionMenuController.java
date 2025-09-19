package com.bytefinger.toutuo.biz.projectextension.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectStepExpansionMenu;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectStepExpansionMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 拓后项目步骤菜单
 * </p>
 *
 * @author ycj
 * @since 2023-03-23
 */
@Slf4j
@Api(tags = "拓后项目步骤菜单")
@AllArgsConstructor
@RestController
@RequestMapping("/projectStepExpansionMenu")
public class ProjectStepExpansionMenuController extends BaseController {
    @Autowired
    private IProjectStepExpansionMenuService projectStepExpansionMenuService;



    @ApiOperation(value = "详情-项目步骤菜单")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectStepMenu:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectStepExpansionMenuService.getById(id));
    }

    @ApiOperation(value = "全部-获取项目步骤树")
    @GetMapping("/tree")
    @RequiresPermissions("biz:projectStepMenu:list")
    public AjaxResult tree() {
        return AjaxResult.success(projectStepExpansionMenuService.tree(Wrappers.<ProjectStepExpansionMenu>lambdaQuery().orderByAsc(ProjectStepExpansionMenu::getSorts)));
    }

    @ApiOperation(value = "单节点-获取项目步骤树")
    @GetMapping("/treeById/{id}")
    @RequiresPermissions("biz:projectStepMenu:list")
    public AjaxResult treeById(@PathVariable("id") Integer id) {
        return AjaxResult.success(projectStepExpansionMenuService.tree(id));
    }

    @ApiOperation(value = "修改-项目节点步骤")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectStepMenu:update")
    public AjaxResult update(@RequestBody ProjectStepExpansionMenu projectStepMenu) {
        projectStepExpansionMenuService.updateById(projectStepMenu);
        return success(projectStepMenu);
    }



}
