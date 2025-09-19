package com.bytefinger.toutuo.biz.user.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.constant.Constants;
import com.bytefinger.common.core.constant.UserConstants;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.SysMenu;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.biz.user.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 * @author patrick
 */
@Api(tags = "菜单")
@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class SysMenuController extends BaseController {

    private final ISysMenuService menuService;

    @ApiOperation("新增菜单")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PutMapping("/add")
    @RequiresPermissions("system:menu:add")
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    @ApiOperation("修改菜单")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @RequiresPermissions("system:menu:update")
    public AjaxResult update(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    @ApiOperation("删除菜单")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{menuId}")
    @RequiresPermissions("system:menu:delete")
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistPost(menuId)) {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }

    @ApiOperation("获取菜单列表-当前登录人")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<SysMenu> menus = menuService.selectMenuList(menu, loginUser.getUserid(), loginUser.getDeptId(), loginUser.getPostId());
        return AjaxResult.success(menus);
    }

    @ApiOperation("获取路由信息-当前登录人")
    @GetMapping("/getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(loginUser.getUserid(), loginUser.getDeptId(), loginUser.getPostId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    @ApiOperation("根据菜单编号获取详细信息")
    @GetMapping(value = "/get/{menuId}")
    @RequiresPermissions("system:menu:get")
    public AjaxResult get(@PathVariable Long menuId) {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    @ApiOperation("获取菜单下拉树列表-当前登录人")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<SysMenu> menus = menuService.selectMenuList(menu, loginUser.getUserid(), loginUser.getDeptId(), loginUser.getPostId());
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    @ApiOperation(value = "加载对应岗位菜单列表树-当前登录人")
    @GetMapping(value = "/postMenuTreeselect")
    public AjaxResult postMenuTreeselect() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<SysMenu> menus = menuService.selectMenuList(loginUser.getUserid(), loginUser.getDeptId(), loginUser.getPostId());
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    @ApiOperation(value = "获取数据权限标识")
    @GetMapping(value = "/dataPermission")
    @RequiresPermissions("system:menu:get")
    public AjaxResult dataPermission() {
        SysMenu sysMenu = menuService.getOne(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getPerms, Constants.DATA_ROLE_KEY));
        if (null != sysMenu) {
            return AjaxResult.success(menuService.list(Wrappers.<SysMenu>lambdaQuery()
                    .eq(SysMenu::getParentId, sysMenu.getMenuId())
                    .orderByAsc(SysMenu::getOrderNum)));
        } else {
            return AjaxResult.success();
        }
    }


}