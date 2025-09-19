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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author patrick
 * @since 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dict_type")
@ApiModel(value = "SysDictType对象", description = "字典类型表")
public class SysDictType {

    @ApiModelProperty(value = "字典主键", hidden = false, required = false)
    @JSONField(name = "dictId", serialize = true)
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    @ApiModelProperty(value = "字典名称", hidden = false, required = false)
    @JSONField(name = "dictName", serialize = true)
    @TableField("dict_name")
    @NotBlank(message = "字典名称不能为空")
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
    private String dictName;

    @ApiModelProperty(value = "字典类型", hidden = false, required = false)
    @JSONField(name = "dictType", serialize = true)
    @TableField("dict_type")
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    private String dictType;

    @ApiModelProperty(value = "父字典类型", hidden = false, required = false)
    @JSONField(name = "dictPtype", serialize = true)
    @TableField("dict_ptype")
    private String dictPtype;

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