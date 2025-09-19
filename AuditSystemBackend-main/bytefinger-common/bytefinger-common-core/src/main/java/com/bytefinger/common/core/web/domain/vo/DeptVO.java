package com.bytefinger.common.core.web.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 部门VO
 *
 * @author pat
 * @date 2022/11/04 10:43
 **/
@Data
public class DeptVO{

    private Long deptId;

    private String deptName;

    @ApiModelProperty(hidden = true)
    private Integer level;

    @ApiModelProperty(hidden = true)
    private Long parentId;

    @ApiModelProperty(hidden = true)
    private String ancestors;

    @ApiModelProperty(hidden = true)
    private String deptType;


    private List<UserVO> userVOList;
}
