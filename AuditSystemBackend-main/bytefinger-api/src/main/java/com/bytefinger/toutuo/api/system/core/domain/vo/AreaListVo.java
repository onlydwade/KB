package com.bytefinger.toutuo.api.system.core.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class AreaListVo {

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "父级行政代码")
    private Long parentCode;

    @ApiModelProperty(value = "行政代码")
    private Long areaCode;

    @ApiModelProperty(value = "邮政编码")
    private Integer zipCode;

    @ApiModelProperty(value = "区号")
    private String cityCode;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "简称")
    private String shortName;

    @ApiModelProperty(value = "组合名")
    private String mergerName;

    @ApiModelProperty(value = "拼音")
    private String pinyin;

    @ApiModelProperty(value = "下级区域列表")
    private List<AreaListVo> subAreaList = Collections.emptyList();

}
