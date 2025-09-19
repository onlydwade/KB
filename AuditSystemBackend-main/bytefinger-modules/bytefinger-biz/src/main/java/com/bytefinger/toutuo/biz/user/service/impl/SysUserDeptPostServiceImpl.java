package com.bytefinger.toutuo.biz.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.toutuo.api.biz.user.domain.SysDept;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.SysUserDeptPost;
import com.bytefinger.toutuo.api.biz.user.domain.dto.SysUserDeptPostInfoDTO;
import com.bytefinger.toutuo.biz.user.domain.dto.UserDeptDTO;
import com.bytefinger.toutuo.biz.user.domain.dto.UserDeptPostDTO;
import com.bytefinger.toutuo.biz.user.mapper.SysUserDeptPostInfoMapper;
import com.bytefinger.toutuo.biz.user.mapper.SysUserDeptPostMapper;
import com.bytefinger.toutuo.biz.user.service.ISysDeptService;
import com.bytefinger.toutuo.biz.user.service.ISysPostService;
import com.bytefinger.toutuo.biz.user.service.ISysUserDeptPostService;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统用户部门岗位表 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
@Service
@AllArgsConstructor
public class SysUserDeptPostServiceImpl extends ServiceImpl<SysUserDeptPostMapper, SysUserDeptPost> implements ISysUserDeptPostService {

    private final ISysPostService postService;

    private final ISysUserService userService;

    private final ISysDeptService deptService;

    private final SysUserDeptPostInfoMapper userDeptPostInfoMapper;

    /**
     * 添加或修改用户部门对应表
     *
     * @param userDeptDTO
     * @return
     */
    @Override
    public R saveOrUpdate(UserDeptDTO userDeptDTO) {
        if (null == userDeptDTO.getDeptId() || null == deptService.getOne(Wrappers.<SysDept>lambdaUpdate().eq(SysDept::getDeptId, userDeptDTO.getDeptId()).eq(SysDept::getDelFlag, Deleted.NO.getCode()))) {
            return R.fail("需要配置的部门不存在");
        }

        List<SysUserDeptPost> insertList = Lists.newArrayList();
        for (Long userId : userDeptDTO.getUserIds()) {
            if (null != userId) {
                SysUser user = userService.getById(userId);
                if (null != user && user.getDelFlag().equals(Deleted.NO.getCode().toString())) {
                    if (super.count(Wrappers.<SysUserDeptPost>lambdaQuery().eq(SysUserDeptPost::getUserId, userId).eq(SysUserDeptPost::getDeptId, userDeptDTO.getDeptId())) == 0) {
                        insertList.add(new SysUserDeptPost(userId, userDeptDTO.getDeptId(), 0L, new Date()));
                    }
                }
            }
        }

        if (!insertList.isEmpty()) {
            saveBatch(insertList);
        }

        return R.ok();
    }

    /**
     * 添加或修改用户-部门-岗位对应表
     *
     * @param userDeptPostDTO
     * @return
     */
    @Override
    public R saveOrUpdate(UserDeptPostDTO userDeptPostDTO) {

        if (null == userDeptPostDTO.getDeptId() || null == deptService.getOne(Wrappers.<SysDept>lambdaUpdate().eq(SysDept::getDeptId, userDeptPostDTO.getDeptId()).eq(SysDept::getDelFlag, Deleted.NO.getCode()))) {
            return R.fail("需要配置的部门不存在");
        }

        if (null == userDeptPostDTO.getPostId() || null == postService.selectPostById(userDeptPostDTO.getPostId())) {
            return R.fail("需要配置的岗位不存在");
        }

        List<SysUserDeptPost> insertList = Lists.newArrayList();
        List<SysUserDeptPost> updatelist = Lists.newArrayList();
        for (Long userId : userDeptPostDTO.getUserIds()) {
            if (null != userId) {
                SysUser user = userService.getById(userId);
                if (null != user && user.getDelFlag().equals(Deleted.NO.getCode().toString())) {
                    List<SysUserDeptPost> udpList = list(Wrappers.<SysUserDeptPost>lambdaQuery().eq(SysUserDeptPost::getUserId, userId));
                    boolean saveFlag = true;
                    for (SysUserDeptPost sysUserDeptPost : udpList) {
                        if (sysUserDeptPost.getDeptId().equals(userDeptPostDTO.getDeptId()) && sysUserDeptPost.getPostId().equals(userDeptPostDTO.getPostId())) {
                            saveFlag = false;
                        }

                        // 只配置了部门，未设置岗位
                        else if (sysUserDeptPost.getDeptId().equals(userDeptPostDTO.getDeptId()) && sysUserDeptPost.getPostId().equals(0L)) {
                            sysUserDeptPost.setPostId(userDeptPostDTO.getPostId());
                            updatelist.add(sysUserDeptPost);
                            saveFlag = false;
                        }
                    }

                    // 保存数据
                    if (saveFlag) {
                        insertList.add(new SysUserDeptPost(userId, userDeptPostDTO.getDeptId(), userDeptPostDTO.getPostId(), new Date()));
                    }
                }
            }
        }

        if (!insertList.isEmpty()) {
            saveBatch(insertList);
        }

        if (!updatelist.isEmpty()) {
            updateBatchById(updatelist);
        }

        return R.ok();
    }

    /**
     * 批量删除用户部门岗位
     *
     * @param userDeptPostDTO
     */
    @Override
    public void deleteByIds(UserDeptPostDTO userDeptPostDTO) {
        remove(Wrappers.<SysUserDeptPost>lambdaUpdate()
                .in(SysUserDeptPost::getUserId, userDeptPostDTO.getUserIds())
                .eq(SysUserDeptPost::getDeptId, userDeptPostDTO.getDeptId())
                .eq(SysUserDeptPost::getPostId, userDeptPostDTO.getPostId())
        );
    }

    /**
     * 通过用户id查询数据
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysUserDeptPostInfoDTO> listByUserId(Long userId) {
        return userDeptPostInfoMapper.selectList(userId);
    }

    /**
     * 获取详情
     *
     * @param userId
     * @param deptId
     * @param postId
     * @return
     */
    @Override
    public SysUserDeptPostInfoDTO getById(Long userId, Long deptId, Long postId) {
        return userDeptPostInfoMapper.selectOne(userId, deptId, postId);
    }

}
