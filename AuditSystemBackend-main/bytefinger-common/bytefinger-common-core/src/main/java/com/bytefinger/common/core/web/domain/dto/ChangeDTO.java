package com.bytefinger.common.core.web.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author pat
 * @date 2022/10/19 10:36
 **/
@Data
public class ChangeDTO {

    @NotNull(message = "请选择数据")
    private Long id;

    private String recordType;

    private String approvalNo;

    private String remarks;

}
