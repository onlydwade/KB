package com.bytefinger.common.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.bytefinger.common.core.web.domain.vo.ProjectCompanyStepMenuVO;
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
public class ProjectCompanyStepMenuTreeUtils {



    public static List<ProjectCompanyStepMenuVO> listTree(List<ProjectCompanyStepMenuVO> treeList) {
        return listTreeByParentId(treeList, 0);
    }

    public static List<ProjectCompanyStepMenuVO> listTreeByParentId(List<ProjectCompanyStepMenuVO> treeList, long parentId) {
        return listTree(treeList, parentId, null);
    }

    public static List<ProjectCompanyStepMenuVO> listTreeByDocument(List<ProjectCompanyStepMenuVO> treeList, Integer isDocument) {
        return listTree(treeList, 0, isDocument);
    }

    public static List<ProjectCompanyStepMenuVO> listTree(List<ProjectCompanyStepMenuVO> treeList, long parentId, Integer isDocument) {
        if (CollectionUtil.isEmpty(treeList)) {
            return Lists.newArrayList();
        }
        //操作所有菜单数据
        Map<Long, List<ProjectCompanyStepMenuVO>> groupMap = treeList.stream().collect(Collectors.groupingBy(ProjectCompanyStepMenuVO::getParentId));

        treeList.forEach(treeEntity -> {
            if (groupMap.containsKey(treeEntity.getId())) {
                List<ProjectCompanyStepMenuVO> documents = groupMap.get(treeEntity.getId());
                if(null != isDocument){
                    documents = documents.stream().filter(v -> isDocument.equals(v.getIsDocument())).collect(Collectors.toList());
                }
                treeEntity.setChildren(documents);
            }
        });
        return treeList.stream().filter(menu -> menu.getParentId().equals(parentId)).collect(Collectors.toList());
    }

}