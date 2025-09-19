package com.bytefinger.toutuo.biz.projectextension.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.Data;

/**
 * 拓后管理续签状态
 *
 * @author ycj
 * @email
 * @date 2023-03-09 11:04:59
 */
@Data
@ApiModel(value = "拓后管理续签状态")
public class ProjectExtensionVo {

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
     * 处理状态(0未处理,1审批中,2已处理)
     */
    @ApiModelProperty(value = "处理状态(0未处理,1审批中,2已处理)", name = "processState")
    private Integer processState;
    /**
     * 处理方式(0未续签,1续签,2重新投标,3退场)
     */
    @ApiModelProperty(value = "处理方式(0未续签,1续签,2重新投标,3退场)", name = "processMode")
    private Integer processMode;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", name = "createUserId")
    private Long createUserId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人", name = "updateUserId")
    private Long updateUserId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", name = "updateTime")
    private Date updateTime;

}
