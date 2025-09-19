package com.bytefinger.toutuo.biz.user.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 岗位信息表
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_post")
@ApiModel(value = "SysPost对象", description = "岗位信息表")
public class SysPost extends Model<SysPost> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "岗位ID", hidden = false, required = false)
    @JSONField(name = "postId", serialize = true)
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;

    @ApiModelProperty(value = "数据权限id", hidden = false, required = false)
    @JSONField(name = "dataRoleId", serialize = true)
    @TableField("data_role_id")
    private Long dataRoleId;

    @ApiModelProperty(value = "岗位编码", hidden = false, required = false)
    @JSONField(name = "postCode", serialize = true)
    @TableField("post_code")
    private String postCode;

    @ApiModelProperty(value = "岗位名称", hidden = false, required = false)
    @JSONField(name = "postName", serialize = true)
    @TableField("post_name")
    private String postName;

    @ApiModelProperty(value = "显示顺序", hidden = false, required = false)
    @JSONField(name = "postSort", serialize = true)
    @TableField("post_sort")
    private Integer postSort;

    @ApiModelProperty(value = "状态（0正常 1停用）", hidden = false, required = false)
    @JSONField(name = "status", serialize = true)
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "级别", hidden = false, required = false)
    @JSONField(name = "level", serialize = true)
    @TableField("level")
    private Integer level;

    @ApiModelProperty(value = "创建者", hidden = false, required = false)
    @JSONField(name = "createBy", serialize = true)
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @JSONField(name = "createTime", serialize = true)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新者", hidden = false, required = false)
    @JSONField(name = "updateBy", serialize = true)
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    @JSONField(name = "updateTime", serialize = true)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @JSONField(name = "remark", serialize = true)
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "类型", hidden = false, required = false)
    @JSONField(name = "type", serialize = true)
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "赢标宝账户", hidden = false, required = false)
    @JSONField(name = "ybbUser", serialize = true)
    @TableField("ybb_user")
    private String ybbUser;

    /**
     * 菜单组
     */
    @TableField(exist = false)
    private Long[] menuIds;

}