package com.bytefinger.toutuo.biz.oa.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Map;

/**
 * 用户登录对象
 *
 * @author patrick
 */
@Data
public class OaApprovalDTO {

    private String attachmentValues;
    private String authAreaId;
    private String docContent;
    private Map<String, String> docCreator;
    private String docProperty;
    private String docStatus;
    private String docSubject;
    private String fdId;
    private String fdKeyword;
    private String fdSource;
    private String fdTemplateId;
    private Map<String, String> formValues;
    private OaApprovalFlowParamDTO flowParam;
}
