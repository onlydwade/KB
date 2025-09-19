package com.bytefinger.toutuo.biz.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.constant.UserConstants;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.IntMainBm;
import com.bytefinger.toutuo.api.biz.user.domain.IntMainDept;
import com.bytefinger.toutuo.api.biz.user.domain.SysDept;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.biz.external.domain.IntMainHr;
import com.bytefinger.toutuo.biz.external.domain.IntMainUser;
import com.bytefinger.toutuo.biz.external.service.IIntMainBmService;
import com.bytefinger.toutuo.biz.external.service.IIntMainHrService;
import com.bytefinger.toutuo.biz.external.service.IIntMainUserService;
import com.bytefinger.toutuo.biz.user.domain.DeptMainDept;
import com.bytefinger.toutuo.biz.user.service.IDeptMainDeptService;
import com.bytefinger.toutuo.biz.user.service.ISysDeptService;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>部门</p>
 *
 * @author Patrick
 * @date 2022/10/13 09:06
 **/
@Api(tags = "用户")
@RestController
@RequestMapping("/dept")
@AllArgsConstructor
public class SysDeptController extends BaseController {

    private final ISysDeptService deptService;

    private final IDeptMainDeptService deptMainDeptService;

    private final ISysUserService userService;

    private final IIntMainBmService intMainBmService;

    private final IIntMainHrService intMainHrService;

    private final IIntMainUserService myUserService;

    @ApiOperation("新增部门")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PutMapping("/add")
    @RequiresPermissions("system:dept:add")
    public AjaxResult add(@Validated @RequestBody com.bytefinger.toutuo.biz.user.domain.SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        int result = deptService.insertDept(dept);
        if(!CollectionUtils.isEmpty(dept.getMainDeptIds()) && dept.getMainDeptIds().size() > 0){
            deptMainDeptService.remove(Wrappers.<DeptMainDept>lambdaQuery().in(DeptMainDept::getMainDeptId, dept.getMainDeptIds()));
            dept.getMainDeptIds().forEach(v -> {
                relation(dept.getDeptId(), v);
            });
        }
        return toAjax(result);
    }

    @ApiOperation("修改部门")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @RequiresPermissions("system:dept:update")
    public AjaxResult update(@Validated @RequestBody com.bytefinger.toutuo.biz.user.domain.SysDept dept) {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(deptId)) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        deptMainDeptService.remove(Wrappers.<DeptMainDept>lambdaQuery().eq(DeptMainDept::getDeptId, deptId));
        if(!CollectionUtils.isEmpty(dept.getMainDeptIds()) && dept.getMainDeptIds().size() > 0){
            deptMainDeptService.remove(Wrappers.<DeptMainDept>lambdaQuery().in(DeptMainDept::getMainDeptId, dept.getMainDeptIds()));
            dept.getMainDeptIds().forEach(v -> {
                relation(deptId, v);
            });
        }
        return toAjax(deptService.updateDept(dept));
    }

    @ApiOperation("删除部门")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{deptId}")
    @RequiresPermissions("system:dept:delete")
    public AjaxResult delete(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return AjaxResult.error("存在下级部门,不允许删除");
        }

        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }

        deptMainDeptService.remove(Wrappers.<DeptMainDept>lambdaQuery().eq(DeptMainDept::getDeptId, deptId));
        deptService.checkDeptDataScope(deptId);
        return toAjax(deptService.deleteDeptById(deptId));
    }

    @ApiOperation("根据部门编号获取详细信息")
    @GetMapping(value = "/get/{deptId}")
    public AjaxResult get(@PathVariable Long deptId) {
        deptService.checkDeptDataScope(deptId);
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    @ApiOperation("根据部门编号获取区域层级信息")
    @GetMapping(value = "/region/{deptId}")
    public AjaxResult region(@PathVariable Long deptId) {
        deptService.checkDeptDataScope(deptId);
        return AjaxResult.success(deptService.selectDeptByRegionId(deptId));
    }

    @ApiOperation("获取部门列表")
    @GetMapping("/list")
    public AjaxResult list() {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        if(!CollectionUtils.isEmpty(depts) && depts.size() > 0){
            depts.forEach(v -> {
                List<IntMainBm> bmList = deptMainDeptService.listByDeptId(v.getDeptId());
                List<IntMainDept> deptBms = new ArrayList<>();
                bmList.stream().forEach(bv -> {
                    IntMainDept dept = new IntMainDept();
                    dept.setDesc1(bv.getCode());
                    dept.setDesc2(bv.getDesc2());
                    dept.setDesc3(bv.getDesc4());
                    dept.setDesc4(bv.getDesc15());
                    dept.setDesc5(bv.getDesc16());
                    dept.setDept(bv.getDesc6());
                    dept.setOptional(true);
                    deptBms.add(dept);
                });
                v.setMainDepts(deptBms);
            });
        }
        return AjaxResult.success(depts);
    }

    @ApiOperation("查询部门列表（排除节点）")
    @GetMapping("/list/exclude/{deptId}")
    @RequiresPermissions("system:dept:list")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new com.bytefinger.toutuo.biz.user.domain.SysDept());
        depts.removeIf(d -> d.getDeptId().intValue() == deptId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""));
        return AjaxResult.success(depts);
    }

    @ApiOperation("关联组织架构和主数据组织架构")
    @GetMapping("/relation/{deptId}/{mainDeptId}")
    @RequiresPermissions("system:dept:relation")
    public AjaxResult relation(@PathVariable(value = "deptId", required = false) Long deptId, @PathVariable(value = "mainDeptId", required = false) String mainDeptId) {
        List<DeptMainDept> list = deptMainDeptService.list(Wrappers.<DeptMainDept>lambdaQuery().eq(DeptMainDept::getDeptId, deptId)
                .eq(DeptMainDept::getMainDeptId, mainDeptId));
        if(!CollectionUtils.isEmpty(list) || list.size() > 0){
            return AjaxResult.error("请勿重复关联");
        }
        DeptMainDept deptMainDept = new DeptMainDept();
        deptMainDept.setDeptId(deptId);
        deptMainDept.setMainDeptId(mainDeptId);
        deptMainDeptService.save(deptMainDept);
        //自动把所有主数据的人进行部门关联
        IntMainBm bm = intMainBmService.getOne(Wrappers.<IntMainBm>lambdaQuery().eq(IntMainBm::getCode, mainDeptId));
        List<IntMainHr> hrs = intMainHrService.list(Wrappers.<IntMainHr>lambdaQuery().eq(IntMainHr::getDesc6, bm.getDesc15()).eq(IntMainHr::getDesc11, "A"));
        if(CollectionUtils.isEmpty(hrs)){
            return AjaxResult.success();
        }
        List<IntMainUser> mainUsers = myUserService.list(Wrappers.<IntMainUser>lambdaQuery().in(IntMainUser::getCodeId, hrs.stream().map(v -> v.getDesc1()).collect(Collectors.toList())));
        if(CollectionUtils.isEmpty(mainUsers) || mainUsers.size() == 0){
            return AjaxResult.success();
        }

        List<String> desc1s = mainUsers.stream().map(v -> v.getDesc1()).collect(Collectors.toList());
        SysUser user = new SysUser();
        user.setDeptId(deptId);
        user.setMainDeptId(mainDeptId);
        userService.update(user, Wrappers.<SysUser>lambdaQuery().in(SysUser::getMainUserId, desc1s));
        return AjaxResult.success(deptMainDept);
    }



    @ApiOperation("初始化部门层级")
    @GetMapping("/initDeptType")
    public AjaxResult initDeptType() {
        List<SysDept> list = deptService.list();
        for (SysDept sysDept : list) {
            List<SysDept> depts = deptService.list(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getParentId, sysDept.getDeptId()));
            if (CollectionUtils.isNotEmpty(depts)){
                sysDept.setDeptType("CENG_JI");
            }else {
                sysDept.setDeptType("BU_MEN");
            }
            deptService.updateById(sysDept);
        }

        return AjaxResult.success();
    }



    @ApiOperation("层级列表")
    @GetMapping("/listSingerType")
    public AjaxResult listSingerType() {
     //   List<SysDept> depts = deptService.selectDeptList(Wrappers.<SysDept>lambdaQuery().equals());
        List<SysDept> depts = deptService.list(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getDeptType, "CENG_JI").eq(SysDept::getDelFlag, 0));
        if(!CollectionUtils.isEmpty(depts) && depts.size() > 0){
            depts.forEach(v -> {
                List<IntMainBm> bmList = deptMainDeptService.listByDeptId(v.getDeptId());
                List<IntMainDept> deptBms = new ArrayList<>();
                bmList.stream().forEach(bv -> {
                    IntMainDept dept = new IntMainDept();
                    dept.setDesc1(bv.getCode());
                    dept.setDesc2(bv.getDesc2());
                    dept.setDesc3(bv.getDesc4());
                    dept.setDesc4(bv.getDesc15());
                    dept.setDesc5(bv.getDesc16());
                    dept.setDept(bv.getDesc6());
                    dept.setOptional(true);
                    deptBms.add(dept);
                });
                v.setMainDepts(deptBms);
            });
        }
        return AjaxResult.success(depts);
    }


    @ApiOperation("人力层级列表")
    @GetMapping("/getlistSingerType")
    public AjaxResult getlistSingerType() {
        //   List<SysDept> depts = deptService.selectDeptList(Wrappers.<SysDept>lambdaQuery().equals());
        List<SysDept> depts = deptService.list(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getDeptType, "CENG_JI").eq(SysDept::getDelFlag, 0));
        if(!CollectionUtils.isEmpty(depts) && depts.size() > 0){
            depts.forEach(v -> {
                List<IntMainBm> bmList = deptMainDeptService.listByDeptId(v.getDeptId());
                List<IntMainDept> deptBms = new ArrayList<>();
                bmList.stream().forEach(bv -> {
                    IntMainDept dept = new IntMainDept();
                    dept.setDesc1(bv.getCode());
                    dept.setDesc2(bv.getDesc2());
                    dept.setDesc3(bv.getDesc4());
                    dept.setDesc4(bv.getDesc15());
                    dept.setDesc5(bv.getDesc16());
                    dept.setDept(bv.getDesc6());
                    dept.setOptional(true);
                    deptBms.add(dept);
                });
                v.setMainDepts(deptBms);
            });
        }
        return AjaxResult.success(depts);
    }

    @ApiOperation("层级、部门列表")
    @GetMapping("/listLevelDeptType")
    public AjaxResult listLevelDeptType() {
        //   List<SysDept> depts = deptService.selectDeptList(Wrappers.<SysDept>lambdaQuery().equals());
        LambdaQueryWrapper<SysDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysDept::getDelFlag, 0)
                .and(wrapper -> wrapper.eq(SysDept::getDeptType, "CENG_JI")
                        .or(innerWrapper -> innerWrapper.eq(SysDept::getDeptType, "BU_MEN").eq(SysDept::getParentId, 100)))
                .orderByAsc(SysDept::getOrderNum);
        // select * from sys_dept where del_flag = 0 AND (dept_type = "CENG_JI" or (dept_type = "BU_MEN" AND parent_id = 100 ))
        List<SysDept> depts = deptService.list(lambdaQueryWrapper);
        if(!CollectionUtils.isEmpty(depts) && depts.size() > 0){
            depts.forEach(v -> {
                List<IntMainBm> bmList = deptMainDeptService.listByDeptId(v.getDeptId());
                List<IntMainDept> deptBms = new ArrayList<>();
                bmList.stream().forEach(bv -> {
                    IntMainDept dept = new IntMainDept();
                    dept.setDesc1(bv.getCode());
                    dept.setDesc2(bv.getDesc2());
                    dept.setDesc3(bv.getDesc4());
                    dept.setDesc4(bv.getDesc15());
                    dept.setDesc5(bv.getDesc16());
                    dept.setDept(bv.getDesc6());
                    dept.setOptional(true);
                    deptBms.add(dept);
                });
                v.setMainDepts(deptBms);
            });
        }
        return AjaxResult.success(depts);
    }

}
