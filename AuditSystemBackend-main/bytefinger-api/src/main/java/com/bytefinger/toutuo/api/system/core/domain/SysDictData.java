package com.bytefinger.toutuo.api.system.core.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author patrick
 * @since 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dict_data")
@ApiModel(value = "SysDictData对象", description = "字典数据表")
public class SysDictData {

    @ApiModelProperty(value = "字典编码", hidden = false, required = false)
    @JSONField(name = "dictCode", serialize = true)
    @TableId(value = "dict_code", type = IdType.AUTO)
    private Long dictCode;

    @ApiModelProperty(value = "字典排序", hidden = false, required = false)
    @JSONField(name = "dictSort", serialize = true)
    @TableField("dict_sort")
    private Integer dictSort;

    @ApiModelProperty(value = "字典标签", hidden = false, required = false)
    @JSONField(name = "dictLabel", serialize = true)
    @TableField("dict_label")
    private String dictLabel;

    @ApiModelProperty(value = "字典键值", hidden = false, required = false)
    @JSONField(name = "dictValue", serialize = true)
    @TableField("dict_value")
    private String dictValue;

    @ApiModelProperty(value = "字典类型", hidden = false, required = false)
    @JSONField(name = "dictType", serialize = true)
    @TableField("dict_type")
    private String dictType;

    @ApiModelProperty(value = "样式属性（其他样式扩展）", hidden = false, required = false)
    @JSONField(name = "cssClass", serialize = true)
    @TableField("css_class")
    private String cssClass;

    @ApiModelProperty(value = "表格回显样式", hidden = false, required = false)
    @JSONField(name = "listClass", serialize = true)
    @TableField("list_class")
    private String listClass;

    @ApiModelProperty(value = "是否默认（Y是 N否）", hidden = false, required = false)
    @JSONField(name = "isDefault", serialize = true)
    @TableField("is_default")
    private String isDefault;

    @ApiModelProperty(value = "状态（0正常 1停用）", hidden = false, required = false)
    @JSONField(name = "status", serialize = true)
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "创建者", hidden = false, required = false)
    @JSONField(name = "createBy", serialize = true)
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @JSONField(name = "createTime", serialize = true)
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新者", hidden = false, required = false)
    @JSONField(name = "updateBy", serialize = true)
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    @JSONField(name = "updateTime", serialize = true)
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @JSONField(name = "remark", serialize = true)
    @TableField("remark")
    private String remark;

}