package com.bytefinger.toutuo.biz.projectextension.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 承接查验
 *
 * @author ycj
 * @email
 * @date 2023-03-15 14:44:28
 */
@Data
@ApiModel(value = "承接查验")
public class ProjectCheckVo {

    /**
     *
     */
    @ApiModelProperty(value = "", name = "id")
    private Long id;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", name = "projectId")
    private Long projectId;
    /**
     * 查验状态(0未完成,1进行中,2已完成)
     */
    @ApiModelProperty(value = "查验状态(0未完成,1进行中,2已完成)", name = "checkState")
    private Integer checkState;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", name = "startTime")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", name = "endTime")
    private Date endTime;
    /**
     * 责任id
     */
    @ApiModelProperty(value = "责任id", name = "personChargeId")
    private Integer personChargeId;
    /**
     * 责任名称
     */
    @ApiModelProperty(value = "责任名称", name = "personChargeName")
    private String personChargeName;
    /**
     * 发现问题数
     */
    @ApiModelProperty(value = "发现问题数", name = "problemNum")
    private Integer problemNum;
    /**
     * 已整改数量
     */
    @ApiModelProperty(value = "已整改数量", name = "rectifyFinishedNum")
    private Integer rectifyFinishedNum;
    /**
     * 待整改数量
     */
    @ApiModelProperty(value = "待整改数量", name = "rectifyAwaitNum")
    private Integer rectifyAwaitNum;
    /**
     * 整改期限
     */
    @ApiModelProperty(value = "整改期限", name = "rectifyDeadline")
    private Date rectifyDeadline;
//    /**
//     * 创建人
//     */
//    @ApiModelProperty(value = "创建人", name = "createUserId")
//    private Long createUserId;
//    /**
//     * 创建时间
//     */
//    @ApiModelProperty(value = "创建时间", name = "createTime")
//    private Date createTime;
//    /**
//     * 修改人
//     */
//    @ApiModelProperty(value = "修改人", name = "updateUserId")
//    private Long updateUserId;
//    /**
//     * 修改时间
//     */
//    @ApiModelProperty(value = "修改时间", name = "updateTime")
//    private Date updateTime;

    /**
     * 当前登入人是否是项目归属人、拓后负责人、项目团队成员
     */
    @ApiModelProperty(value = "当前登入人是否是项目归属人、拓后负责人、项目团队成员", hidden = false, required = false)
    @Schema(description = "当前登入人是否是项目归属人、拓后负责人、项目团队成员", name = "checkState")
    private Boolean show;

}
