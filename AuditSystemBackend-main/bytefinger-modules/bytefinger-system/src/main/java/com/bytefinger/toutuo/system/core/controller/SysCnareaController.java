package com.bytefinger.toutuo.system.core.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.system.core.service.ISysCnareaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 中国行政地区表 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-14
 */
@Slf4j
@Api(tags = "中国行政地区表")
@AllArgsConstructor
@RestController
@RequestMapping("/cnarea")
public class SysCnareaController extends BaseController {

    private final ISysCnareaService sysCnareaService;

    @ApiOperation(value = "到城市")
    @GetMapping("/listProvinceCity")
    public AjaxResult listProvinceCity() {
        return success(sysCnareaService.listProvinceCity());
    }

    @ApiOperation(value = "到区域")
    @GetMapping("/listProvinceCityArea")
    public AjaxResult listProvinceCityArea() {
        return success(sysCnareaService.listProvinceCityArea());
    }

    @ApiOperation(value = "通过区域id查街道")
    @GetMapping("/listStreet/{parentCode}")
    public AjaxResult listStreet(@PathVariable("parentCode") Long parentCode) {
        return success(sysCnareaService.listStreet(parentCode));
    }

//    @GetMapping("/test")
//    public void test() {
//        List<SysCnarea> dbAreas = sysCnareaService.list(new LambdaQueryWrapper<SysCnarea>().in(SysCnarea::getLevel, Arrays.asList(2)));
//        for (SysCnarea dbArea : dbAreas) {
//            SysCnarea n = new SysCnarea();
//            n.setLevel(3);
//            n.setParentCode(dbArea.getAreaCode());
//            n.setAreaCode(dbArea.getAreaCode() + 910101000000L);
//            n.setZipCode(999999);
//            n.setCityCode(dbArea.getCityCode());
//            n.setName("其他");
//            n.setShortName("其他");
//            n.setMergerName(dbArea.getMergerName() + ",其他");
//            n.setPinyin("QiTa");
//            sysCnareaService.save(n);
//        }
//    }


}
