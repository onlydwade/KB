package com.bytefinger.toutuo.system.core.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.toutuo.api.system.core.domain.SysConfig;
import com.bytefinger.toutuo.system.core.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 参数配置表 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-10
 */
@Slf4j
@Api(tags = "系统")
@AllArgsConstructor
@RestController
@RequestMapping("/config")
public class SysConfigController extends BaseController {

    private final ISysConfigService sysConfigService;

    @ApiOperation(value = "详情-参数配置")
    @GetMapping("/get/{configKey}")
    public R<SysConfig> get(@PathVariable("configKey") String configKey) {
        SysConfig sysConfig = sysConfigService.getOne(Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getConfigKey, configKey));
        return R.ok(sysConfig);
    }

    @ApiOperation(value = "设置参数配置")
    @PostMapping("/set/{configKey}")
    public R set(@PathVariable("configKey") String configKey, @RequestBody String confiVal) {
        sysConfigService.setConfig(configKey,confiVal);
        return R.ok();
    }

}
