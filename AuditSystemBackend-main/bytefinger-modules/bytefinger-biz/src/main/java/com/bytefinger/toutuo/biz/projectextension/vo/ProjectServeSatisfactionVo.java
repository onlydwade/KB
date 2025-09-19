package com.bytefinger.toutuo.biz.projectextension.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.Data;

/**
 * 拓后运营-服务满意度信息
 *
 * @author ycj
 * @email
 * @date 2023-03-20 11:40:08
 */
@Data
@ApiModel(value = "拓后运营-服务满意度信息")
public class ProjectServeSatisfactionVo {

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
     * 满意度类型(1.合同相对方满意度,2.业主服务满意度)
     */
    @ApiModelProperty(value = "满意度类型(1.合同相对方满意度,2.业主服务满意度)", name = "satisfactionType")
    private Integer satisfactionType;
    /**
     * 满意度说明
     */
    @ApiModelProperty(value = "满意度说明", name = "satisfactionExplain")
    private String satisfactionExplain;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份", name = "year")
    private String year;
    /**
     * 是否删除 0-否 2-是
     */
    @ApiModelProperty(value = "是否删除 0-否 2-是", name = "deleted")
    private Integer deleted;
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
