package com.bytefinger.toutuo.biz.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.utils.TreeUtils;
import com.bytefinger.common.core.web.domain.vo.DeptVO;
import com.bytefinger.common.core.web.domain.vo.PostVO;
import com.bytefinger.common.core.web.domain.vo.TreeEntity;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.auth.AuthUtil;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.SysDept;
import com.bytefinger.toutuo.biz.user.mapper.SysDeptMapper;
import com.bytefinger.toutuo.biz.user.mapper.UserMapper;
import com.bytefinger.toutuo.biz.user.service.UserService;
import com.bytefinger.toutuo.common.domain.dto.UserRoleDTO;
import com.bytefinger.toutuo.common.enums.DataRole;
import com.google.common.collect.Lists;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地查询用户信息
 * 从数据库里查询
 *
 * @author pat
 * @date 2022/11/04 10:44
 **/
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final SysDeptMapper sysDeptMapper;

    @Override
    public List<UserVO> listUserByIds(List<Long> userIds) {
        return Collections.isEmpty(userIds) ? Lists.newArrayList() : userMapper.listUserByIds(userIds);
    }

    @Override
    public List<DeptVO> listDeptByIds(List<Long> deptIds) {
        return Collections.isEmpty(deptIds) ? Lists.newArrayList() : userMapper.listDeptByIds(deptIds);
    }

    @Override
    public List<PostVO> listPostByIds(List<Long> postIds) {
        return Collections.isEmpty(postIds) ? Lists.newArrayList() : userMapper.listPostByIds(postIds);
    }

    @Override
    public UserVO getUser(Long userId) {
        return userMapper.userById(userId);
    }

    @Override
    public List<UserVO> getUserByListDeptId(Long deptId) {
        List<Long> deptIds = this.deptTreeByDeptId(deptId);
        if (!deptIds.isEmpty()) {
            deptIds.add(deptId);
            return userMapper.listUsersByDeptIds(deptIds);
        }
        return Lists.newArrayList();
    }

    public List<DeptVO> getIntersectionParents(Long deptId1, Long deptId2) {
        List<DeptVO> list = userMapper.listDeptByIds(Arrays.asList(deptId1, deptId2));
        if (list.size() == 2 && StringUtils.isNotNull(list.get(0).getAncestors()) && StringUtils.isNotNull(list.get(1).getAncestors())) {
            String[] split1 = (list.get(0).getAncestors() + "," + list.get(0).getDeptId()).split(",");
            String[] split2 = (list.get(1).getAncestors() + "," + list.get(1).getDeptId()).split(",");

            List<String> intersection = Arrays.stream(split1).filter(item -> (!item.equals("0") && ArrayUtils.contains(split2, item))).collect(Collectors.toList());
            if (!intersection.isEmpty()) {
                return userMapper.listDeptByIds(intersection.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
            }
        }

        return Lists.newArrayList();
    }

    @Override
    public List<UserRoleDTO> findSuperiorsRolePermsUser(Long deptId, String... perms) {
        DeptVO dept = userMapper.getDept(deptId);
        if (null != dept && StringUtils.isNotEmpty(dept.getAncestors())) {
            List<Long> ids = Arrays.stream(dept.getAncestors().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            if (null != ids && !ids.isEmpty()) {
                ids.add(deptId);
                List<UserRoleDTO> userRoleDTOS = userMapper.listByPerms(ids, perms);
                Integer level = 0;
                List<UserRoleDTO> reList = Lists.newArrayList();
                for (UserRoleDTO userRoleDTO : userRoleDTOS) {
                    if (userRoleDTO.getLevel().intValue() > level) {
                        reList.clear();
                        level = userRoleDTO.getLevel().intValue();
                    }

                    if (userRoleDTO.getLevel().intValue() == level) {
                        reList.add(userRoleDTO);
                    }
                }

                return reList;
            }
        }

        return Lists.newArrayList();
    }

    /**
     * 获取指定部门下的所有部门id
     *
     * @return
     */
    @Override
    public List<Long> deptTreeByDeptId(Long deptId) {
        List<DeptVO> depts = userMapper.listAllDept();
        List<TreeEntity> treeEntities = depts.stream().map(item -> new TreeEntity()
                .setId(item.getDeptId())
                .setName(item.getDeptName())
                .setLevel(item.getLevel())
                .setParentId(item.getParentId())).collect(Collectors.toList());
        return TreeUtils.getAllChildrenIdWithParentId(treeEntities, deptId);
    }

    /**
     * 获取当前登录人角色的部门树-到指定层级
     *
     * @return
     */
    @Override
    public TreeEntity currDeptTree(Integer level) {
        List<DeptVO> depts = userMapper.listDeptByLevel(level);
        List<TreeEntity> treeEntities = depts.stream().map(item -> new TreeEntity()
                .setId(item.getDeptId())
                .setName(item.getDeptName())
                .setLevel(item.getLevel())
                .setParentId(item.getParentId())).collect(Collectors.toList());
        DeptVO deptVO = depts.stream().filter(d -> d.getLevel() == 1).findFirst().orElse(null);
        if (null == deptVO || null == deptVO.getDeptId()) {
            return null;
        }
        return TreeUtils.list2Tree(treeEntities, deptVO.getDeptId());
    }


    /**
     * 获取到指定层级部门
     *
     * @return
     */
    @Override
    public TreeEntity getCurrDeptTree(Integer level) {
        List<DeptVO> depts = userMapper.listDeptByLevel(level);
        List<TreeEntity> treeEntities = depts.stream().map(item -> new TreeEntity()
                .setId(item.getDeptId())
                .setName(item.getDeptName())
                .setLevel(item.getLevel())
                .setParentId(item.getParentId())).collect(Collectors.toList());
        return TreeUtils.list2Tree(treeEntities).get(0);
    }

    /**
     * 获取当前登录人角色的部门树
     *
     * @return
     */
    @Override
    public TreeEntity currDeptTree() {
        Long deptId = SecurityUtils.getLoginUser().getDeptId();
        if (null == deptId) {
            return null;
        }

        List<DeptVO> depts = userMapper.listAllDept();

        List<TreeEntity> treeEntities = depts.stream().map(item -> new TreeEntity()
                .setId(item.getDeptId())
                .setName(item.getDeptName())
                .setLevel(item.getLevel())
                .setParentId(item.getParentId())).collect(Collectors.toList());

        return TreeUtils.list2Tree(treeEntities, deptId);
    }

    @Override
    public TreeEntity currDeptTreeTwo() {
        Long deptId = SecurityUtils.getLoginUser().getDeptId();
        if (null == deptId) {
            return null;
        }

        List<SysDept> list = sysDeptMapper.selectList(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getDeptType, "CENG_JI").eq(SysDept::getDelFlag, 0));

        List<TreeEntity> treeEntities = list.stream().map(item -> new TreeEntity()
                .setId(item.getDeptId())
                .setName(item.getDeptName())
                .setLevel(item.getLevel())
                .setDeptType(item.getDeptType())
                .setParentId(item.getParentId())).collect(Collectors.toList());

        SysDept sysDept = sysDeptMapper.selectById(deptId);

        if (sysDept == null)
            return null;

        if (!"CENG_JI".equals(sysDept.getDeptType())) {
            boolean b = true;
            deptId = sysDept.getParentId();
            while (b) {
                sysDept = sysDeptMapper.selectById(deptId);
                if (!"CENG_JI".equals(sysDept.getDeptType())) {
                    deptId = sysDept.getParentId();
                } else {
                    deptId = sysDept.getDeptId();
                    b = false;
                }
            }
            //上找的层级，直接返回
            return tree1Entity(sysDept);
        }
        return TreeUtils.list2Tree(treeEntities, deptId);
    }

    public TreeEntity tree1Entity(SysDept sysDept) {
        TreeEntity tree = new TreeEntity();
        tree.setId(sysDept.getDeptId());
        tree.setName(sysDept.getDeptName());
        tree.setLevel(sysDept.getLevel());
        tree.setDeptType(sysDept.getDeptType());
        tree.setParentId(sysDept.getParentId());
        return tree;
    }

    public TreeEntity deptTree() {
        List<DeptVO> depts = userMapper.listAllDept();

        List<TreeEntity> treeEntities = depts.stream().map(item -> new TreeEntity()
                .setId(item.getDeptId())
                .setName(item.getDeptName())
                .setLevel(item.getLevel())
                .setParentId(item.getParentId())).collect(Collectors.toList());

        return TreeUtils.list2Tree(treeEntities, 100);
    }

    @Override
    public TreeEntity deptTreeTwo() {
        List<SysDept> list = sysDeptMapper.selectList(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getDeptType, "CENG_JI").eq(SysDept::getDelFlag, 0));

        List<TreeEntity> treeEntities = list.stream().map(item -> new TreeEntity()
                .setId(item.getDeptId())
                .setName(item.getDeptName())
                .setLevel(item.getLevel())
                .setDeptType(item.getDeptType())
                .setParentId(item.getParentId())).collect(Collectors.toList());
        return TreeUtils.list2Tree(treeEntities, 100);
    }


    @Override
    public List<Long> selectHeadOperateRoleUserId(String deptName) {
        return userMapper.selectHeadOperateRoleUserId(deptName);
    }

    @Override
    public DeptVO getDept(Long id) {
        return userMapper.getDept(id);
    }

    @Override
    public List<UserVO> getUserByDeptId(Long deptId) {
        return userMapper.listUsersByDeptId(deptId);
    }

    @Override
    public TreeEntity getSelectDeptTree() {
        Long deptId = SecurityUtils.getLoginUser().getDeptId();
        if (null == deptId) {
            return null;
        }
        List<DeptVO> depts = userMapper.listAllDept();
        List<TreeEntity> treeEntities = depts.stream().map(item -> new TreeEntity()
                .setId(item.getDeptId())
                .setName(item.getDeptName())
                .setLevel(item.getLevel())
                .setDeptType(item.getDeptType())
                .setParentId(item.getParentId())).collect(Collectors.toList());

        if (AuthUtil.hasPermi(DataRole.SHOW_ALL.getRoleKey())) {
            //获取上一级层次
            Long levelId = depts.stream().filter(f -> f.getParentId().equals(0L)).map(f -> f.getDeptId()).findFirst().orElse(null);
            if (null == levelId) {
                return null;
            }
            return TreeUtils.list2NoDeptSelectTree(treeEntities, levelId);
        } else if (AuthUtil.hasPermi(DataRole.SHOW_SUB.getRoleKey())) {
            //如果有子节点，显示当前层级
            Long levelId = depts.stream().filter(f -> f.getParentId().equals(deptId)).map(f -> f.getParentId()).findFirst().orElse(null);
            //如果没有子节点说明是部门显示上一层
            if (null == levelId) {
                levelId = depts.stream().filter(f -> f.getDeptId().equals(deptId)).map(f -> f.getParentId()).findFirst().orElse(null);
                return TreeUtils.list2NoDeptSelectTree(treeEntities, levelId);
            }
            return TreeUtils.list2NoDeptSelectTree(treeEntities, levelId);

        } else {
            //如果有子节点，显示当前层级
            Long levelId = depts.stream().filter(f -> f.getParentId().equals(deptId)).map(f -> f.getParentId()).findFirst().orElse(null);
            //如果没有子节点说明是部门显示上一层
            if (null == levelId) {
                levelId = depts.stream().filter(f -> f.getDeptId().equals(deptId)).map(f -> f.getParentId()).findFirst().orElse(null);
                TreeEntity tree = TreeUtils.list2NoDeptSelectTree(treeEntities, levelId);
                List<TreeEntity> children = tree.getChildren();
                children = children.stream().filter(f -> f.getId().equals((0L - tree.getId()))).collect(Collectors.toList());
                tree.setChildren(children);
                return tree;
            }
            TreeEntity tree = TreeUtils.list2NoDeptSelectTree(treeEntities, levelId);
            List<TreeEntity> children = tree.getChildren();
            children = children.stream().filter(f -> f.getId().equals((0L - tree.getId()))).collect(Collectors.toList());
            tree.setChildren(children);
            return tree;
        }
    }

    @Override
    public List<UserVO> getUserPrincipalByDeptId(DeptVO deptVO) {
        return userMapper.getUserPrincipalByDeptId(deptVO);
    }

    @Override
    public List<UserVO> getUserPrincipalId(UserVO userVO) {
        return userMapper.getUserPrincipalId(userVO);
    }
}

