package com.bytefinger.common.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bytefinger.common.core.web.domain.vo.TreeEntity;
import com.bytefinger.common.core.web.domain.vo.TreeEntityVO;
import com.google.common.collect.Sets;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 树
 *
 * @author pat
 * @date 2022/10/21 14:12
 **/
public class TreeUtils {

    /**
     * list 转 树
     */
    public static List<TreeEntity> list2Tree(List<TreeEntity> treeList) {
        if (CollectionUtil.isEmpty(treeList)) {
            return Lists.newArrayList();
        }

        //操作所有菜单数据
        Map<Long, List<TreeEntity>> groupMap = treeList.stream().collect(Collectors.groupingBy(TreeEntity::getParentId));
        treeList.forEach(treeEntity -> {
            if (groupMap.containsKey(treeEntity.getId())) {
                treeEntity.setChildren(groupMap.get(treeEntity.getId()));
            }
        });
        return treeList.stream().filter(menu -> menu.getParentId().equals(0L)).collect(Collectors.toList());
    }


    public static List<TreeEntityVO> list3Tree(List<TreeEntityVO> treeList) {
        return list3Tree(treeList, 0);
    }

    public static List<TreeEntityVO> list3Tree(List<TreeEntityVO> treeList, long id) {
        if (CollectionUtil.isEmpty(treeList)) {
            return Lists.newArrayList();
        }
        //操作所有菜单数据
        Map<Long, List<TreeEntityVO>> groupMap = treeList.stream().collect(Collectors.groupingBy(TreeEntityVO::getParentId));
        treeList.forEach(treeEntity -> {
            if (groupMap.containsKey(treeEntity.getId())) {
                treeEntity.setChildren(groupMap.get(treeEntity.getId()));
            }
        });
        return treeList.stream().filter(menu -> menu.getParentId().equals(0L)).collect(Collectors.toList());
    }

    /**
     * list 转 tree
     * 指定id节点返回
     *
     * @param treeList
     * @param id
     * @return
     */
    public static TreeEntity list2Tree(List<TreeEntity> treeList, long id) {
        if (CollUtil.isEmpty(treeList)) {
            return null;
        }
        //TreeEntity currEntity = treeList.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
        //操作所有菜单数据
        Map<Long, List<TreeEntity>> groupMap = treeList.stream().collect(Collectors.groupingBy(TreeEntity::getParentId));
        treeList.forEach(treeEntity -> {
            if (groupMap.containsKey(treeEntity.getId())) {
                treeEntity.setChildren(groupMap.get(treeEntity.getId()));
            }
        });

        return treeList.stream().filter(menu -> menu.getId().equals(id)).collect(Collectors.toList()).stream().findFirst().orElse(null);
    }

    /**
     * list 转 tree
     * 指定id节点返回
     *
     * @param treeList
     * @param id
     * @return
     */
    public static TreeEntity list2TreeNoParentId(List<TreeEntity> treeList, long id) {
        if (CollUtil.isEmpty(treeList)) {
            return null;
        }

        //操作所有菜单数据
        Map<Long, List<TreeEntity>> groupMap = treeList.stream().collect(Collectors.groupingBy(TreeEntity::getParentId));
        treeList.forEach(treeEntity -> {
            if (groupMap.containsKey(treeEntity.getId())) {
                treeEntity.setChildren(groupMap.get(treeEntity.getId()));
            }
        });

        return treeList.stream().filter(menu -> menu.getParentId().equals(id)).collect(Collectors.toList()).stream().findFirst().orElse(null);
    }

    /**
     * 递归获取子节点
     *
     * @param childTrees
     * @param trees
     * @param parentId
     */
    public static void treeOrgChildren(List<TreeEntity> childTrees, List<TreeEntity> trees, long parentId) {
        for (TreeEntity tree : trees) {
            if (tree.getParentId() != null && tree.getParentId().equals(parentId)) {
                treeOrgChildren(childTrees, trees, tree.getId());
                childTrees.add(tree);
            }
        }
    }

    /**
     * 递归获取全部子节点id 包括自己
     *
     * @param treeId
     * @return
     */
    public static List<Long> getAllChildrenIdWithParentId(List<TreeEntity> trees, long treeId) {
        List<TreeEntity> childTrees = new ArrayList<>();
        treeOrgChildren(childTrees, trees, treeId);
        Set<Long> ids = Sets.newHashSet();
        for (TreeEntity treeDTO : childTrees) {
            ids.add(treeDTO.getId());
        }
        ids.add(treeId);
        return Lists.newArrayList(ids.iterator());
    }

    /**
     * 递归树
     *
     * @param treeEntity
     * @param parentMap
     */
    public static void setChildren(TreeEntity treeEntity, Map<Long, List<TreeEntity>> parentMap) {
        List<TreeEntity> childrenList = parentMap.get(treeEntity.getId());
        if (childrenList == null || childrenList.isEmpty()) {
            return;
        }

        treeEntity.setChildren(childrenList);
        for (TreeEntity child : childrenList) {
            setChildren(child, parentMap);
        }
    }

    /**
     * 递归获取下级全部节点的id
     *
     * @param treeEntity
     * @param pNodeIds
     * @param list
     */
    public static void getSubNodeIds(TreeEntity treeEntity, List<Long> pNodeIds, Set<Long> list) {
        pNodeIds.forEach(v -> {
            getSubNodeIds(treeEntity, v, list);
        });
    }

    public static void getSubNodeIds(TreeEntity treeEntity, Long pNodeId, Set<Long> list) {
        if (treeEntity.getId().equals(pNodeId)) {
            list.add(treeEntity.getId());
        }

        for (TreeEntity child : treeEntity.getChildren()) {
            getSubNodeIds(child, pNodeId, list);
            if (null != child && null != child.getParentId() && child.getParentId().equals(pNodeId)) {
                list.add(child.getId());
                if (!child.getChildren().isEmpty()) {
                    getSubNodeIds(child, child.getId(), list);
                }
            }
        }
    }

    /**
     * 递归获取上级节点数据
     *
     * @param treeEntity
     * @param pNodeId
     * @param list
     */
    public static TreeEntity findSubNodes(TreeEntity treeEntity, long pNodeId, List<TreeEntity> list) {
        if (null != treeEntity && null != treeEntity.getId() && treeEntity.getId().equals(pNodeId)) {
            return treeEntity;
        }

        for (TreeEntity child : treeEntity.getChildren()) {
            //获取到一个父级节点
            TreeEntity subNodes = findSubNodes(child, pNodeId, list);
            //第一个父级节点不为空，添加到父级节点集合，再根据父级节点拿到父父级节点
            if (null != subNodes) {
                list.add(subNodes);
                findSubNodes(child, subNodes.getParentId(), list);
                return treeEntity;
            }
        }

        return null;
    }

/*    public static List<TreeEntity> findAllNodes(TreeEntity treeEntity,  List<TreeEntity> list) {
       if (null != treeEntity){
           list.add(treeEntity);
       }
       if (CollectionUtil.isNotEmpty(treeEntity.getChildren())){
           for (TreeEntity child : treeEntity.getChildren()) {
               if (null != child){
                   findAllNodes(child,list);
               }else {
                   continue;
               }
           }

       }
       return list;
    }




    public static List<TreeEntity> findFatherNodes( List<TreeEntity> list,Long nodeId,List<TreeEntity> tree) {

        for (TreeEntity treeEntity : list) {
            if (nodeId .equals(treeEntity.getId())){
                tree.add(treeEntity);
                Long parentId = treeEntity.getParentId();
                if (null !=parentId){
                    findFatherNodes(list,parentId,tree);
                }else {
                    break;
                }

            }



        }
       return tree;
    }*/

    public static void findSubNodes(List<TreeEntity> list) {
        for (TreeEntity child : list) {
            System.out.println(child.getName());
        }

        System.out.println();
    }


    public static void main(String[] args) {
        TreeEntity treeEntity = new TreeEntity();
        treeEntity.setId(0L);
        treeEntity.setName("0");

        List<TreeEntity> te = Lists.newArrayList();
        for (long i = 10; i < 15; i++) {
            TreeEntity treeEntity1 = new TreeEntity();
            treeEntity1.setParentId(0L);
            treeEntity1.setId(i);
            treeEntity1.setName(treeEntity.getId() + "_" + treeEntity1.getId());
            te.add(treeEntity1);

            List<TreeEntity> te1 = Lists.newArrayList();
            for (long j = 20; j < 25; j++) {
                TreeEntity treeEntity2 = new TreeEntity();
                treeEntity2.setParentId(treeEntity1.getId());
                treeEntity2.setId(i * 10 + j);
                treeEntity2.setName(treeEntity1.getParentId() + "_" + treeEntity1.getId() + "_" + treeEntity2.getId());
                te1.add(treeEntity2);

                List<TreeEntity> te2 = Lists.newArrayList();
                for (long o = 30; o < 35; o++) {
                    TreeEntity treeEntity3 = new TreeEntity();
                    treeEntity3.setParentId(treeEntity2.getId());
                    treeEntity3.setId(i * 100 + j * 10 + o);
                    treeEntity3.setName(treeEntity1.getParentId() + "_" + treeEntity1.getId() + "_" + treeEntity2.getId() + "_" + treeEntity3.getId());
                    te2.add(treeEntity3);
                }
                treeEntity2.setChildren(te2);
            }
            treeEntity1.setChildren(te1);

        }
        treeEntity.setChildren(te);

        List<TreeEntity> childMenu = Lists.newArrayList();
        findSubNodes(treeEntity, 31L, childMenu);
        childMenu.add(treeEntity);
        findSubNodes(childMenu);


//        for (TreeEntity menu : childMenu) {
//            printfile(menu, 1);
//            System.out.println("");
//        }

//        List<TreeEntity> childMenu = Lists.newArrayList();
//        treeMenuList(treeEntity.getChildren(), 12L, childMenu);
//
//        for (TreeEntity menu : childMenu) {
//            printfile(menu, 1);
//            System.out.println("");
//        }

    }

    /**
     * list 转 tree
     * 指定id节点返回
     *
     * @param treeList
     * @param id
     * @return
     */
    public static TreeEntity list2NoDeptSelectTree(List<TreeEntity> treeList, long id) {
        if (CollUtil.isEmpty(treeList)) {
            return null;
        }
        //TreeEntity currEntity = treeList.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
        //操作所有菜单数据
        Map<Long, List<TreeEntity>> groupMap = treeList.stream().collect(Collectors.groupingBy(TreeEntity::getParentId));
        treeList.forEach(treeEntity -> {
            if (groupMap.containsKey(treeEntity.getId())) {
                TreeEntity newTreeNode = new TreeEntity();
                newTreeNode.setLevel(treeEntity.getLevel());
                newTreeNode.setName(treeEntity.getName());
                newTreeNode.setId(treeEntity.getId());
                newTreeNode.setCode(treeEntity.getCode());
                newTreeNode.setChildren(new ArrayList<>());
                newTreeNode.setParentId(treeEntity.getParentId());
                newTreeNode.setStatus(treeEntity.getStatus());
                newTreeNode.setAncestors(treeEntity.getAncestors());
                newTreeNode.setOrderNum(treeEntity.getOrderNum());
                newTreeNode.setShow(treeEntity.getShow());
                newTreeNode.setDeptType(treeEntity.getDeptType());
                List<TreeEntity> treeEntities = groupMap.get(treeEntity.getId());
                //去除无下级节点（部门节点）
//                for(int i=treeEntities.size()-1;i>=0;i--){
//                    if (!groupMap.containsKey(treeEntities.get(i).getId())) {
//                        treeEntities.remove(i);
//                    }
//                }
                treeEntities=treeEntities.stream().filter(t->"CENG_JI".equals(t.getDeptType())).collect(Collectors.toList());
                if(treeEntities.size() != 0){
                    //添加父级本身，并且把自身的节点id设置为负数
                    treeEntities.add(0,newTreeNode);
                    treeEntity.setId(-treeEntity.getId());
                }
                treeEntity.setChildren(treeEntities);
            }
        });

        return treeList.stream().filter(menu -> menu.getId().equals(0L-id) || menu.getId().equals(id)).collect(Collectors.toList()).stream().findFirst().orElse(null);
    }

}
