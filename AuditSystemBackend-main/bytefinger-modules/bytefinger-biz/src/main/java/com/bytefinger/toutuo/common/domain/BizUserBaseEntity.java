package com.bytefinger.toutuo.common.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 业务基础实体
 *
 * @author pat
 * @date 2022/10/23 11:24
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "基础信息", description = "基础信息")
public class BizUserBaseEntity extends BaseEntity {

    @ApiModelProperty(value = "添加时间", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "id", hidden = false, required = false)
    @TableField("record_id")
    private Long recordId;

    @ApiModelProperty(value = "用户id", hidden = false, required = false)
    @TableField("user_id")
    private Long userId;

}
