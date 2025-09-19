package com.bytefinger.toutuo.biz.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.constant.Constants;
import com.bytefinger.common.security.utils.SysConfigUtils;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.SysUserDeptPost;
import com.bytefinger.toutuo.api.biz.user.domain.dto.SysUserDeptPostInfoDTO;
import com.bytefinger.toutuo.biz.user.service.ISysMenuService;
import com.bytefinger.toutuo.biz.user.service.ISysPermissionService;
import com.bytefinger.toutuo.biz.user.service.ISysUserDeptPostService;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author patrick
 */
@Service
@AllArgsConstructor
public class SysPermissionServiceImpl implements ISysPermissionService {

    private final ISysMenuService menuService;

    private final ISysUserDeptPostService userDeptPostService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户
     * @return 角色权限信息
     */
    @Override
    public String getRolePermission(SysUser user) {
        return user.isAdmin() ? Constants.ADMIN_USER_FLAG : SysConfigUtils.getConfigCache(Constants.COMMON_USER_FLAG);
    }

    /**
     * 获取用户岗位部门列表
     *
     * @param user
     * @return
     */
    @Override
    public List<SysUserDeptPostInfoDTO> getDeptPostList(SysUser user) {
        return user.isAdmin() ? Lists.newArrayList() : userDeptPostService.listByUserId(user.getUserId());
    }

    /**
     * 获取用户岗位部门
     *
     * @param user
     * @param deptId
     * @param postId
     * @return
     */
    @Override
    public SysUserDeptPostInfoDTO getDeptPost(SysUser user, Long deptId, Long postId) {
        return user.isAdmin() ? null : userDeptPostService.getById(user.getUserId(), deptId, postId);
    }

    /**
     * 获取菜单数据权限
     *
     * @param postId 岗位ID
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(SysUser user, Long postId) {
        return user.isAdmin() ? Sets.newHashSet(Constants.ADMIN_USER_PERMISSION_FLAG) : menuService.selectMenuPermsByPostId(postId);
    }

    /**
     * 修改岗位部门最近登录时间
     *
     * @param sysUserDeptPostInfo
     */
    @Async
    @Override
    public void updateDeptPostTime(SysUserDeptPostInfoDTO sysUserDeptPostInfo) {
        if (null != sysUserDeptPostInfo && null != sysUserDeptPostInfo.getUserId() && null != sysUserDeptPostInfo.getDeptId() && null != sysUserDeptPostInfo.getPostId()) {
            sysUserDeptPostInfo.setUpdateTime(new Date());
            userDeptPostService.update(Wrappers.<SysUserDeptPost>lambdaUpdate()
                    .eq(SysUserDeptPost::getUserId, sysUserDeptPostInfo.getUserId())
                    .eq(SysUserDeptPost::getDeptId, sysUserDeptPostInfo.getDeptId())
                    .eq(SysUserDeptPost::getPostId, sysUserDeptPostInfo.getPostId())
                    .set(SysUserDeptPost::getUpdateTime, new Date()));
        }
    }

}
