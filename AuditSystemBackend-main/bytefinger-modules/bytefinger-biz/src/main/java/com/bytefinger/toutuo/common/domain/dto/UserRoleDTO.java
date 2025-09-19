package com.bytefinger.toutuo.common.domain.dto;

import lombok.Data;

/**
 * @author pat
 * @date 2022/11/08 13:33
 **/
@Data
public class UserRoleDTO {

    private Long userId;
    private Integer level;
    private Long deptId;

}
