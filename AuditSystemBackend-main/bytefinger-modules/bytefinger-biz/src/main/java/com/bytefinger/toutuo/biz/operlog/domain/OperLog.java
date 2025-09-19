package com.bytefinger.toutuo.biz.operlog.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.common.security.annotation.DataFillField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 业务数据变更记录表
 * </p>
 *
 * @author patrick
 * @since 2022-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("biz_oper_log")
@ApiModel(value = "OperLog对象", description = "业务数据变更记录表")
public class OperLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private Date updateTime;

    @ApiModelProperty(value = "更新人id", hidden = false, required = false)
    @TableField(value = "update_user_id", fill = FieldFill.INSERT)
    private Long updateUserId;

    @ApiModelProperty(value = "更新人", hidden = false, required = false)
    @TableField(value = "update_user_name", fill = FieldFill.INSERT)
    private String updateUserName;

    @ApiModelProperty(value = "表名表示", hidden = false, required = false)
    @TableField("module_name")
    private String moduleName;

    @ApiModelProperty(value = "更新数据的id", hidden = false, required = false)
    @TableField("record_id")
    private Long recordId;

    @ApiModelProperty(value = "更新日志", hidden = false, required = false)
    @TableField("update_log")
    private String updateLog;

    @ApiModelProperty(value = "操作ip", hidden = false, required = false)
    @TableField(value = "oper_ip", fill = FieldFill.INSERT)
    private String operIp;

    @TableField(exist = false)
    private UserVO updateUser;

    @ApiModelProperty(value = "更新人部门", hidden = false, required = false)
    @TableField("update_dept_id")
    private Long updateDeptId;

    @ApiModelProperty(value = "更新人部门", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(fillMethod = FillMethod.DEPT, dataField = "updateDeptId")
    private String updateDeptName;

    @ApiModelProperty(value = "更新人岗位", hidden = false, required = false)
    @TableField("update_post_id")
    private Long updatePostId;

    @ApiModelProperty(value = "更新人岗位", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(fillMethod = FillMethod.POST, dataField = "updatePostId")
    private String updatePostName;

}