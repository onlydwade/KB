package com.bytefinger.common.core.web.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户数据返回值
 *
 * @author pat
 * @date 2022/10/19 09:55
 **/
@Data
@Accessors(chain = true)
public class UserVO {

    private Long id;

    private Long recordId;

    private Long userId;

    private String realname;

    private String avatar;

    private Long deptId;

    private Long postId;

    private String userName;

    private String phonenumber;

    private String status;

}
