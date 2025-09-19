package com.bytefinger.toutuo.biz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.common.core.web.domain.vo.DeptVO;
import com.bytefinger.common.core.web.domain.vo.PostUserVO;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.SysUserDeptPostInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 通过部门id + 菜单权限获取对应用户
     *
     * @param deptId
     * @param perms
     * @return
     */
    List<Long> getRoleUserList(@Param("deptId") Long deptId, @Param("perms") String perms);

    /**
     * 通过部门列表 + 权限标识获取用户角色列表
     *
     * @param deptIds
     * @param perms
     * @return
     */
    List<SysUserDeptPostInfoDTO> getRoleList(@Param("deptIds") List<Long> deptIds, @Param("perms") String perms);

    List<Long> getPermsUserList(@Param("perms") String perms);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public SysUser checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public SysUser checkEmailUnique(String email);

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
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

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

    List<DeptVO> listAllDept();

    List<UserVO> selectUserDeptAll();

    List<PostUserVO> selectUserPostAll();

}
