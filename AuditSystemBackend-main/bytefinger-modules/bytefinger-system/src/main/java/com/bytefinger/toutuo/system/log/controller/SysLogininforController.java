package com.bytefinger.toutuo.system.log.controller;

import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.constant.CacheConstants;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.redis.service.RedisService;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.api.system.log.domain.SysLogininfor;
import com.bytefinger.toutuo.system.log.service.ISysLogininforService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 系统访问记录
 *
 * @author patrick
 */
@Api(tags = "日志")
@RestController
@RequestMapping("/logininfor")
@AllArgsConstructor
public class SysLogininforController extends BaseController {

    private final ISysLogininforService logininforService;

    private final RedisService redisService;

    @ApiOperation("登录日志")
    @RequiresPermissions("system:operlog:list")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return AjaxResult.success(logininforService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation("账户解锁")
    @RequiresPermissions("system:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public AjaxResult unlock(@PathVariable("userName") String userName) {
        redisService.deleteObject(CacheConstants.PWD_ERR_CNT_KEY + userName);
        return success();
    }

    @ApiOperation(value = "新增登录日志", hidden = true)
    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysLogininfor logininfor) {
        logininfor.setAccessTime(new Date());
        return toAjax(logininforService.save(logininfor));
    }
}
