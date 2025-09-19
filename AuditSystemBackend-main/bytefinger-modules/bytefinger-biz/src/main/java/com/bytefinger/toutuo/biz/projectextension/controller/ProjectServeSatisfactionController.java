package com.bytefinger.toutuo.biz.projectextension.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCheck;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectOperateIncome;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectServeSatisfaction;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectServeSatisfactionService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 拓后运营-服务满意度信息
 *
 * @author ycj
 * @email
 * @date 2023-03-20 11:40:08
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectServeSatisfaction")
public class ProjectServeSatisfactionController extends BaseController {

    @Autowired
    private IProjectServeSatisfactionService projectServeSatisfactionService;


    @ApiOperation(value = "修改-服务满意度信息")
    @PostMapping("/update")
    @RequiresPermissions("biz:projectServeSatisfaction:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectServeSatisfaction projectServeSatisfaction) {
        return success(projectServeSatisfactionService.update(projectServeSatisfaction));
    }

    @ApiOperation(value = "添加-服务满意度信息")
    @PostMapping("/add")
    @RequiresPermissions("biz:projectServeSatisfaction:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectServeSatisfaction projectServeSatisfaction) {
        projectServeSatisfaction.setDeleted(0);
        return success(projectServeSatisfactionService.add(projectServeSatisfaction));
    }

    @ApiOperation(value = "删除-服务满意度信息")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:projectServeSatisfaction:delete")
    public AjaxResult get(@PathVariable("id") Long id) {
        return success(projectServeSatisfactionService.deleteById(id));
    }

    @ApiOperation(value = "项目收支列表")
    @PostMapping("/list")
    @RequiresPermissions("biz:projectServeSatisfaction:list")
    public AjaxResult list(@RequestBody ProjectServeSatisfaction projectServeSatisfaction) {
        LambdaQueryWrapper<ProjectServeSatisfaction> queryWrapper = new LambdaQueryWrapper<ProjectServeSatisfaction>();
        queryWrapper.eq(ProjectServeSatisfaction::getProjectId, projectServeSatisfaction.getProjectId());
        if (ObjectUtils.isNotEmpty(projectServeSatisfaction.getSatisfactionType())) {
            queryWrapper.eq(ProjectServeSatisfaction::getSatisfactionType, projectServeSatisfaction.getSatisfactionType());
        }
        queryWrapper.eq(ProjectServeSatisfaction::getDeleted, 0);
        List<ProjectServeSatisfaction> serveSatisfactionList = projectServeSatisfactionService.list(queryWrapper);


        return success(serveSatisfactionList);
    }
}
