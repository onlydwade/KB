package com.bytefinger.toutuo.biz.oa.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.toutuo.api.biz.project.enums.OAApprovalStatus;
import com.bytefinger.toutuo.biz.oa.constants.OaConstant;
import com.bytefinger.toutuo.biz.oa.domain.dto.OaApprovalDTO;
import com.bytefinger.toutuo.biz.oa.domain.dto.OaCallBackDTO;
import com.bytefinger.toutuo.biz.oa.domain.dto.ReimbursementApplicationDTO;
import com.bytefinger.toutuo.biz.oa.domain.dto.ReimbursementApplicationRespone;
import com.bytefinger.toutuo.biz.oa.service.IOaApprovalService;
import com.bytefinger.toutuo.biz.oa.domain.OaApproval;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStep;
import com.bytefinger.toutuo.biz.projectstep.service.IProjectStepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;

/**
 * <p>
 * 发起OA审批 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-16
 */
@Slf4j
@Api(tags = "发起OA审批")
@AllArgsConstructor
@RestController
@RequestMapping("/oaApproval")
public class OaApprovalController extends BaseController {
    private final IOaApprovalService oaApprovalService;

    @ApiOperation(value = "OA状态更新-回调")
    @PostMapping("/callBack")
    public AjaxResult callBack(@RequestBody OaCallBackDTO oaCallBackDTO) {
        log.info("OaCallBackDTO=====================>" + oaCallBackDTO.toString());
        oaApprovalService.callBack(oaCallBackDTO);
        return success();
    }

    @ApiOperation(value = "新增-发起OA审批")
    @PutMapping("/add")
    public AjaxResult add(@RequestBody OaApproval oaApproval) {
        oaApproval = oaApprovalService.add(oaApproval);
        return success(oaApproval);
    }

    @ApiOperation(value = "修改-发起OA审批")
    @PutMapping("/update")
    public AjaxResult update(@RequestBody OaApproval oaApproval) {
        oaApproval = oaApprovalService.update(oaApproval);
        return success(oaApproval);
    }

    @ApiOperation(value = "查询OA审批详情")
    @GetMapping("/get/{moduleName}/{recordId}/{subRecordId}")
    public AjaxResult get(@PathVariable("moduleName") String moduleName, Long recordId, Long subRecordId) {
        OaApproval oaApproval = oaApprovalService.getOne(Wrappers.<OaApproval>lambdaQuery().eq(OaApproval::getModuleName, moduleName)
                .eq(OaApproval::getRecordId, recordId).eq(OaApproval::getSubRecordId, subRecordId).eq(OaApproval::getExpire, OaConstant.FOU));
        return success(oaApproval);
    }

    @ApiOperation(value = "分页（带条件）-发起OA审批")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(oaApprovalService.page(queryPage));
    }

    @ApiOperation(value = "删除oa审批结果查询")
    @GetMapping("/getDeleteList")
    public AjaxResult getDeleteList(String moduleType, String moduleId, String serviceId, String userId) {
        return success(oaApprovalService.getDeleteList(moduleType, moduleId, serviceId, userId));
    }

    @ApiOperation(value = "删除oa待办")
    @GetMapping("/deleteList")
    public void deleteList(String moduleType, String moduleId, String serviceId, String userId) {
        oaApprovalService.deleteList(moduleType, moduleId, serviceId, userId);
    }
}
