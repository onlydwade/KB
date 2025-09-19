package com.bytefinger.toutuo.api.biz.user.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 系统用户部门岗位表
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_dept_post")
@ApiModel(value = "SysUserDeptPost对象", description = "系统用户部门岗位表")
@NoArgsConstructor
@AllArgsConstructor
public class SysUserDeptPost {

    @ApiModelProperty(value = "用户id", hidden = false, required = false)
    @JSONField(name = "userId", serialize = true)
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;

    @ApiModelProperty(value = "部门id", hidden = false, required = false)
    @JSONField(name = "deptId", serialize = true)
    @TableField("dept_id")
    private Long deptId;

    @ApiModelProperty(value = "岗位id", hidden = false, required = false)
    @JSONField(name = "postId", serialize = true)
    @TableField("post_id")
    private Long postId;

    @ApiModelProperty(value = "最后一次切换岗位时间", hidden = false, required = false)
    @JSONField(name = "updateTime", serialize = true)
    @TableField("update_time")
    private Date updateTime;

}