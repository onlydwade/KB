package com.bytefinger.toutuo.system.core.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * <p>
 * 通知公告表
 * </p>
 *
 * @author patrick
 * @since 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_notice")
@ApiModel(value = "SysNotice对象", description = "通知公告表")
public class SysNotice extends Model<SysNotice> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "公告ID", hidden = false, required = false)
    @JSONField(name = "noticeId", serialize = true)
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Integer noticeId;

    @ApiModelProperty(value = "公告标题", hidden = false, required = false)
    @JSONField(name = "noticeTitle", serialize = true)
    @TableField("notice_title")
    private String noticeTitle;

    @ApiModelProperty(value = "公告类型（1通知 2公告）", hidden = false, required = false)
    @JSONField(name = "noticeType", serialize = true)
    @TableField("notice_type")
    private String noticeType;

    @ApiModelProperty(value = "公告内容", hidden = false, required = false)
    @JSONField(name = "noticeContent", serialize = true)
    @TableField("notice_content")
    private Blob noticeContent;

    @ApiModelProperty(value = "公告状态（0正常 1关闭）", hidden = false, required = false)
    @JSONField(name = "status", serialize = true)
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "创建者", hidden = false, required = false)
    @JSONField(name = "createBy", serialize = true)
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @JSONField(name = "createTime", serialize = true)
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新者", hidden = false, required = false)
    @JSONField(name = "updateBy", serialize = true)
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    @JSONField(name = "updateTime", serialize = true)
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @JSONField(name = "remark", serialize = true)
    @TableField("remark")
    private String remark;


    @Override
    protected Serializable pkVal() {
        return this.noticeId;
    }

}