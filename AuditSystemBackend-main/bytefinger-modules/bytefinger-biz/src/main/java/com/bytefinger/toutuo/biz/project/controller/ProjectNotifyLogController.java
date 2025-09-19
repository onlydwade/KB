package com.bytefinger.toutuo.biz.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.biz.project.domain.ProjectBid;
import com.bytefinger.toutuo.biz.project.domain.ProjectNotifyLog;
import com.bytefinger.toutuo.biz.project.service.IProjectBidService;
import com.bytefinger.toutuo.biz.project.service.IProjectNotifyLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 通告撤销日志 前端控制器
 * </p>
 *
 * @author lin
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "通告撤销日志")
@AllArgsConstructor
@RestController
@RequestMapping("/projectNotifyLog")
public class ProjectNotifyLogController extends BaseController {

    private final IProjectNotifyLogService iProjectNotifyLogService;

    @ApiOperation(value = "分页（带条件）-通告撤销日志")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(iProjectNotifyLogService.page(queryPage));
    }

    @ApiOperation(value = "保存-通告撤销日志")
    @PostMapping("/save")
    public AjaxResult add(@RequestBody ProjectNotifyLog projectNotifyLog) {
        iProjectNotifyLogService.save(projectNotifyLog);
        return success(projectNotifyLog);
    }

    @ApiOperation(value = "恢复-通告")
    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        ProjectNotifyLog log = new ProjectNotifyLog();
        log.setId(id);
        log.setDeleted(2);
        return success(iProjectNotifyLogService.updateById(log));
    }

}
