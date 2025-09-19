package com.bytefinger.toutuo.biz.oa.domain.dto;

import lombok.Data;

import java.util.Map;

/**
 * 用户登录对象
 *
 * @author patrick
 */
@Data
public class OaApprovalFlowParamDTO {

    private Map<String, String> handler;

    private String operationType;
}
