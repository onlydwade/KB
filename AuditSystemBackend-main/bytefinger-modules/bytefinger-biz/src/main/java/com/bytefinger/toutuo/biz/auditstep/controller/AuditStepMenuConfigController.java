package com.bytefinger.toutuo.biz.auditstep.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.core.web.domain.vo.AuditStepMenuVO;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.auditstep.constants.AuditStepConstant;
import com.bytefinger.toutuo.biz.auditstep.domain.AuditStep;
import com.bytefinger.toutuo.biz.auditstep.domain.AuditStepMenu;
import com.bytefinger.toutuo.biz.auditstep.domain.AuditStepMenuConfig;
import com.bytefinger.toutuo.biz.auditstep.service.IAuditStepMenuConfigService;
import com.bytefinger.toutuo.biz.auditstep.service.IAuditStepMenuService;
import com.bytefinger.toutuo.biz.auditstep.service.IAuditStepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 审计步骤菜单配置表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "审计步骤菜单配置表")
@AllArgsConstructor
@RestController
@RequestMapping("/auditStepMenuConfig")
public class AuditStepMenuConfigController extends BaseController {

    private final IAuditStepMenuConfigService auditStepMenuConfigService;

    private final IAuditStepMenuService auditStepMenuService;

    private final IAuditStepService auditStepService;

    @ApiOperation(value = "修改-审计步骤菜单配置表")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:auditStepMenuConfig:add")
    public AjaxResult update(@RequestBody AuditStepMenuConfig auditStepMenuConfig) {
        return toAjax(auditStepMenuConfigService.update(auditStepMenuConfig));
    }

    @ApiOperation(value = "列表-根据审计类型获取审计步骤")
    @GetMapping("/listByAuditType/{auditType}")
    @RequiresPermissions("biz:auditStepMenuConfig:list")
    public AjaxResult listByAuditType(@PathVariable("auditType") String auditType) {
        return success(auditStepMenuConfigService.list(Wrappers.<AuditStepMenuConfig>lambdaQuery()
                .eq(AuditStepMenuConfig::getAuditType, auditType)));
    }

    @ApiOperation(value = "节点树-根据审计类型获取审计步骤树")
    @GetMapping("/treeAuditType/{auditType}")
    @RequiresPermissions("biz:auditStepMenuConfig:list")
    public AjaxResult treeByAuditType(@PathVariable("auditType") String auditType) {
        List<AuditStepMenu> list = auditStepMenuConfigService.findAuditStepMenuByAuditType(auditType);
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        List<Long> menuIds = list.stream().map(v -> v.getId()).collect(Collectors.toList());
        List<AuditStepMenuVO> tree = auditStepMenuService.tree(Wrappers.<AuditStepMenu>lambdaQuery()
                .in(AuditStepMenu::getId, menuIds).orderByAsc(AuditStepMenu ::getSorts));
        return success(tree);
    }

    @ApiOperation(value = "节点树-根据审计类型获取审计步骤树（只含需要上传文件的节点）")
    @GetMapping("/listByAuditTypeTwo/{auditType}")
    @RequiresPermissions("biz:auditStepMenuConfig:list")
    public AjaxResult listByAuditTypeTwo(@PathVariable("auditType") String auditType) {
        List<AuditStepMenu> list = auditStepMenuConfigService.findAuditStepMenuByAuditType(auditType);
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        List<Long> menuIds = list.stream().map(v -> v.getId()).collect(Collectors.toList());
        List<AuditStepMenuVO> tree = auditStepMenuService.tree(Wrappers.<AuditStepMenu>lambdaQuery()
                .in(AuditStepMenu::getId, menuIds).orderByAsc(AuditStepMenu ::getSorts), AuditStepConstant.AUDIT_STEP_DOCUMENT_YES);
        List<AuditStepMenuVO> resultList = tree.stream().filter(v -> (AuditStepConstant.AUDIT_STEP_DOCUMENT_YES == v.getIsDocument()) || v.getChildren().size() > 0).collect(Collectors.toList());
        return success(resultList);
    }


    @ApiOperation(value = "列表-审计步骤菜单配置表")
    @GetMapping("/listByAuditId/{auditId}")
    @RequiresPermissions("biz:auditStepMenuConfig:list")
    public AjaxResult listByAuditId(@PathVariable("auditId") Long auditId) {
        return success(auditStepMenuConfigService.findAuditStepMenuByAuditId(auditId));
    }

    @ApiOperation(value = "节点树-审计步骤菜单配置表")
    @GetMapping("/treeByAuditId/{auditId}")
    @RequiresPermissions("biz:auditStepMenuConfig:list")
    public AjaxResult treeByAuditId(@PathVariable("auditId") Long auditId) {
        List<AuditStepMenu> list = auditStepMenuConfigService.findAuditStepMenuByAuditId(auditId);
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        List<Long> menuIds = list.stream().map(v -> v.getId()).collect(Collectors.toList());
        List<AuditStepMenuVO> tree = auditStepMenuService.tree(Wrappers.<AuditStepMenu>lambdaQuery()
                .in(AuditStepMenu::getId, menuIds).orderByAsc(AuditStepMenu ::getSorts));

        //组装节点完成状态
        List<AuditStep> steps = auditStepService.listByAuditId(auditId);
        Map<Long, AuditStep> stepMap = steps.stream().collect(Collectors.toMap(AuditStep::getStepMenuId, Function.identity(), (key1, key2) -> key2));
        recursion(tree, stepMap);
        return success(tree);
    }

    private void recursion(List<AuditStepMenuVO> list, Map<Long, AuditStep> stepMap){
        if(CollectionUtils.isEmpty(list) || list.size() == 0) return;
        list.forEach(v -> {
            AuditStep auditStep = stepMap.get(v.getId());
            v.setStatus(ObjectUtils.isEmpty(auditStep) ? AuditStepConstant.AUDIT_STEP_UNDONE : auditStep.getStatus());
            //v.setApprovalStatus(ObjectUtils.isEmpty(auditStep) ? ProjectApprovalStatus.WEI_FA_QI_SHEN_PI.getCode() : auditStep.getApprovalStatus());
            recursion(v.getChildren(), stepMap);
        });
    }

    @ApiOperation(value = "审计步骤节点完成状态")
    @GetMapping("/get/{auditId}")
    @RequiresPermissions("biz:auditStepMenuConfig:get")
    public AjaxResult get(@PathVariable("auditId") Long auditId) {
        List<AuditStepMenu> menus = auditStepMenuConfigService.findAuditStepStatus(auditId);
        return success(menus);
    }

}
