package com.bytefinger.toutuo.biz.user.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.constant.Constants;
import com.bytefinger.common.core.constant.UserConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.core.enums.UserStatus;
import com.bytefinger.common.core.exception.AuthException;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.utils.bean.BeanUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.IntMainBm;
import com.bytefinger.toutuo.api.biz.user.domain.SysDept;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.SysUserDeptPostInfoDTO;
import com.bytefinger.toutuo.biz.external.service.IIntMainBmService;
import com.bytefinger.toutuo.biz.user.service.ISysDeptService;
import com.bytefinger.toutuo.biz.user.service.ISysPermissionService;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import com.bytefinger.toutuo.biz.workbrief.dto.GroupPushDto;
import com.bytefinger.toutuo.biz.workbrief.service.IGroupInfoService;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Slf4j
@Api(tags = "用户")
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    private final ISysUserService userService;

    private final ISysDeptService deptService;

    private final IIntMainBmService intMainBmService;

    private final ISysPermissionService permissionService;

    private final IGroupInfoService groupInfoService;

    @ApiOperation(value = "获取当前登录的用户信息")
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        // 用户信息
        SysUser user = this.userService.getById(SecurityUtils.getUserId());

        // 用户角色标识
        String role = this.permissionService.getRolePermission(user);

        Set<String> permissions = Sets.newHashSet();
        List<SysUserDeptPostInfoDTO> deptPostPermissions = Lists.newArrayList();
        SysUserDeptPostInfoDTO currSysUserDeptPostInfo = null;

        // 是超管
        if (user.isAdmin()) {
            permissions.add(Constants.ADMIN_USER_PERMISSION_FLAG);
        } else {
            deptPostPermissions = this.permissionService.getDeptPostList(user);

            // 当前登录的部门岗位
            LoginUser loginUser = SecurityUtils.getLoginUser();
            currSysUserDeptPostInfo = this.permissionService.getDeptPost(loginUser.getSysUser(), loginUser.getDeptId(), loginUser.getPostId());

            // 当前登录的角色被调整
            if (null == currSysUserDeptPostInfo || null == currSysUserDeptPostInfo.getPostId()) {
                throw new AuthException("您的权限已被修改，请重新登录系统");
            }

            // 权限集合
            permissions.addAll(this.permissionService.getMenuPermission(user, currSysUserDeptPostInfo.getPostId()));
        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("role", role);
        ajax.put("permissions", permissions);
        ajax.put("deptPosts", deptPostPermissions);
        ajax.put("loginDeptPost", currSysUserDeptPostInfo);

        return ajax;
    }

    @ApiOperation(value = "分页查询用户信息")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {

        // 存在部门id则查询当前部门下所有用户
        List<Long> childDeptIds = Lists.newArrayList();
        if (queryPage.getParams().containsKey("deptId")) {
            Integer deptId = (Integer) queryPage.getParams().get("deptId");
            if (null != deptId && deptId > 0) {
                queryPage.getParams().remove("deptId");
                childDeptIds.addAll(this.deptService.getChildDeptIds(Integer.toUnsignedLong(deptId)));
            }
        }

        List<Long> childUserIds = Lists.newArrayList();
        if (queryPage.getInParams().containsKey("userIds")) {
            List<Object> userIds = queryPage.getInParams().get("userIds");
            if (null != userIds) {
                for (Object obj : userIds) {
                    childUserIds.add(Long.valueOf(obj.toString()));
                }
                queryPage.getInParams().remove("userIds");
            }
        }

        QueryWrapper<SysUser> wrapper = queryPage.getWrapper();
        wrapper.lambda().ne(SysUser::getUserId, 1).eq(SysUser::getDelFlag, Deleted.NO.getCode());
        if (!childDeptIds.isEmpty()) {
            wrapper.lambda().in(SysUser::getDeptId, childDeptIds);
        }
        if (!childUserIds.isEmpty()) {
            wrapper.lambda().in(SysUser::getUserId, childUserIds);
        }

        Page<SysUser> page = userService.page(queryPage.toPage(), wrapper);
        List<Long> deptIds = page.getRecords().stream().map(SysUser::getDeptId).collect(Collectors.toList());

        if (!deptIds.isEmpty()) {
            List<SysDept> list = deptService.list(Wrappers.<SysDept>lambdaQuery().in(SysDept::getDeptId, deptIds));

            if (!list.isEmpty()) {
                for (SysUser record : page.getRecords()) {
                    for (SysDept sysDept : list) {
                        if (sysDept.getDeptId().equals(record.getDeptId())) {
                            record.setDeptName(sysDept.getDeptName());
                        }
                    }
                }
            }
        }

        List<String> mainDeptIds = page.getRecords().stream().map(SysUser::getMainDeptId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(mainDeptIds)) {
            List<IntMainBm> list = intMainBmService.list(Wrappers.<IntMainBm>lambdaQuery().in(IntMainBm::getCode, mainDeptIds));

            if (!list.isEmpty()) {
                for (SysUser record : page.getRecords()) {
                    for (IntMainBm bm : list) {
                        if (null != bm.getCode() && bm.getCode().equals(record.getMainDeptId())) {
                            record.setMainDeptName(bm.getDesc2());
                        }
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            for (SysUser record : page.getRecords()) {
                record.setRealname(record.getRealname() + "(" + record.getUserName() + ")");
            }
        }

        return success(page);
    }

    @ApiOperation(value = "分页查询用户和分组信息")
    @PostMapping("/groupPage")
    public AjaxResult groupPage(@RequestBody QueryPage queryPage) {

        // 存在部门id则查询当前部门下所有用户
        List<Long> childDeptIds = Lists.newArrayList();
        if (queryPage.getParams().containsKey("deptId")) {
            Integer deptId = (Integer) queryPage.getParams().get("deptId");
            if (null != deptId && deptId > 0) {
                queryPage.getParams().remove("deptId");
                childDeptIds.addAll(this.deptService.getChildDeptIds(Integer.toUnsignedLong(deptId)));
            }
        }

        List<Long> childUserIds = Lists.newArrayList();
        List<String> childGroupIds = Lists.newArrayList();
        if (queryPage.getInParams().containsKey("userIds")) {
            List<Object> userIds = queryPage.getInParams().get("userIds");
            if (null != userIds) {
                for (Object obj : userIds) {
                    String idStr = obj.toString();
                    if (idStr.contains("group_user_"))
                        continue;
                    if (!idStr.contains("group_"))
                        childUserIds.add(Long.valueOf(idStr));
                    else
                        childGroupIds.add(idStr);
                }
                queryPage.getInParams().remove("userIds");
            }
        }

        QueryWrapper<SysUser> wrapper = queryPage.getWrapper();
        wrapper.lambda().ne(SysUser::getUserId, 1).eq(SysUser::getDelFlag, Deleted.NO.getCode());
        if (!childDeptIds.isEmpty()) {
            wrapper.lambda().in(SysUser::getDeptId, childDeptIds);
        }
        if (!childUserIds.isEmpty()) {
            wrapper.lambda().in(SysUser::getUserId, childUserIds);
        }

        Page<SysUser> page = userService.page(queryPage.toPage(), wrapper);
        List<Long> deptIds = page.getRecords().stream().map(SysUser::getDeptId).collect(Collectors.toList());

        if (!deptIds.isEmpty()) {
            List<SysDept> list = deptService.list(Wrappers.<SysDept>lambdaQuery().in(SysDept::getDeptId, deptIds));

            if (!list.isEmpty()) {
                for (SysUser record : page.getRecords()) {
                    for (SysDept sysDept : list) {
                        if (sysDept.getDeptId().equals(record.getDeptId())) {
                            record.setDeptName(sysDept.getDeptName());
                        }
                    }
                }
            }
        }

        List<String> mainDeptIds = page.getRecords().stream().map(SysUser::getMainDeptId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(mainDeptIds)) {
            List<IntMainBm> list = intMainBmService.list(Wrappers.<IntMainBm>lambdaQuery().in(IntMainBm::getCode, mainDeptIds));

            if (!list.isEmpty()) {
                for (SysUser record : page.getRecords()) {
                    for (IntMainBm bm : list) {
                        if (null != bm.getCode() && bm.getCode().equals(record.getMainDeptId())) {
                            record.setMainDeptName(bm.getDesc2());
                        }
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            for (SysUser record : page.getRecords()) {
                record.setRealname(record.getRealname() + "(" + record.getUserName() + ")");
            }
        }


        List<GroupPushDto> result = new ArrayList<>();

        result.addAll(groupInfoService.getGroupPush(childGroupIds,queryPage.getContent()));

        page.getRecords().forEach(o -> {
            GroupPushDto item = new GroupPushDto();
            item.setUserId(o.getUserId().toString());
            item.setDeptId(o.getDeptId().toString());
            item.setRealname(o.getRealname());
            item.setDeptName(o.getDeptName());
            item.setCompanyName(o.getCompanyName());
            result.add(item);
        });
        Page<GroupPushDto> pageResult = new Page<>();
        pageResult.setRecords(result);
        return success(pageResult);
    }

    @ApiOperation(value = "通过用户id获取用户信息")
    @GetMapping(value = "/get/{userId}")
    @RequiresPermissions("system:user:get")
    public AjaxResult get(@PathVariable(value = "userId") Long userId) {
        SysUser user = userService.getById(userId);
        if (null == user || user.getDelFlag().equals(Deleted.YES.getCode().toString())) {
            return error("用户不存在");
        }
        return success(user);
    }

    @ApiOperation("新增用户")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PutMapping("/add")
    @RequiresPermissions("system:user:add")
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    @ApiOperation("修改用户")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @RequiresPermissions("system:user:update")
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    @ApiOperation("重置用户密码")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    @RequiresPermissions("system:user:update")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    @ApiOperation("删除多个用户")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{userIds}")
    @RequiresPermissions("system:user:delete")
    public AjaxResult delete(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, SecurityUtils.getUserId())) {
            return AjaxResult.error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }


    @ApiOperation(value = "获取人员组织树", hidden = true)
    @GetMapping("/currDeptTreeTwo")
    public AjaxResult currDeptTreeTwo() {
        return AjaxResult.success(userService.currDeptTreeTwo());
    }


    @ApiOperation(value = "岗位树", hidden = true)
    @GetMapping("/currPostTree")
    public AjaxResult currPostTree() {
        return AjaxResult.success(userService.currPostTree());
    }


    /********************************************** inner auth ****************************************************************/
    @ApiOperation(value = "判断用户是否有此角色", hidden = true)
    @InnerAuth
    @GetMapping("/getUserByListDeptId/{deptId}")
    public R<List<UserVO>> getUserByListDeptId(@PathVariable("deptId") Long deptId) {
        // 存在部门id则查询当前部门下所有用户
        List<Long> childDeptIds = this.deptService.getChildDeptIds(deptId);
        List<UserVO> result = Lists.newArrayList();
        if (!childDeptIds.isEmpty()) {
            List<SysUser> list = userService.list(Wrappers.<SysUser>lambdaQuery()
                    .ne(SysUser::getUserId, 1)
                    .eq(SysUser::getDelFlag, Deleted.NO.getCode())
                    .in(SysUser::getDeptId, childDeptIds));

            for (SysUser sysUser : list) {
                UserVO userVO = new UserVO();
                BeanUtils.copyBeanProp(userVO, sysUser);
                result.add(userVO);
            }
        }

        return R.ok(result);
    }

    /**
     * 判断用户是否有此角色
     *
     * @param deptId
     * @param postId
     * @return
     */
    @ApiOperation(value = "判断用户是否有此角色", hidden = true)
    @InnerAuth
    @GetMapping("/authRole/{userId}/{deptId}/{postId}")
    public R<LoginUser> getInfo(@PathVariable("userId") Long userId, @PathVariable("deptId") Long
            deptId, @PathVariable("postId") Long postId) {
        SysUser user = userService.getById(userId);
        if (null != user) {

            SysUserDeptPostInfoDTO sysUserDeptPostInfo = this.permissionService.getDeptPost(user, deptId, postId);
            if (null != sysUserDeptPostInfo) {
                return R.ok(this.setRole(user, sysUserDeptPostInfo));
            }
        }

        return R.fail("切换失败，请稍后再试");
    }

    /**
     * 获取用户基本信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取用户信息", hidden = true)
    @InnerAuth
    @GetMapping("/getInfo/{id}")
    public R<UserVO> getInfo(@PathVariable("id") Long id) {
        SysUser sysUser = userService.getById(id);
        UserVO userVO = null;

        if (null != sysUser) {
            userVO = new UserVO();
            BeanUtils.copyBeanProp(userVO, sysUser);
        }

        return null == userVO ? R.fail() : R.ok(userVO);
    }

    /**
     * 通过手机号码获取用户信息
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "通过手机号码获取用户信息", hidden = true)
    @InnerAuth
    @GetMapping("/getInfoByPhone/{phone}")
    public R<SysUser> getInfoByPhone(@PathVariable("phone") String phone) {
        SysUser sysUser = userService.getOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getDelFlag, Deleted.NO.getCode())
                .eq(SysUser::getStatus, UserStatus.OK.getCode())
                .and(wrappers -> wrappers.eq(SysUser::getUserName, phone).or().eq(SysUser::getPhonenumber, phone))
        );

        if (StringUtils.isNull(sysUser)) {
            return R.fail("用户不存在.");
        }

        return null == sysUser ? R.fail() : R.ok(sysUser);
    }

    /**
     * 获取指定权限的用户数据
     *
     * @param deptId
     * @param perms
     * @return
     */
    @ApiOperation(value = "获取指定权限的用户数据", hidden = true)
    @InnerAuth
    @GetMapping("/getRoleUserList/{deptId}/{perms}")
    public R<List<Long>> getRoleUserList(@PathVariable("deptId") Long deptId, @PathVariable("perms") String perms) {
        return R.ok(userService.getRoleUserList(deptId, perms));
    }

    /**
     * 获取指定权限的用户数据
     *
     * @param perms
     * @return
     */
    @ApiOperation(value = "获取指定权限的用户数据", hidden = true)
    @InnerAuth
    @GetMapping("/getPermsUserList/{perms}")
    public R<List<Long>> getPermsUserList(@PathVariable("perms") String perms) {
        return R.ok(userService.getPermsUserList(perms));
    }


    /**
     * 获取用户基本信息
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "获取用户信息", hidden = true)
    @InnerAuth
    @PostMapping("/list")
    public R<List<UserVO>> list(@RequestBody List<Long> ids) {
        List<SysUser> list = userService.list(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, ids));
        List<UserVO> result = Lists.newArrayList();
        for (SysUser sysUser : list) {
            UserVO userVO = new UserVO();
            BeanUtils.copyBeanProp(userVO, sysUser);
            result.add(userVO);
        }
        return R.ok(result);
    }

    @ApiOperation(value = "获取当前用户信息 + 角色、权限", hidden = true)
    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginUser> info(@PathVariable("username") String username) {
        SysUser user = userService.getOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getDelFlag, Deleted.NO.getCode())
                .eq(SysUser::getStatus, UserStatus.OK.getCode())
                .and(wrappers -> wrappers.eq(SysUser::getUserName, username).or().eq(SysUser::getPhonenumber, username))
        );
        if (StringUtils.isNull(user)) {
            return R.fail("用户名密码错误或账号已禁用.");
        }
        return R.ok(this.setRole(user, null));
    }

    private LoginUser setRole(SysUser user, SysUserDeptPostInfoDTO currSysUserDeptPostInfo) {
        LoginUser loginUser = new LoginUser();
        Set<String> permissions = Sets.newHashSet();
        Set<Long> subDeptId = Sets.newHashSet();

        // 是超管
        if (user.isAdmin()) {
            permissions.add(Constants.ADMIN_USER_PERMISSION_FLAG);
        } else {
            // 获取部门岗位列表
            if (null == currSysUserDeptPostInfo) {
                List<SysUserDeptPostInfoDTO> deptPostPermissions = this.permissionService.getDeptPostList(user);
                log.info("*******************deptPostPermissions:{}", deptPostPermissions);
                if (CollUtil.isNotEmpty(deptPostPermissions)) {
                    currSysUserDeptPostInfo = deptPostPermissions.get(0);
                }
            }

            if (null != currSysUserDeptPostInfo) {
                loginUser.setDeptId(currSysUserDeptPostInfo.getDeptId());
                loginUser.setPostId(currSysUserDeptPostInfo.getPostId());
                loginUser.setPostCode(currSysUserDeptPostInfo.getPostCode());

                // 权限集合
                log.info("***********************************查询权限 ******************************************");
                permissions.addAll(this.permissionService.getMenuPermission(user, currSysUserDeptPostInfo.getPostId()));
                log.info("**********************************************permissions:{}", permissions);
                this.permissionService.updateDeptPostTime(currSysUserDeptPostInfo);
                log.info("**********************************************permissions:{}", permissions);
                // 设置下级部门id
                List<Long> childDeptIds = deptService.getChildDeptIds(currSysUserDeptPostInfo.getDeptId());
                subDeptId.add(currSysUserDeptPostInfo.getDeptId());
                subDeptId.addAll(childDeptIds);
            }
        }

        loginUser.setSysUser(user);
        loginUser.setPermissions(permissions);
        loginUser.setSubDeptIds(subDeptId);
        return loginUser;
    }

}