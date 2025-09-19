package com.bytefinger.toutuo.biz.auditstep.dto;


import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class OaTodo {

    private String appName;
    private String modelName;
    private String modelId;
    private String subject;
    private String link;
    private String mobileLink;
    private String padLink;
    private int type;
    private String key;
    private String param1;
    private String param2;
    private Map<String, String> targets;
    private String createTime;
    private Map<String, String> docCreator;
    private String level;
    private String extendContent;
    private String others;

}
