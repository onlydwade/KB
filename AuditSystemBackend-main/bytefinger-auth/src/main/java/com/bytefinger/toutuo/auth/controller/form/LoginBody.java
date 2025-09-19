package com.bytefinger.toutuo.auth.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 用户登录对象
 *
 * @author patrick
 */
@Data
public class LoginBody {

    @NotEmpty(message = "请填写用户名")
    @Size(min = 5, max = 100, message = "用户名不在指定范围")
    private String username;

    @NotEmpty(message = "请输入密码")
    @Size(min = 5, max = 100, message = "密码不在指定范围")
    private String password;

}
