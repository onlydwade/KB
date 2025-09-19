package com.bytefinger.common.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.bytefinger.common.core.web.domain.vo.AuditStepMenuVO;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 树
 *
 * @author pat
 * @date 2022/10/21 14:12
 **/
public class AuditStepMenuTreeUtils {




    public static List<AuditStepMenuVO> listTree(List<AuditStepMenuVO> treeList) {
        return listTreeByParentId(treeList, 0);
    }

    public static List<AuditStepMenuVO> listTreeByParentId(List<AuditStepMenuVO> treeList, long parentId) {
        return listTree(treeList, parentId, null);
    }

    public static List<AuditStepMenuVO> listTreeByDocument(List<AuditStepMenuVO> treeList, Integer isDocument) {
        return listTree(treeList, 0, isDocument);
    }

    public static List<AuditStepMenuVO> listTree(List<AuditStepMenuVO> treeList, long parentId, Integer isDocument) {
        if (CollectionUtil.isEmpty(treeList)) {
            return Lists.newArrayList();
        }
        //操作所有菜单数据
        Map<Long, List<AuditStepMenuVO>> groupMap = treeList.stream().collect(Collectors.groupingBy(AuditStepMenuVO::getParentId));

        treeList.forEach(treeEntity -> {
            if (groupMap.containsKey(treeEntity.getId())) {
                List<AuditStepMenuVO> documents = groupMap.get(treeEntity.getId());
                if(null != isDocument){
                    documents = documents.stream().filter(v -> isDocument.equals(v.getIsDocument())).collect(Collectors.toList());
                }
                treeEntity.setChildren(documents);
            }

        });
        return treeList.stream().filter(menu -> menu.getParentId().equals(parentId)).collect(Collectors.toList());
    }

}