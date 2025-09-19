package com.bytefinger.toutuo.biz.workbrief.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.workbrief.service.IRecentProjectWinBidderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 近期项目中标情况 前端控制器
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
@Slf4j
@Api(tags = "近期项目中标情况")
@RequiredArgsConstructor
@RestController
@RequestMapping("/recentProject/winBidder")
public class RecentProjectWinBidderController extends BaseController {

    @Autowired
    IRecentProjectWinBidderService iRecentProjectWinBidderService;

    @ApiOperation(value = "最近2周中标的项目数据")
    @PostMapping("/getList")
    public AjaxResult getList(@RequestBody QueryPage queryPage) {
        return success(iRecentProjectWinBidderService.getList(queryPage));
    }

}
