package com.bytefinger.toutuo.biz.companyexit.controller;

import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.toutuo.biz.companyexit.service.IProjectCompanyExitService;
import com.bytefinger.toutuo.biz.companyexit.domain.ProjectCompanyExit;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 项目退出 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "项目退出")
@AllArgsConstructor
@RestController
@RequestMapping("/companyExit")
public class ProjectCompanyExitController extends BaseController {
    private final IProjectCompanyExitService projectCompanyExitService;

    @ApiOperation(value = "新增-项目退出")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyExit projectCompanyExit) {
        projectCompanyExitService.save(projectCompanyExit);
        return success(projectCompanyExit);
    }

    @ApiOperation(value = "修改-项目退出")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyExit projectCompanyExit) {
        return success(projectCompanyExitService.updateById(projectCompanyExit));
    }

    @ApiOperation(value = "删除-项目退出")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyExitService.removeById(id));
    }

    @ApiOperation(value = "详情-项目退出")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        ProjectCompanyExit one =   projectCompanyExitService.getOneByDataFill(id);
       // ProjectCompanyExit byId = projectCompanyExitService.getById(id);
        return success(one);
    }

    @ApiOperation(value = "列表-项目退出")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyExitService.list());
    }

    @ApiOperation(value = "分页（带条件）-项目退出")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyExitService.page(queryPage));
    }

}
