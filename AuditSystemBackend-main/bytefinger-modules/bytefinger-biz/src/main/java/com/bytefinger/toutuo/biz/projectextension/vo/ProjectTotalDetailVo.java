package com.bytefinger.toutuo.biz.projectextension.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: chengwei
 * @desc:
 * @date: 2023-11-22
 **/
@Data
@Schema(description = "在管项目统计")
@ApiModel(value = "在管项目统计", description = "在管项目统计")
public class ProjectTotalDetailVo {
    /**
     * 在管项目数
     */
    @ApiModelProperty(value = "在管项目数", hidden = false, required = false)
    @Schema(description = "在管项目数", name = "projectTotal")
    private Integer id;

    /**
     * 续签数
     */
    @ApiModelProperty(value = "续签数", hidden = false, required = false)
    @Schema(description = "续签数", name = "renewTotal")
    private String serviceStatus;

    /**
     * 重新投标
     */
    @ApiModelProperty(value = "重新投标数", hidden = false, required = false)
    @Schema(description = "重新投标数", name = "newBidTotal")
    private Integer processState;

    /**
     * 退场数
     */
    @ApiModelProperty(value = "退场数", hidden = false, required = false)
    @Schema(description = "退场数", name = "exitTotal")
    private Integer processMode;


    /**
     * 重新投标
     */
    @ApiModelProperty(value = "重新投标数", hidden = false, required = false)
    @Schema(description = "重新投标数", name = "newBidTotal")
    private Integer processStatet;

    /**
     * 退场数
     */
    @ApiModelProperty(value = "退场数", hidden = false, required = false)
    @Schema(description = "退场数", name = "exitTotal")
    private Integer processModet;
}
