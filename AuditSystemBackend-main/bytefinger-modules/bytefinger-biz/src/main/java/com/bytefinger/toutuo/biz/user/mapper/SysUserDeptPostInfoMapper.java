package com.bytefinger.toutuo.biz.user.mapper;

import com.bytefinger.toutuo.api.biz.user.domain.dto.SysUserDeptPostInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
@Mapper
public interface SysUserDeptPostInfoMapper {

    /**
     * 查询一条数据
     *
     * @param userId
     * @param deptId
     * @param postId
     * @return
     */
    SysUserDeptPostInfoDTO selectOne(@Param("userId") Long userId, @Param("deptId") Long deptId, @Param("postId") Long postId);

    /**
     * 查询当前用户角色列表
     *
     * @param userId
     * @return
     */
    List<SysUserDeptPostInfoDTO> selectList(@Param("userId") Long userId);

}
