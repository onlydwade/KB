package com.bytefinger.toutuo.biz.user.service;

import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.SysUserDeptPostInfoDTO;

import java.util.List;
import java.util.Set;

/**
 * 权限信息 服务层
 *
 * @author patrick
 */
public interface ISysPermissionService {

    /**
     * 获取角色数据权限
     *
     * @param user 用户
     * @return 角色权限信息
     */
    String getRolePermission(SysUser user);

    /**
     * 获取菜单数据权限
     *
     * @param postId 岗位ID
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(SysUser user, Long postId);

    /**
     * 获取用户岗位部门列表
     *
     * @param user
     * @return
     */
    List<SysUserDeptPostInfoDTO> getDeptPostList(SysUser user);

    /**
     * 获取用户岗位部门
     *
     * @param user
     * @param deptId
     * @param postId
     * @return
     */
    SysUserDeptPostInfoDTO getDeptPost(SysUser user, Long deptId, Long postId);

    /**
     * 修改岗位部门最近登录时间
     *
     * @param sysUserDeptPostInfo
     */
    void updateDeptPostTime(SysUserDeptPostInfoDTO sysUserDeptPostInfo);
}
