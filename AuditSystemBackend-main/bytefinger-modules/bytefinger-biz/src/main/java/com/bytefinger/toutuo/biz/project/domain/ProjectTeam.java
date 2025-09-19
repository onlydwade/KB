package com.bytefinger.toutuo.biz.project.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.web.domain.vo.DeptVO;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * <p>
 * 项目团队信息
 * </p>
 *
 * @author patrick
 * @since 2022-10-20
 */
@Data
@TableName("biz_project_team")
@ApiModel(value = "项目团队信息", description = "项目团队信息")
public class ProjectTeam extends BaseEntity {

    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @TableField("record_id")
    @NotNull(message = "项目id不能为空")
    private Long recordId;

    @ApiModelProperty(value = "用户id", hidden = false, required = false)
    @TableField("user_id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty(value = "成员信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "userId", fillMethod = FillMethod.USER)
    private UserVO user;

    @ApiModelProperty(value = "部门id", hidden = false, required = false)
    @TableField("dept_id")
    @HistoryFieldLog("部门id")
    private Long deptId;

    @ApiModelProperty(value = "部门信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "deptId", fillMethod = FillMethod.DEPT)
    private String deptName;

    @ApiModelProperty(value = "角色类型", hidden = false, required = false)
    @TableField("role_type")
    @Dict(type = "XIANG_MU_JUE_SE_LEI_XING")
    private String roleType;

    @ApiModelProperty(value = "角色名称", hidden = false, required = false)
    @TableField("role_key")
    @Dict(ptype = "XIANG_MU_JUE_SE_LEI_XING")
    private String roleKey;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


}