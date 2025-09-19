package com.bytefinger.toutuo.biz.performance.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("biz_indicator_display_level")
@ApiModel(value = "IndicatorDisplayLevel对象", description = "指标展示层级表")
public class IndicatorDisplayLevel extends BaseEntity {

    @ApiModelProperty(value = "展示层级ID", hidden = false, required = false)
    @TableField("display_level_id")
    private Long displayLevelId;

    @ApiModelProperty(value = "'指标的标识'", hidden = false, required = false)
    @TableField("field_key")
    private String fieldKey;

    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "'创建时间'", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新人", hidden = false, required = false)
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "是否删除", hidden = false, required = false)
    @TableField("deleted")
    private Integer deleted;

}
