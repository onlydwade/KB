package com.bytefinger.toutuo.system.core.controller;

import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.system.core.domain.SysNotice;
import com.bytefinger.toutuo.system.core.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author patrick
 */
@Api(tags = "消息")
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController {
    @Autowired
    private ISysNoticeService noticeService;

    @ApiOperation("获取通知公告列表")
    @RequiresPermissions("system:notice:list")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return AjaxResult.success(noticeService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation("根据通知公告编号获取详细信息")
    @RequiresPermissions("system:notice:query")
    @GetMapping(value = "/get/{noticeId}")
    public AjaxResult get(@PathVariable Long noticeId) {
        return AjaxResult.success(noticeService.getById(noticeId));
    }

    @ApiOperation("新增通知公告")
    @RequiresPermissions("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PutMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysNotice notice) {
        notice.setCreateBy(SecurityUtils.getUsername());
        return toAjax(noticeService.save(notice));
    }

    @ApiOperation("修改通知公告")
    @RequiresPermissions("system:notice:update")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult update(@Validated @RequestBody SysNotice notice) {
        notice.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(noticeService.updateById(notice));
    }

    @ApiOperation("删除通知公告")
    @RequiresPermissions("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{noticeIds}")
    public AjaxResult delete(@PathVariable List<Long> noticeIds) {
        return toAjax(noticeService.removeByIds(noticeIds));
    }
}
