package com.bytefinger.toutuo.biz.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.web.domain.vo.PostUserVO;
import com.bytefinger.common.core.web.domain.vo.TreeEntityVO;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    String checkUserNameUnique(SysUser user);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    String checkPhoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    String checkEmailUnique(SysUser user);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserProfile(SysUser user);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    public int resetPwd(SysUser user);

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(String userName, String password);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    public void checkUserAllowed(SysUser user);


    List<Long> getRoleUserList(Long deptId, String perms);

    List<Long> getPermsUserList(String perms);

    public List<TreeEntityVO> currDeptTreeTwo();

    List<PostUserVO> currPostTree();

}
