package com.bytefinger.toutuo.biz.user.controller;

import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.toutuo.api.biz.user.domain.SysUserDeptPost;
import com.bytefinger.toutuo.biz.user.domain.dto.UserDeptDTO;
import com.bytefinger.toutuo.biz.user.domain.dto.UserDeptPostDTO;
import com.bytefinger.toutuo.biz.user.service.ISysUserDeptPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户授权
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
@Slf4j
@Api(tags = "用户")
@AllArgsConstructor
@RestController
@RequestMapping("/userDeptPost")
public class SysUserDeptPostController extends BaseController {

    private final ISysUserDeptPostService sysUserDeptPostService;

    @ApiOperation(value = "批量为用户添加部门")
    @PutMapping("/addDept")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult addDept(@Validated @RequestBody UserDeptDTO userDeptDTO) {
        return sysUserDeptPostService.saveOrUpdate(userDeptDTO).toAjaxResult();
    }

    @ApiOperation(value = "批量为用户添加部门和岗位")
    @PutMapping("/addDeptPost")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult addDeptPost(@Validated @RequestBody UserDeptPostDTO userDeptPostDTO) {
        return sysUserDeptPostService.saveOrUpdate(userDeptPostDTO).toAjaxResult();
    }

    @ApiOperation(value = "批量删除用户部门和岗位")
    @DeleteMapping("/deleteDeptPost")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult deleteDeptPost(@Validated @RequestBody UserDeptPostDTO userDeptPostDTO) {
        sysUserDeptPostService.deleteByIds(userDeptPostDTO);
        return success();
    }

    @ApiOperation(value = "列表-系统用户部门岗位表")
    @GetMapping("/list/{userId}")
    public AjaxResult list(@PathVariable("userId") Long userId) {
        return success(sysUserDeptPostService.listByUserId(userId));
    }

    @ApiOperation(value = "修改-系统用户部门岗位表")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody SysUserDeptPost sysUserDeptPost) {
        sysUserDeptPostService.updateById(sysUserDeptPost);
        return success("更新成功");
    }

    @ApiOperation(value = "详情-系统用户部门岗位表")
    @GetMapping("/get/{userId}/{deptId}/{postId}")
    public AjaxResult get(@PathVariable("userId") Long userId, @PathVariable("deptId") Long deptId, @PathVariable("postId") Long postId) {
        return success(sysUserDeptPostService.getById(userId, deptId, postId));
    }


}
