package com.bytefinger.toutuo.biz.rules.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.toutuo.biz.rules.domain.dto.RuleActionDTO;
import com.bytefinger.toutuo.biz.rules.domain.dto.RuleConditionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.compress.utils.Lists;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 规则管理
 * </p>
 *
 * @author patrick
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("biz_rules")
@ApiModel(value = "Rules对象", description = "规则管理")
public class Rules extends BaseEntity {

    @ApiModelProperty(value = "规则名称", hidden = false, required = false)
    @TableField("rule_name")
    @Size(max = 50, message = "规则名称不能超过50个字")
    private String ruleName;

    @ApiModelProperty(value = "规则对象类型（枚举）", hidden = false, required = false)
    @TableField("mode_name")
    @Dict(type = "GUI_ZE_CHU_LI_DUI_XIANG")
    private String modeName;

    @ApiModelProperty(value = "规则 JSON", hidden = false, required = false)
    @TableField("`condition`")
    private String condition;

    @ApiModelProperty(value = "动作 JSON", hidden = false, required = false)
    @TableField("action")
    private String action;

    @ApiModelProperty(value = "规则状态 0-关闭 1-打开", hidden = false, required = false)
    @TableField("rule_status")
    private Integer ruleStatus;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建人")
    @TableField(exist = false)
    @DataFillField(dataField = "createUserId", fillMethod = FillMethod.USER)
    private UserVO createUser;

    @ApiModelProperty(value = "修改时间", hidden = false, required = false)
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "修改人", hidden = false, required = false)
    @TableField(value = "update_user_id", fill = FieldFill.UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "修改人")
    @TableField(exist = false)
    @DataFillField(dataField = "updateUserId", fillMethod = FillMethod.USER)
    private UserVO updateUser;

    @ApiModelProperty(value = "删除数据", hidden = false, required = false)
    @TableField("deleted")
    @TableLogic(value = Deleted.N, delval = Deleted.Y)
    private Integer deleted;

    @ApiModelProperty(value = "规则类型 0-符合全部条件 1-符合任意条件", hidden = false, required = false)
    @TableField("condition_type")
    private Integer conditionType;

    @ApiModelProperty(value = "规则级别", hidden = false, required = false)
    @TableField("rule_level")
    @NotNull(message = "请输入优先级")
    @Min(1)
    private Integer ruleLevel;

    @ApiModelProperty(value = "规则，新增修改的时候必填", hidden = false, required = false)
    @TableField(exist = false)
    @NotNull(message = "请设置规则条件")
    private List<RuleConditionDTO> conditionList = Lists.newArrayList();

    @ApiModelProperty(value = "规则，新增修改的时候必填", hidden = false, required = false)
    @TableField(exist = false)
    @NotNull(message = "请设置执行动作")
    private List<RuleActionDTO> actionList = Lists.newArrayList();

}