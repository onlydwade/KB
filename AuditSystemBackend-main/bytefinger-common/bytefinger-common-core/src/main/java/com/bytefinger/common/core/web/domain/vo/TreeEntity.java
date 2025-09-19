package com.bytefinger.common.core.web.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * Tree基类
 *
 * @author patrick
 */
@Data
@Accessors(chain = true)
public class TreeEntity {
    private Long parentId;
    private Long id;
    private String name;
    private String code;
    private Integer orderNum;
    private Integer status = 0;
    private Integer show;
    private String ancestors;
    private Integer level;
    private List<TreeEntity> children = Lists.newArrayList();
    private String groupName;
    private String deptType;
}
