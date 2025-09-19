package com.bytefinger.toutuo.system.core.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.security.utils.DictUtils;
import com.bytefinger.toutuo.api.system.core.domain.SysDictData;
import com.bytefinger.toutuo.api.system.core.domain.SysDictType;
import com.bytefinger.toutuo.system.core.service.ISysDictDataService;
import com.bytefinger.toutuo.system.core.service.ISysDictTypeService;
import com.bytefinger.toutuo.system.dto.GetEditDicData;
import com.bytefinger.toutuo.system.dto.GetEditDicType;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-09
 */
@Slf4j
@Api(tags = "系统")
@AllArgsConstructor
@RestController
@RequestMapping("/dict")
public class SysDictController extends BaseController {

    private final ISysDictDataService sysDictDataService;

    private final ISysDictTypeService sysDictTypeService;

    @ApiOperation(value = "列表-字典类型表")
    @GetMapping("/typeList")
    public AjaxResult typeList() {
        List<SysDictType> sysDictTypeList = sysDictTypeService.list();
        return success(sysDictTypeList);
    }

    @ApiOperation(value = "列表-字典数据表")
    @GetMapping(value = "/list")
    public AjaxResult list() {
        LambdaQueryWrapper<SysDictData> sysDictDataLambdaQueryWrapper = Wrappers.<SysDictData>lambdaQuery().orderByAsc(SysDictData::getDictSort);
        List<SysDictData> sysDictDataList = sysDictDataService.list(sysDictDataLambdaQueryWrapper);
        //List<SysDictData> sysDictDataListNew = sysDictDataService.ignoreDictDataList(sysDictDataList);
        return success(sysDictDataList);
    }

    @ApiOperation(value = "MAP-字典数据")
    @GetMapping("/map")
    public AjaxResult map() {
        Map<String, List<SysDictData>> dictCacheAll = DictUtils.getDictCacheAll();
        if (null == dictCacheAll) {
            sysDictTypeService.resetDictCache();
            dictCacheAll = DictUtils.getDictCacheAll();
        }

        return success(dictCacheAll);
    }

    @ApiOperation(value = "刷新字典缓存")
    @GetMapping("/reset")
    public AjaxResult reset() {
        sysDictTypeService.resetDictCache();
        return success();
    }


    @ApiOperation(value = "列表-字典类型表(页面可修改)")
    @GetMapping("/getEditTypeList")
    public AjaxResult getEditTypeList(String dictName) {
        return success(sysDictTypeService.getEditTypeList(dictName));
    }

    @ApiOperation(value = "列表-字典类型表(页面可修改)")
    @GetMapping("/getDataListByType")
    public AjaxResult getDataListByType(String dictType) {
        return success(sysDictTypeService.getDataListByType(dictType));
    }

    @ApiOperation(value = "列表-字典类型表(页面可修改)")
    @PostMapping("/addDataInfo")
    public AjaxResult addDataInfo(@RequestBody GetEditDicData getEditDicData) {
        String errorMsg = sysDictTypeService.addDataInfo(getEditDicData);
        if (StrUtil.isBlank(errorMsg)) {
            //刷新数据
            reset();
            return success(errorMsg);
        }
        return error(errorMsg);
    }

    @ApiOperation(value = "列表-字典类型表(页面可修改)")
    @GetMapping("/getYeTaiDic")
    public AjaxResult getYeTaiDic() {
        return success(sysDictTypeService.getYeTaiDic());
    }


    @ApiOperation(value = "添加-字典类型表(页面可修改)")
    @PostMapping("/addDictType")
    public AjaxResult addDictType(@RequestBody GetEditDicType getEditDicType) {
        String errorMsg = sysDictTypeService.addDictType(getEditDicType);
        if (StrUtil.isBlank(errorMsg)) {
            //刷新数据
            reset();
            return success(errorMsg);
        }
        return error(errorMsg);
    }
}
