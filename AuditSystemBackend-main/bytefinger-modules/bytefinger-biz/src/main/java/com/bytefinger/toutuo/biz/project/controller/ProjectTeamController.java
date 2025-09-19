package com.bytefinger.toutuo.biz.project.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.project.domain.ProjectRiskInspection;
import com.bytefinger.toutuo.biz.project.domain.ProjectTeam;
import com.bytefinger.toutuo.biz.project.domain.ProjectTeamOutside;
import com.bytefinger.toutuo.biz.project.service.IProjectTeamOutsideService;
import com.bytefinger.toutuo.biz.project.service.IProjectTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 项目团队信息 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "项目团队信息")
@AllArgsConstructor
@RestController
@RequestMapping("/projectTeam")
public class ProjectTeamController extends BaseController {
    private final IProjectTeamService projectTeamService;

    private final IProjectTeamOutsideService projectTeamOutsideService;

    @ApiOperation(value = "新增-项目团队信息")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectTeam:add")
    public AjaxResult add(@RequestBody ProjectTeam projectTeam) {
        projectTeamService.save(projectTeam);
        return success(projectTeam);
    }

    @ApiOperation(value = "修改-项目团队信息")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectTeam:update")
    public AjaxResult update(@RequestBody ProjectTeam projectTeam) {
        projectTeamService.updateById(projectTeam);
        return success(projectTeam);
    }

    @ApiOperation(value = "删除-项目团队信息")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectTeam:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectTeamService.removeById(id));
    }

    @ApiOperation(value = "详情-项目团队信息")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectTeam:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectTeamService.getById(id));
    }

    @ApiOperation(value = "是否存在-项目团队信息")
    @GetMapping("/exist/{projectId}")
    @RequiresPermissions("biz:projectTeam:get")
    public AjaxResult exist(@PathVariable("projectId") Long projectId) {
        List<ProjectTeam> list = projectTeamService.list(projectId);
        if(CollectionUtils.isEmpty(list)){
            return success(false);
        }
        Optional<ProjectTeam> optional = list.stream().filter(v -> v.getUserId().equals(SecurityUtils.getUserId())).findFirst();
        if (optional.isPresent()) {
            return success(true);
        }

        return success(false);
    }



    @ApiOperation(value = "列表-项目团队信息")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectTeam:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId) {
        return success(projectTeamService.list(projectId));
    }


    @ApiOperation(value = "分页（带条件）-项目团队信息")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectTeam:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectTeamService.page(queryPage));
    }





}
