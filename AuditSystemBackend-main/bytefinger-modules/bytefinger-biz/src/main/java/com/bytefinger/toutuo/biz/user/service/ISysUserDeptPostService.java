package com.bytefinger.toutuo.biz.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.user.domain.SysUserDeptPost;
import com.bytefinger.toutuo.api.biz.user.domain.dto.SysUserDeptPostInfoDTO;
import com.bytefinger.toutuo.biz.user.domain.dto.UserDeptDTO;
import com.bytefinger.toutuo.biz.user.domain.dto.UserDeptPostDTO;

import java.util.List;

/**
 * <p>
 * 系统用户部门岗位表 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
public interface ISysUserDeptPostService extends IService<SysUserDeptPost> {

    R saveOrUpdate(UserDeptDTO userDeptDTO);

    R saveOrUpdate(UserDeptPostDTO userDeptPostDTO);

    void deleteByIds(UserDeptPostDTO userDeptPostDTO);

    List<SysUserDeptPostInfoDTO> listByUserId(Long userId);

    SysUserDeptPostInfoDTO getById(Long userId, Long deptId, Long postId);
}
