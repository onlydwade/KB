package com.bytefinger.toutuo.biz.projectstep.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bytefinger.common.core.utils.TreeUtils;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.domain.ProjectBid;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenuConfig;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepMenuService;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenu;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;

/**
 * <p>
 * 项目步骤菜单 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "项目步骤菜单")
@AllArgsConstructor
@RestController
@RequestMapping("/projectStepMenu")
public class ProjectStepMenuController extends BaseController {
    private final IProjectStepMenuService projectStepMenuService;

    @ApiOperation(value = "详情-项目步骤菜单")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectStepMenu:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectStepMenuService.getById(id));
    }

    @ApiOperation(value = "全部-获取项目步骤树")
    @GetMapping("/tree")
    @RequiresPermissions("biz:projectStepMenu:list")
    public AjaxResult tree() {
        return AjaxResult.success(projectStepMenuService.tree(Wrappers.<ProjectStepMenu>lambdaQuery().orderByAsc(ProjectStepMenu::getSorts)));
    }

    @ApiOperation(value = "单节点-获取项目步骤树")
    @GetMapping("/treeById/{id}")
    @RequiresPermissions("biz:projectStepMenu:list")
    public AjaxResult treeById(@PathVariable("id") Integer id) {
        return AjaxResult.success(projectStepMenuService.tree(id));
    }

    @ApiOperation(value = "修改-项目节点步骤")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectStepMenu:update")
    public AjaxResult update(@RequestBody ProjectStepMenu projectStepMenu) {
        projectStepMenuService.updateById(projectStepMenu);
        return success(projectStepMenu);
    }

//    @ApiOperation(value = "分页（带条件）-项目步骤菜单")
//    @PostMapping("/page")
//    public AjaxResult page(@RequestBody QueryPage queryPage) {
//        return success(projectStepMenuService.page(queryPage.toPage(), queryPage.getWrapper()));
//    }

}
