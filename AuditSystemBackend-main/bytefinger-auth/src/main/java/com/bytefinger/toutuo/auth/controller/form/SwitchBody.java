package com.bytefinger.toutuo.auth.controller.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户切换身份
 *
 * @author patrick
 */
@Data
public class SwitchBody {

    /**
     * 部门id
     */
    @NotNull(message = "参数不能为空")
    private Long deptId;

    /**
     * 岗位id
     */
    @NotNull(message = "参数不能为空")
    private Long postId;

}
