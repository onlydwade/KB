package com.bytefinger.toutuo.common.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.common.security.enums.RoleKeys;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import java.util.Date;
import java.util.List;

/**
 * 业务基础实体
 *
 * @author pat
 * @date 2022/10/23 11:24
 **/
@Data
@ApiModel(value = "基础信息", description = "基础信息")
public class BizCommonBaseEntity extends TimeBaseEntity {

    /****** 通用业务字段 **/
    @ApiModelProperty(value = "参与人ids", hidden = false, required = false)
    @TableField(exist = false)
    private List<Long> userIds;

    @ApiModelProperty(value = "参与人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "id", fillMethod = FillMethod.USERS, joinWrite = "userIds")
    private List<UserVO> users = Lists.newArrayList();
    
    @ApiModelProperty(value = "归属人id", hidden = false, required = false)
    @HistoryFieldLog(value = "归属人", joinField = "attributorUser")
    @TableField(value = "attributor_user_id")
    private Long attributorUserId;

    @ApiModelProperty(value = "归属人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "attributorUserId", fillMethod = FillMethod.USER)
    private UserVO attributorUser;

    @Excel(name = "最后跟进时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后跟进时间", hidden = false, required = false)
    @TableField("follow_time")
    private Date followTime;

    @ApiModelProperty(value = "负责主体", hidden = false, required = false)
    @TableField("dept_id")
    private Long deptId;

    @Excel(name = "负责主体")
    @ApiModelProperty(value = "负责主体", hidden = false, required = false)
    @TableField(exist = false)
    @HistoryFieldLog("负责主体")
    @DataFillField(dataField = "deptId", fillMethod = FillMethod.DEPT)
    private String deptName;

    @ApiModelProperty(value = "数据权限", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "id", fillMethod = FillMethod.ROLEKEYS, roleKeysField = {RoleKeys.APPROVAL, RoleKeys.USER_IDS, RoleKeys.ATTRIBUTOR_USER_ID, RoleKeys.DEPT_ID})
    private List<Integer> roleKeys = Lists.newArrayList();

}
