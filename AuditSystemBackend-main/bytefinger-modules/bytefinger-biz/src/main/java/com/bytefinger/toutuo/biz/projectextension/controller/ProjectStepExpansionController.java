package com.bytefinger.toutuo.biz.projectextension.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.core.web.domain.vo.ProjectStepMenuVO;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectStepExpansion;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectStepExpansionService;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStep;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenu;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepMenuConfigService;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepMenuService;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 拓后项目步骤菜单
 * </p>
 *
 * @author ycj
 * @since 2023-03-23
 */
@Slf4j
@Api(tags = "拓项目步骤完成状态")
@AllArgsConstructor
@RestController
@RequestMapping("/projectStepExpansion")
public class ProjectStepExpansionController extends BaseController {

    @Autowired
    private IProjectStepExpansionService projectStepExpansionService;


    @ApiOperation(value = "修改Or新增-项目步骤完成状态")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectStepExpansion:update")
    public AjaxResult update(@RequestBody ProjectStepExpansion projectStepExpansion) {
        projectStepExpansionService.update(projectStepExpansion);
        return success(projectStepExpansion);
    }

    @ApiOperation(value = "删除-项目步骤完成状态")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectStepExpansion:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectStepExpansionService.removeById(id));
    }

    @ApiOperation(value = "详情-项目步骤完成状态")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectStepExpansion:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectStepExpansionService.getById(id));
    }

    @ApiOperation(value = "列表-项目步骤完成状态")
    @GetMapping("/list")
    @RequiresPermissions("biz:projectStepExpansion:list")
    public AjaxResult list() {
        return success(projectStepExpansionService.list());
    }

    @ApiOperation(value = "分页（带条件）-项目步骤完成状态")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectStepExpansion:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectStepExpansionService.page(queryPage.toPage(), queryPage.getWrapper()));
    }


//    @ApiOperation(value = "节点树-拓后项目步骤菜单配置表")
//    @GetMapping("/treeByProjectId/{projectId}")
//    @RequiresPermissions("biz:projectExtension:list")
//    public AjaxResult treeByProjectId(@PathVariable("projectId") Long projectId) {
//        List<ProjectStepMenu> list = bizProjectExtensionService.findProjectEXtensionStepMenuBy(projectId);
//        if(CollectionUtils.isEmpty(list)){
//            return success();
//        }
//        List<Long> menuIds = list.stream().map(v -> v.getId()).collect(Collectors.toList());
//        List<ProjectStepMenuVO> tree = projectStepMenuService.tree(Wrappers.<ProjectStepMenu>lambdaQuery()
//                .in(ProjectStepMenu::getId, menuIds).orderByAsc(ProjectStepMenu ::getSorts));
//
//        //组装节点完成状态
//        List<ProjectStep> steps = projectStepService.listByProjectId(projectId);
//        Map<Long, ProjectStep> stepMap = steps.stream().collect(Collectors.toMap(ProjectStep::getStepMenuId, Function.identity(), (key1, key2) -> key2));
//        recursion(tree, stepMap);
//        return success(tree);
//    }

}
