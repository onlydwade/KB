package com.bytefinger.toutuo.biz.companystep.controller;

import com.bytefinger.common.core.web.domain.vo.ProjectCompanyStepMenuVO;
import com.bytefinger.common.core.web.domain.vo.ProjectStepMenuVO;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.companystep.service.IProjectCompanyStepMenuService;
import com.bytefinger.toutuo.biz.companystep.domain.ProjectCompanyStepMenu;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;

/**
 * <p>
 * 子公司菜单 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-19
 */
@Slf4j
@Api(tags = "子公司菜单")
@AllArgsConstructor
@RestController
@RequestMapping("/companyStep")
public class ProjectCompanyStepMenuController extends BaseController {
    private final IProjectCompanyStepMenuService projectCompanyStepMenuService;

    @ApiOperation(value = "新增-子公司菜单")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyStepMenu projectCompanyStepMenu) {
        projectCompanyStepMenuService.save(projectCompanyStepMenu);
        return success(projectCompanyStepMenu);
    }

    @ApiOperation(value = "修改-子公司菜单")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyStepMenu projectCompanyStepMenu) {
        return success(projectCompanyStepMenuService.updateById(projectCompanyStepMenu));
    }

    @ApiOperation(value = "删除-子公司菜单")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyStepMenuService.removeById(id));
    }

    @ApiOperation(value = "详情-子公司菜单")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyStepMenuService.getById(id));
    }

    @ApiOperation(value = "列表-子公司菜单")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyStepMenuService.list());
    }

    @ApiOperation(value = "分页（带条件）-子公司菜单")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyStepMenuService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation(value = "节点树-获取子公司节点树")
    @GetMapping("/tree")
    public AjaxResult tree() {
        List<ProjectCompanyStepMenu> list = projectCompanyStepMenuService.list();
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        List<Long> menuIds = list.stream().map(v -> v.getId()).collect(Collectors.toList());
        List<ProjectCompanyStepMenuVO> tree = projectCompanyStepMenuService.tree(Wrappers.<ProjectCompanyStepMenu>lambdaQuery()
                .in(ProjectCompanyStepMenu::getId, menuIds).orderByAsc(ProjectCompanyStepMenu ::getSorts));
        return success(tree);
    }

}
