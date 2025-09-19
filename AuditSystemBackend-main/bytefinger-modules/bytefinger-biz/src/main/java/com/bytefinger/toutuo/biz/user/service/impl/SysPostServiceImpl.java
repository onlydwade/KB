package com.bytefinger.toutuo.biz.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.constant.UserConstants;
import com.bytefinger.common.core.exception.ServiceException;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.toutuo.biz.user.domain.SysPost;
import com.bytefinger.toutuo.biz.user.domain.SysPostMenu;
import com.bytefinger.toutuo.biz.user.mapper.SysPostMapper;
import com.bytefinger.toutuo.biz.user.mapper.SysPostMenuMapper;
import com.bytefinger.toutuo.biz.user.mapper.SysUserDeptPostMapper;
import com.bytefinger.toutuo.biz.user.service.ISysPostService;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位信息 服务层处理
 *
 * @author pat
 */
@Service
@AllArgsConstructor
public class SysPostServiceImpl implements ISysPostService {

    private final SysPostMapper postMapper;

    private final SysUserDeptPostMapper userDeptPostMapper;

    private final SysPostMenuMapper postMenuMapper;

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPost(SysPost post) {
        postMapper.insertPost(post);
        return this.insertPostMenu(post);
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePost(SysPost post) {
        this.updatePostMenu(post);
        return postMapper.updatePost(post);
    }

    @Override
    public List<SysPost> selectPostListByType(List<String> roleKey) {
        List<SysPost> list = postMapper.selectPostListByType(roleKey);
        return list;
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }


        }

        // 删除菜单
        postMenuMapper.deletePostMenu(postIds);

        return postMapper.deletePostByIds(postIds);
    }


    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return postMapper.selectPostList(post);
    }

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectPostAll() {
        return postMapper.selectPostAll();
    }

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(Long postId) {
        SysPost sysPost = postMapper.selectPostById(postId);
        if (null != sysPost) {
            List<SysPostMenu> sysPostMenus = postMenuMapper.selectList(Wrappers.<SysPostMenu>lambdaQuery().eq(SysPostMenu::getPostId, postId));
            List<Long> collect = sysPostMenus.stream().map(SysPostMenu::getMenuId).collect(Collectors.toList());
            sysPost.setMenuIds(collect.toArray(new Long[collect.size()]));
        }
        return sysPost;
    }

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostNameUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPost info = postMapper.checkPostNameUnique(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostCodeUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPost info = postMapper.checkPostCodeUnique(post.getPostCode());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(Long postId) {
        return postMapper.selectCount(postId);

        // return userDeptPostMapper.selectCount(Wrappers.<SysUserDeptPost>lambdaQuery().eq(SysUserDeptPost::getPostId, postId));
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int deletePostById(Long postId) {
        return postMapper.deletePostById(postId);
    }


    /**
     * 新增岗位菜单信息
     *
     * @param post 岗位对象
     */
    private int insertPostMenu(SysPost post) {
        int rows = 1;

        boolean flag = true;

        List<SysPostMenu> list = Lists.newArrayList();

//
//        if (null != post.getMenuIds() || post.getMenuIds().length >= 0) {
//            List<Long> longs = Arrays.asList(post.getMenuIds());
//            Iterator<Long> iterator = longs.iterator();
//            while (iterator.hasNext()) {
//                Long next = iterator.next();
//                if (next.equals(2040l) || next.equals(2088l) || next.equals(2090l)) {
//                    iterator.remove();
//                }
//
//            }
//        }

        for (Long menuId : post.getMenuIds()) {
            if (menuId == null) {
                continue;
            }
            SysPostMenu rm = new SysPostMenu();
            rm.setPostId(post.getPostId());
            rm.setMenuId(menuId);
            if (menuId.equals(2040l) || menuId.equals(2088l) || menuId.equals(2090l)) {
                continue;
            }
            list.add(rm);
        }

        if (post.getDataRoleId() != null) {
            SysPostMenu rm = new SysPostMenu();
            rm.setPostId(post.getPostId());
            rm.setMenuId(post.getDataRoleId());
            list.add(rm);
        }

        if (list.size() > 0) {
            rows = postMenuMapper.batchPostMenu(list);
        }
        return rows;
    }

    /**
     * 修改岗位菜单信息
     *
     * @param post 岗位对象
     */
    private int updatePostMenu(SysPost post) {
        postMenuMapper.deletePostMenuByPostId(post.getPostId());
        return insertPostMenu(post);
    }

}
