package com.bytefinger.toutuo.biz.auditteam.controller;

import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.auditteam.service.IAuditTeamService;
import com.bytefinger.toutuo.biz.auditteam.domain.AuditTeam;
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
 * 审计团队信息 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-05
 */
@Slf4j
@Api(tags = "审计团队信息")
@AllArgsConstructor
@RestController
@RequestMapping("/auditTeam")
public class AuditTeamController extends BaseController {
    private final IAuditTeamService auditTeamService;

    @ApiOperation(value = "新增-审计团队信息")
    @PutMapping("/add")
    @RequiresPermissions("biz:auditTeam:add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody AuditTeam auditTeam) {
        return success(auditTeamService.save(auditTeam));
    }

    @ApiOperation(value = "修改-审计团队信息")
    @PutMapping("/update")
    @RequiresPermissions("biz:auditTeam:update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AuditTeam auditTeam) {
        return success(auditTeamService.updateById(auditTeam));
    }

    @ApiOperation(value = "删除-审计团队信息")
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("biz:auditTeam:delete")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(auditTeamService.removeById(id));
    }

    @ApiOperation(value = "详情-审计团队信息")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:auditTeam:get")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditTeamService.getById(id));
    }

    @ApiOperation(value = "列表-审计团队信息")
    @GetMapping("/list")
    @RequiresPermissions("biz:auditTeam:list")
    public AjaxResult list() {
        return success(auditTeamService.list());
    }

    @ApiOperation(value = "列表-审计团队信息")
    @GetMapping("/list/{auditId}")
    @RequiresPermissions("biz:auditTeam:list")
    public AjaxResult list(@PathVariable("auditId") Long auditId) {
        return success(auditTeamService.list(auditId));
    }

    @ApiOperation(value = "分页（带条件）-审计团队信息")
    @PostMapping("/page")
    @RequiresPermissions("biz:auditTeam:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(auditTeamService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
