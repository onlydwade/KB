package com.bytefinger.toutuo.biz.projectcompany.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyContact;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyContactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 企业联系人 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-03-17
 */
@Slf4j
@Api(tags = "企业联系人")
@AllArgsConstructor
@RestController
@RequestMapping("/projectCompanyContact")
public class ProjectCompanyContactController extends BaseController {
    private final IProjectCompanyContactService projectCompanyContactService;

    @ApiOperation(value = "新增-企业联系人")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ProjectCompanyContact projectCompanyContact) {
        projectCompanyContactService.save(projectCompanyContact);
        return success(projectCompanyContact);
    }

    @ApiOperation(value = "修改-企业联系人")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ProjectCompanyContact projectCompanyContact) {
        return success(projectCompanyContactService.updateById(projectCompanyContact));
    }

    @ApiOperation(value = "删除-企业联系人")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(projectCompanyContactService.removeById(id));
    }

    @ApiOperation(value = "详情-企业联系人")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(projectCompanyContactService.getById(id));
    }

    @ApiOperation(value = "列表-企业联系人")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(projectCompanyContactService.list());
    }

    @ApiOperation(value = "分页（带条件）-企业联系人")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyContactService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
