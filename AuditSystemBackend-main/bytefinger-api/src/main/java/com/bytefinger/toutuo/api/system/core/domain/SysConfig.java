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
 * 参数配置表
 * </p>
 *
 * @author patrick
 * @since 2022-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_config")
@ApiModel(value = "SysConfig对象", description = "参数配置表")
public class SysConfig {

    @ApiModelProperty(value = "参数主键", hidden = false, required = false)
    @JSONField(name = "configId", serialize = true)
    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;

    @ApiModelProperty(value = "参数名称", hidden = false, required = false)
    @JSONField(name = "configName", serialize = true)
    @TableField("config_name")
    private String configName;

    @ApiModelProperty(value = "参数键名", hidden = false, required = false)
    @JSONField(name = "configKey", serialize = true)
    @TableField("config_key")
    private String configKey;

    @ApiModelProperty(value = "参数键值", hidden = false, required = false)
    @JSONField(name = "configValue", serialize = true)
    @TableField("config_value")
    private String configValue;

    @ApiModelProperty(value = "系统内置（Y是 N否）", hidden = false, required = false)
    @JSONField(name = "configType", serialize = true)
    @TableField("config_type")
    private String configType;

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