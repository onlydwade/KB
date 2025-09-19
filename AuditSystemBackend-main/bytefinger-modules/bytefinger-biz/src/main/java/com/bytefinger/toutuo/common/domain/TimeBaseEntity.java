package com.bytefinger.toutuo.common.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.common.security.annotation.DataFillField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 基础信息
 *
 * @author pat
 * @date 2022/10/23 11:24
 **/
@Data
@ApiModel(value = "基础信息", description = "基础信息")
public class TimeBaseEntity extends BaseEntity {

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "createUserId", fillMethod = FillMethod.USER)
    private UserVO createUser;

    @ApiModelProperty(value = "最后修改时间", hidden = false, required = false)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "最后修改人id", hidden = false, required = false)
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "修改人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "updateUserId", fillMethod = FillMethod.USER)
    private UserVO updateUser;

}
