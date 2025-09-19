package com.bytefinger.toutuo.biz.projectstep.controller;

import com.bytefinger.common.core.web.domain.vo.ProjectStepMenuVO;
import com.bytefinger.common.core.web.domain.vo.TreeEntityVO;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectBid;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectstep.constants.ProjectStepConstant;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStep;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenu;
import com.bytefinger.toutuo.biz.projectstep.enums.ProjectApprovalStatus;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepMenuConfigService;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenuConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepMenuService;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;

/**
 * <p>
 * 项目步骤菜单配置表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "项目步骤菜单配置表")
@AllArgsConstructor
@RestController
@RequestMapping("/projectStepMenuConfig")
public class ProjectStepMenuConfigController extends BaseController {
    private final IProjectStepMenuConfigService projectStepMenuConfigService;
    private final IProjectStepMenuService projectStepMenuService;

    private final IProjectStepService projectStepService;


    @ApiOperation(value = "修改-项目步骤菜单配置表")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectStepMenuConfig:add")
    public AjaxResult update(@RequestBody ProjectStepMenuConfig projectStepMenuConfig) {
        return toAjax(projectStepMenuConfigService.update(projectStepMenuConfig));
    }

    @ApiOperation(value = "列表-根据项目类型获取项目步骤")
    @GetMapping("/listByProjectType/{projectType}")
    @RequiresPermissions("biz:projectStepMenuConfig:list")
    public AjaxResult listByProjectType(@PathVariable("projectType") String projectType) {
        return success(projectStepMenuConfigService.list(Wrappers.<ProjectStepMenuConfig>lambdaQuery().eq(ProjectStepMenuConfig::getProjectType, projectType)));
    }

    @ApiOperation(value = "节点树-根据项目类型获取项目步骤树")
    @GetMapping("/treeProjectType/{projectType}")
    @RequiresPermissions("biz:projectStepMenuConfig:list")
    public AjaxResult treeByProjectType(@PathVariable("projectType") String projectType) {
        List<ProjectStepMenu> list = projectStepMenuConfigService.findProjectStepMenuByProjectType(projectType);
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        List<Long> menuIds = list.stream().map(v -> v.getId()).collect(Collectors.toList());
        List<ProjectStepMenuVO> tree = projectStepMenuService.tree(Wrappers.<ProjectStepMenu>lambdaQuery()
                .in(ProjectStepMenu::getId, menuIds).orderByAsc(ProjectStepMenu ::getSorts));
        return success(tree);
    }

    @ApiOperation(value = "节点树-根据项目类型获取项目步骤树（只含需要上传文件的节点）")
    @GetMapping("/listByProjectTypeTwo/{projectType}")
    @RequiresPermissions("biz:projectStepMenuConfig:list")
    public AjaxResult listByProjectTypeTwo(@PathVariable("projectType") String projectType) {
        List<ProjectStepMenu> list = projectStepMenuConfigService.findProjectStepMenuByProjectType(projectType);
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        List<Long> menuIds = list.stream().map(v -> v.getId()).collect(Collectors.toList());
        List<ProjectStepMenuVO> tree = projectStepMenuService.tree(Wrappers.<ProjectStepMenu>lambdaQuery()
                .in(ProjectStepMenu::getId, menuIds).orderByAsc(ProjectStepMenu ::getSorts), ProjectStepConstant.PROJECT_STEP_DOCUMENT_YES);
        List<ProjectStepMenuVO> resultList = tree.stream().filter(v -> (ProjectStepConstant.PROJECT_STEP_DOCUMENT_YES == v.getIsDocument()) || v.getChildren().size() > 0).collect(Collectors.toList());
        return success(resultList);
    }


    @ApiOperation(value = "列表-项目步骤菜单配置表")
    @GetMapping("/listByProjectId/{projectId}")
    @RequiresPermissions("biz:projectStepMenuConfig:list")
    public AjaxResult listByProjectId(@PathVariable("projectId") Long projectId) {
        return success(projectStepMenuConfigService.findProjectStepMenuByProjectId(projectId));
    }

    @ApiOperation(value = "节点树-项目步骤菜单配置表")
    @GetMapping("/treeByProjectId/{projectId}")
    @RequiresPermissions("biz:projectStepMenuConfig:list")
    public AjaxResult treeByProjectId(@PathVariable("projectId") Long projectId) {
        List<ProjectStepMenu> list = projectStepMenuConfigService.findProjectStepMenuByProjectId(projectId);
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        List<Long> menuIds = list.stream().map(v -> v.getId()).collect(Collectors.toList());
        List<ProjectStepMenuVO> tree = projectStepMenuService.tree(Wrappers.<ProjectStepMenu>lambdaQuery()
                .in(ProjectStepMenu::getId, menuIds).orderByAsc(ProjectStepMenu ::getSorts));

        //组装节点完成状态
        List<ProjectStep> steps = projectStepService.listByProjectId(projectId);
        Map<Long, ProjectStep> stepMap = steps.stream().collect(Collectors.toMap(ProjectStep::getStepMenuId, Function.identity(), (key1, key2) -> key2));
        recursion(tree, stepMap);
        return success(tree);
    }

    private void recursion(List<ProjectStepMenuVO> list, Map<Long, ProjectStep> stepMap){
        if(CollectionUtils.isEmpty(list) || list.size() == 0) return;
        list.forEach(v -> {
            ProjectStep projectStep = stepMap.get(v.getId());
            v.setStatus(ObjectUtils.isEmpty(projectStep) ? ProjectStepConstant.PROJECT_STEP_UNDONE : projectStep.getStatus());
            v.setApprovalStatus(ObjectUtils.isEmpty(projectStep) ? ProjectApprovalStatus.WEI_FA_QI_SHEN_PI.getCode() : projectStep.getApprovalStatus());
            recursion(v.getChildren(), stepMap);
        });
    }

//    @ApiOperation(value = "项目步骤节点完成状态")
//    @GetMapping("/get/{projectId}")
//    @RequiresPermissions("biz:projectStepMenuConfig:get")
//    public AjaxResult get(@PathVariable("projectId") Long projectId) {
//        List<ProjectStepMenu> menus = projectStepMenuConfigService.findProjectStepStatus(projectId);
//        return success(menus);
//    }

}
