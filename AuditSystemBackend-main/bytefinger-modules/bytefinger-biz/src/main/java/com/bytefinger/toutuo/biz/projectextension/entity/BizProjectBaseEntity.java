package com.bytefinger.toutuo.biz.projectextension.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 业务基础实体
 *
 * @author pat
 * @date 2022/10/23 11:24
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "基础信息", description = "基础信息")
public class BizProjectBaseEntity extends BaseEntity {

    /**
     * 公司id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @TableField("project_id")
    @HistoryFieldLog("项目id")
    @Excel(name = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "文件模板", hidden = false, required = false)
    @TableField(exist = false)
    @HistoryFieldLog("文件模板")
    List<ProjectExpansionDocumentTemplate> documentTemplateList;

}
