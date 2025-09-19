package com.bytefinger.toutuo.biz.followlog.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.biz.customer.domain.CustomerContacts;
import com.bytefinger.toutuo.biz.customer.domain.CustomerFollowLogDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author pat
 * @date 2022/10/29 02:16
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "跟踪记录", description = "跟踪记录")
public class BizFollowLog extends BaseEntity {

    @ApiModelProperty(value = "模块名称= {Lead, Opportunity, Project, Customer}", hidden = false, required = false)
    @NotEmpty(message = "数据不能为空")
    private String modelName;

    @ApiModelProperty(value = "数据id", hidden = false, required = false)
    @NotNull(message = "数据不能为空")
    private Long recordId;

    @ApiModelProperty(value = "跟进人id", hidden = false, required = false)
    private Long followUserId;

    @ApiModelProperty(value = "跟进人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "followUserId", fillMethod = FillMethod.USER)
    private UserVO followUser;

    @ApiModelProperty(value = "跟进时间", hidden = false, required = false)
    private Date followTime;

    @ApiModelProperty(value = "主要维护人", hidden = false, required = false)
    private String principal;

    @ApiModelProperty(value = "主要维护人岗位", hidden = false, required = false)
    private String principalPost;

    @ApiModelProperty(value = "参与人", hidden = false, required = false)
    private String partUsers;

    @ApiModelProperty(value = "跟踪类型", hidden = false, required = false)
    @Dict(type = "GEN_ZONG_DONG_TAI")
    private String followType;

    @ApiModelProperty(value = "地点", hidden = false, required = false)
    private String address;

    @ApiModelProperty(value = "对方参与人数", hidden = false, required = false)
    @Max(value = 10000000)
    private Integer partUsersCount;

    @ApiModelProperty(value = "跟踪内容", hidden = false, required = false)
    private String followContent;

    @ApiModelProperty(value = "跟踪附件 附件类型 + 附件路径", hidden = false, required = false)
    private String followDocument;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    private Date createTime;

    @ApiModelProperty(value = "修改时间", hidden = false, required = false)
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "创建人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "createUserId", fillMethod = FillMethod.USER)
    private UserVO createUser;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "最后一次跟进时间")
    private Date lastFollowTime;

    @ApiModelProperty(value = "拜访人")
    private String visitUserName;

    @ApiModelProperty(value = "拜访日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date visitTime;

    @ApiModelProperty(value = "拜访方式")
    @Dict(type = "BAI_FANG_FANG_SHI")
    private String visitType;


    @ApiModelProperty(value = "跟进动态明细", hidden = false, required = false)
    @TableField(exist = false)
    private List<CustomerFollowLogDetail> customerFollowLogDetailList;
}
