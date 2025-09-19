package com.bytefinger.toutuo.biz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.common.core.web.domain.vo.DeptVO;
import com.bytefinger.common.core.web.domain.vo.PostVO;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.SysUserDeptPostInfoDTO;
import com.bytefinger.toutuo.common.domain.dto.UserRoleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 用户信息、角色信息
 *
 * @author Patrick
 * @date 2022/11/4 10:52
 **/
@Mapper
public interface UserMapper extends BaseMapper<UserVO> {

    /**
     * 通过用户ids查询用户信息
     *
     * @param ids
     * @return
     */
    @Select({
            "<script>",
            "select u.user_id,u.dept_id,u.user_name,u.nick_name,u.realname,u.avatar",
            "from sys_user u where ",
            "u.user_id in",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    List<UserVO> listUserByIds(@Param("ids") List<Long> ids);

    @Select({
            "<script>",
            "select u.user_id,u.dept_id,u.user_name,u.nick_name,u.realname,u.avatar",
            "from sys_user u where ",
            "u.user_id = #{userId}",
            "</script>"
    })
    UserVO userById(@Param("userId") Long userId);

    /**
     * 通过用户部门查询用户信息
     *
     * @param ids
     * @return
     */
    @Select({
            "<script>",
            "select d.dept_id,d.parent_id,d.ancestors,d.dept_name,d.order_num,d.leader,d.level",
            "from sys_dept d where ",
            "d.dept_id in",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    List<DeptVO> listDeptByIds(@Param("ids") List<Long> ids);

    /**
     * 通过部门列表 + 权限标识获取用户角色列表
     *
     * @param deptIds
     * @param perms
     * @return
     */
    List<SysUserDeptPostInfoDTO> getRoleList(@Param("deptIds") List<Long> deptIds, @Param("perms") String perms);

    /**
     * 获取有法人主体的公司
     *
     * @return
     */
    List<DeptVO> listBorrowedDept();

    /**
     * 获取有法人主体的公司
     *
     * @param ids
     * @return
     */
    List<DeptVO> listBorrowedDeptIds(@Param("ids") List<Long> ids);

    /**
     * 部门对应法人主体id
     *
     * @param deptId
     * @return
     */
    DeptVO getJuridicalByDeptId(Long deptId);

    /**
     * 验证是否接法人主体审批
     *
     * @param deptId
     * @param borrowDeptId
     * @return
     */
    int validBorrowDept(@Param("deptId") Long deptId, @Param("borrowDeptId") Long borrowDeptId);

    /**
     * 获取全部数据
     *
     * @return
     */
    @Select({
            "<script>",
            "select d.dept_id,d.parent_id,d.ancestors,d.dept_name,d.order_num,d.leader,d.level,d.dept_type",
            "from sys_dept d where d.del_flag = '0'",
            "</script>"
    })
    List<DeptVO> listAllDept();

    /**
     * 获取全部数据
     *
     * @return
     */
    @Select({
            "<script>",
            "select d.dept_id,d.parent_id,d.ancestors,d.dept_name,d.order_num,d.leader,d.level",
            "from sys_dept d where ",
            "d.level = #{level}",
            "</script>"
    })
    List<DeptVO> listDeptByLevel(@Param("level") Integer level);

    /**
     * 通过用户部门查询用户信息
     *
     * @param ids
     * @return
     */
    @Select({
            "<script>",
            "select p.post_id,p.post_name",
            "from sys_post p where ",
            "p.post_id in",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    List<PostVO> listPostByIds(@Param("ids") List<Long> ids);

    /**
     * @param ids
     * @return
     */
    List<DeptVO> listDeptAncestors(@Param("ids") List<Long> ids);

    /**
     * 通过 id查 Dept
     *
     * @param id
     * @return
     */
    @Select({
            "<script>",
            "select d.dept_id,d.parent_id,d.ancestors,d.dept_name,d.order_num,d.leader,d.level",
            "from sys_dept d where ",
            "d.dept_id = #{id}",
            "</script>"
    })
    DeptVO getDept(@Param("id") Long id);

    /**
     * 通过指定权限查询上级
     *
     * @param ids
     * @param perms
     * @return
     */
    @Select({
            "<script>",
            "SELECT DISTINCT sudp.user_id as userId,sd.level,sd.dept_id deptId FROM sys_user_dept_post sudp",
            "INNER JOIN sys_dept sd ON ( sudp.dept_id = sd.dept_id )",
            "INNER JOIN sys_post sp ON ( sudp.post_id = sp.post_id )",
            "INNER JOIN sys_post_menu spm ON ( spm.post_id = sp.post_id )",
            "INNER JOIN sys_menu sm ON ( sm.menu_id = spm.menu_id )",
            "INNER JOIN sys_user su ON ( su.user_id = sudp.user_id )",
            "WHERE  su.status=0 and  sm.perms IN",
            "<foreach collection='perms' index='index' item='item' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "AND sd.dept_id IN",
            "<foreach collection='ids' index='index' item='item' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "ORDER BY sd.level desc, sd.dept_id desc",
            "</script>"
    })
    List<UserRoleDTO> listByPerms(@Param("ids") List<Long> ids, @Param("perms") String[] perms);

    /**
     * 通过父节点获取子节点部门
     *
     * @param deptId
     * @return
     */
    List<DeptVO> listDeptByParentId(@Param("deptId") Long deptId);

    /**
     * 查询当前部门的子节点
     *
     * @param deptId
     * @return
     */
    @Select({
            "<script>",
            "select d.dept_id,",
            "d.parent_id,",
            "d.ancestors,",
            "d.dept_name,",
            "d.order_num,",
            "d.leader,",
            "d.phone,",
            "d.email,",
            "d.status,",
            "d.level,",
            "d.del_flag,",
            "d.create_by,",
            "d.create_time",
            "from sys_dept d",
            "where d.del_flag = '0'",
            "and FIND_IN_SET(#{deptId}, d.ancestors)",
            "OR d.dept_id = #{deptId}",
            "</script>"
    })
    List<DeptVO> getChildDeptIds(Long deptId);

    /**
     * 通过部门id获取用户列表
     *
     * @param deptId
     * @return
     */
        @Select({
                "<script>",
                "SELECT su.user_id AS userId,su.realname AS realname FROM sys_user_dept_post sudp INNER JOIN sys_user su ON ( sudp.user_id = su.user_id )",
                "WHERE sudp.dept_id = #{deptId}",
                "</script>"
        })
      List<UserVO> listUsersByDeptId(@Param("deptId") Long deptId);

    /**
     * 通过部门ids获取用户列表
     *
     * @param deptIds
     * @return
     */
    @Select({
            "<script>",
            "select u.user_id,u.dept_id,u.user_name,u.nick_name,u.realname,u.avatar",
            "from sys_user u where ",
            "u.dept_id in",
            "<foreach item='item' index='index' collection='deptIds' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    List<UserVO> listUsersByDeptIds(@Param("deptIds") List<Long> deptIds);


    List<UserVO> getUserPrincipalByDeptId(DeptVO deptVO);


    List<UserVO> getUserPrincipalId(UserVO userVO);

    /**
     * 查出根据岗位编码查出该岗位下的人员列表*
     * @param deptName
     * @return
     */
    @Select({
            "<script>",
            " select sudp.user_id ",
            " FROM sys_user_dept_post sudp LEFT JOIN sys_dept sd on sudp.dept_id = sd.dept_id ",
            " LEFT JOIN sys_user su ON sudp.user_id = su.user_id ",
            " WHERE sd.dept_name = #{deptName} AND su.del_flag = 0 AND su.`status` = 0 and sd.parent_id = 100 and su.dept_id = sd.dept_id ",
            "</script>"
    })
    List<Long> selectHeadOperateRoleUserId(@Param("deptName") String deptName);}
