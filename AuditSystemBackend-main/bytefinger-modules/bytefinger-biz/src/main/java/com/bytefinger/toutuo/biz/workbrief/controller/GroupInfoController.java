package com.bytefinger.toutuo.biz.workbrief.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.workbrief.domain.GroupInfo;
import com.bytefinger.toutuo.biz.workbrief.service.IGroupInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api(tags = "工作简报")
@RequiredArgsConstructor
@RestController
@RequestMapping("/groupInfo")
public class GroupInfoController extends BaseController {

    @Autowired
    IGroupInfoService iGroupInfoService;

    @ApiOperation(value = "工作简报群组")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(iGroupInfoService.page(queryPage));
    }

    @ApiOperation(value = "保存工作简报")
    @PostMapping("/save")
    public AjaxResult save(@RequestBody GroupInfo groupInfo) {
        return success(iGroupInfoService.saveInfo(groupInfo));
    }

    @ApiOperation(value = "获取工作简报")
    @GetMapping("/get")
    public AjaxResult getById(Long id) {
        return success(iGroupInfoService.getGroupById(id));
    }

    @ApiOperation(value = "删除工作简报")
    @GetMapping("/delete")
    public AjaxResult delete(Long id) {
        return success(iGroupInfoService.deleteById(id));
    }

}
