package com.bytefinger.toutuo.biz.workbrief.dto;

import lombok.Data;

@Data
public class WorkPushUserDto {

    private Long briefId;

    private String userId;

    private Integer pushStatus;

    private String userName;
}
