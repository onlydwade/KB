package com.bytefinger.toutuo.biz.projectextension.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: ycj
 * @desc:
 * @date: 2023-03-10
 **/
@Data
@Schema(description = "在管项目总数查询")
@ApiModel(value = "在管项目总数查询", description = "在管项目总数查询")
public class ProjectQueryTotalVo {
    /**
     * 拓后单一项目
     */
    @ApiModelProperty(value = "拓后单一项目", hidden = false, required = false)
    @Schema(description = "拓后单一项目", name = "extensionProjectTotal")
    private Integer extensionProjectTotal;

    /**
     * 已承接查验
     */
    @ApiModelProperty(value = "已承接查验", hidden = false, required = false)
    @Schema(description = "已承接查验", name = "checkTotal")
    private Integer checkTotal;

    /**
     * 已评估
     */
    @ApiModelProperty(value = "已评估", hidden = false, required = false)
    @Schema(description = "已评估", name = "assessTotal")
    private Integer assessTotal;

    /**
     * 已退场
     */
    @ApiModelProperty(value = "已退场", hidden = false, required = false)
    @Schema(description = "已退场", name = "exitTotal")
    private Integer exitTotal;

    /**
     * 已续签
     */
    @ApiModelProperty(value = "已续签", hidden = false, required = false)
    @Schema(description = "已续签", name = "renewTotal")
    private Integer renewTotal;

    /**
     * 重新投标
     */
    @ApiModelProperty(value = "重新投标", hidden = false, required = false)
    @Schema(description = "重新投标", name = "newBidTotal")
    private Integer newBidTotal;

    /**
     * 续签或重新投标
     */
    @ApiModelProperty(value = "续签或重新投标", hidden = false, required = false)
    @Schema(description = "续签或重新投标", name = "renewOrNewBidTotal")
    private Integer renewOrNewBidTotal;
}
