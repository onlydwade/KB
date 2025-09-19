package com.bytefinger.toutuo.biz.rules.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 规则发送消息记录
 * </p>
 *
 * @author patrick
 * @since 2022-11-22
 */
@Data
@ToString
@Accessors(chain = true)
@TableName("biz_rules_message_log")
@ApiModel(value = "RulesMessageLog对象", description = "规则发送消息记录")
public class RulesMessageLog extends BaseEntity {

    @TableField("record_id")
    private Long recordId;

    @TableField("rules_id")
    private Long rulesId;

    @TableField("message_id")
    private String messageId;

    @TableField("send_time")
    private Date sendTime;

    @TableField("user_id")
    private Long userId;

    //关联表
    @TableField("association_table")
    private String  associationTable;

    //关联表的主键ID
    @TableField("association_business_id")
    private Long associationBusinessId;


}