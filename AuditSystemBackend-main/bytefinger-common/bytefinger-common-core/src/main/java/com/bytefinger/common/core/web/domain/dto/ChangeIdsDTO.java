package com.bytefinger.common.core.web.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author pat
 * @date 2022/10/19 10:36
 **/
@Data
public class ChangeIdsDTO {

    @NotNull(message = "请选择数据")
    private Long[] ids;

    private String remarks;

}
