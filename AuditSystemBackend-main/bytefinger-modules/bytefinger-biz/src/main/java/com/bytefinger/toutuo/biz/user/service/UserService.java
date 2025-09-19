package com.bytefinger.toutuo.biz.user.service;

import com.bytefinger.common.core.web.domain.vo.DeptVO;
import com.bytefinger.common.core.web.domain.vo.PostVO;
import com.bytefinger.common.core.web.domain.vo.TreeEntity;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.toutuo.common.domain.dto.UserRoleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户模块
 *
 * @author pat
 * @date 2022/10/19 13:16
 **/
public interface UserService {

    /**
     * 通过用户ids查询用户列表
     *
     * @param userIds
     * @return
     */
    List<UserVO> listUserByIds(List<Long> userIds);

    /**
     * 通过部门ids查询部门列表
     *
     * @param deptIds
     * @return
     */
    List<DeptVO> listDeptByIds(List<Long> deptIds);

    /**
     * 通过部门ids查询岗位列表
     *
     * @param postIds
     * @return
     */
    List<PostVO> listPostByIds(List<Long> postIds);

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    UserVO getUser(Long userId);

    /**
     * 获取部门以及部门一下的员工数据
     *
     * @param deptId
     * @return
     */
    List<UserVO> getUserByListDeptId(Long deptId);

    /**
     * 查询指定部门以及以上的数据
     *
     * @param deptId
     * @return
     */
    List<UserRoleDTO> findSuperiorsRolePermsUser(Long deptId, String... perms);

    /**
     * 获取指定部门下的所有子节点
     *
     * @param deptId
     * @return
     */
    List<Long> deptTreeByDeptId(Long deptId);

    /**
     * 获取当前登录人角色的部门树-到指定层级
     *
     * @param level
     * @return
     */
    TreeEntity currDeptTree(Integer level);


    /**
     * 获取到指定层级部门
     *
     * @param level
     * @return
     */
    TreeEntity getCurrDeptTree(Integer level);

    /**
     * 获取当前登录人的部门菜单树
     *
     * @return
     */
    TreeEntity currDeptTree();

    DeptVO getDept(@Param("id") Long id);

    List<UserVO> getUserByDeptId(Long deptId);

    TreeEntity getSelectDeptTree();

    TreeEntity deptTree();

    /**
     * 通过部门信息获取对应部门负责人
     *
     * @return
     */
    List<UserVO> getUserPrincipalByDeptId(DeptVO deptVO);

    List<UserVO> getUserPrincipalId(UserVO userVO);
    /**
     * 查出根据岗位编码查出该岗位下的人员列表*
     *
     * @param deptName 部门名称
     * @return
     */
    List<Long> selectHeadOperateRoleUserId(String deptName);


    public TreeEntity deptTreeTwo();
    public TreeEntity currDeptTreeTwo();
}


