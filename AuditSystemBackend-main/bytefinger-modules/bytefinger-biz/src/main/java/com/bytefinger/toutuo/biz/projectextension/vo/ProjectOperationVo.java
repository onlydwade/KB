package com.bytefinger.toutuo.biz.projectextension.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 拓后运营管理
 *
 * @author ycj
 * @email
 * @date 2023-03-14 11:05:23
 */
@Data
@ApiModel(value = "拓后运营管理")
public class ProjectOperationVo {

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
     * 总户数
     */
    @ApiModelProperty(value = "总户数", name = "househoidSum")
    private Integer househoidSum;
    /**
     * 占地面积
     */
    @ApiModelProperty(value = "占地面积", name = "floorSpace")
    private String floorSpace;

    /**
     * 建筑面积
     */
    @ApiModelProperty(value = "建筑面积", hidden = false, required = false)
    @Excel(name = "建筑面积", sort = 20)
    private BigDecimal constructionArea;
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
