package com.bytefinger.toutuo.biz.projectextension.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 拓后运营项目关联
 *
 * @author ycj
 * @email
 * @date 2023-03-09
 */
@Data
@ApiModel(value = "拓后运营项目关联")
public class ProjectCorrelationVo {

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
     * 项目id
     */
    @ApiModelProperty(value = "项目名称", name = "projectName")
    private String projectName;
    /**
     * 关联的项目
     */
    @ApiModelProperty(value = "关联的项目", name = "projectRelevantId")
    private Long projectRelevantId;
    /**
     * 删除
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
