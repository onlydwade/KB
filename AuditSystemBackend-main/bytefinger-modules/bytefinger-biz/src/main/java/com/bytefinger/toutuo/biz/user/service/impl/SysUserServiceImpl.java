package com.bytefinger.toutuo.biz.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.core.constant.UserConstants;
import com.bytefinger.common.core.exception.ServiceException;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.utils.TreeUtils;
import com.bytefinger.common.core.web.domain.vo.*;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.biz.user.mapper.SysUserMapper;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserMapper userMapper;

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (!Objects.isNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (!Objects.isNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (!Objects.isNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId) {
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] userIds) {
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }


    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        return rows;
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 获取有此权限的用户
     *
     * @param deptId
     * @param perms
     * @return
     */
    @Override
    public List<Long> getRoleUserList(Long deptId, String perms) {
        return userMapper.getRoleUserList(deptId, perms);
    }

    @Override
    public List<Long> getPermsUserList(String perms) {
        return userMapper.getPermsUserList(perms);
    }


    @Override
    public List<TreeEntityVO> currDeptTreeTwo() {
    /*    Long deptId = SecurityUtils.getLoginUser().getDeptId();
        if (null == deptId) {
            return null;
        }*/

        List<DeptVO> depts = userMapper.listAllDept();
        List<UserVO> userPostAll =userMapper.selectUserDeptAll();
        Map<Long, List<UserVO>> map = userPostAll.stream().collect(Collectors.groupingBy(UserVO::getDeptId));
        for (DeptVO dept : depts) {
            for (Map.Entry<Long, List<UserVO>> entry : map.entrySet()) {
                Long key = entry.getKey();
                List<UserVO> value = entry.getValue();
                if (dept.getDeptId().equals(key)){
                    dept.setUserVOList(value);
                }


            }

        }


        List<TreeEntityVO> treeEntities = depts.stream().map(item -> new TreeEntityVO()
                .setId(item.getDeptId())
                .setName(item.getDeptName())
                .setLevel(item.getLevel())
                .setUserVOList(item.getUserVOList())
                .setParentId(item.getParentId())).collect(Collectors.toList());

        return TreeUtils.list3Tree(treeEntities);
    }

    @Override
    public List<PostUserVO> currPostTree() {
      List<PostUserVO> postUserVOList = userMapper.selectUserPostAll();
      if (CollectionUtils.isEmpty(postUserVOList)){
          return new ArrayList<>();
      }
        Map<Long, List<PostUserVO>> collect = postUserVOList.stream().collect(Collectors.groupingBy(PostUserVO::getPostId));

        List<PostUserVO> postUserTree = new ArrayList<>();
        for (Map.Entry<Long, List<PostUserVO>> longListEntry : collect.entrySet()) {
            Long key = longListEntry.getKey();
            List<PostUserVO> value = longListEntry.getValue();

            PostUserVO postUserVO = new PostUserVO();
            postUserVO.setPostId(key);
            postUserVO.setPostName(value.get(0).getPostName());
            postUserVO.setPostUserVOList(value);
            postUserTree.add(postUserVO);

        }


        return postUserTree;
    }
}
