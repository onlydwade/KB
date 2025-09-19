package com.bytefinger.toutuo.biz.project.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.dto.ChangeDTO;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.project.domain.ProjectCompanyLog;
import com.bytefinger.toutuo.biz.project.service.IProjectCompanyLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 项目客户操作日志 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2024-09-12
 */
@Slf4j
@Api(tags = "项目客户操作日志")
@AllArgsConstructor
@RestController
@RequestMapping("/projectLog")
public class ProjectCompanyLogController extends BaseController {
    private final IProjectCompanyLogService projectCompanyLogService;

    @ApiOperation(value = "新增-项目客户操作日志")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyLog projectCompanyLog) {
        return success(projectCompanyLogService.save(projectCompanyLog));
    }

    @ApiOperation(value = "修改-项目客户操作日志")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyLog projectCompanyLog) {
        return success(projectCompanyLogService.updateById(projectCompanyLog));
    }

    @ApiOperation(value = "删除-项目客户操作日志")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyLogService.removeById(id));
    }

    @ApiOperation(value = "详情-项目客户操作日志")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyLogService.getById(id));
    }

    @ApiOperation(value = "列表-项目客户操作日志")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyLogService.list());
    }

    @ApiOperation(value = "分页（带条件）-项目客户操作日志")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectCompanyLog:list")
    @DataFill
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyLogService.page(queryPage));
    }


    @ApiOperation(value = "项目客户恢复")
    @PostMapping("/reset")
    public AjaxResult reset(@RequestBody @Validated ChangeDTO changeStatusDTO) {
        if (changeStatusDTO.getId() == null) {
            return error("请选择恢复数据");
        }
        projectCompanyLogService.reset(changeStatusDTO);
        return success();
    }
}
