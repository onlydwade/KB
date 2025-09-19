package com.bytefinger.toutuo.biz.oa.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.toutuo.biz.oa.domain.OaApprovalTemplate;
import com.bytefinger.toutuo.biz.oa.service.IOaApprovalTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 发起OA审批 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-26
 */
@Slf4j
@Api(tags = "OA审批模板")
@AllArgsConstructor
@RestController
@RequestMapping("oaApprovalTemplate")
public class OaApprovalTemplateController extends BaseController {
    private final IOaApprovalTemplateService oaApprovalTemplateService;

    @ApiOperation(value = "新增-OA审批模板")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody OaApprovalTemplate oaApprovalTemplate) {
        return success(oaApprovalTemplateService.save(oaApprovalTemplate));
    }

    @ApiOperation(value = "修改-OA审批模板")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody OaApprovalTemplate oaApprovalTemplate) {
        return success(oaApprovalTemplateService.updateById(oaApprovalTemplate));
    }

    @ApiOperation(value = "详情-OA审批模板")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(oaApprovalTemplateService.getById(id));
    }

    @ApiOperation(value = "列表-OA审批模板")
    @GetMapping("/list/{projectType}/{stepMenuId}")
    public AjaxResult list(@PathVariable("projectType") String projectType,@PathVariable("stepMenuId") Long stepMenuId) {
        return success(oaApprovalTemplateService.list());
    }

    @ApiOperation(value = "分页（带条件）-OA审批模板")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(oaApprovalTemplateService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
