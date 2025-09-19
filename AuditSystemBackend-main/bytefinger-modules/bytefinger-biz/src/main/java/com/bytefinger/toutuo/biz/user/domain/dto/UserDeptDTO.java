package com.bytefinger.toutuo.biz.user.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author pat
 * @date 2022/10/13 15:30
 **/
@Data
public class UserDeptDTO {

    @NotNull(message = "用户id不能为空")
    private List<Long> userIds;

    @NotNull(message = "部门id不能为空")
    private Long deptId;

}
