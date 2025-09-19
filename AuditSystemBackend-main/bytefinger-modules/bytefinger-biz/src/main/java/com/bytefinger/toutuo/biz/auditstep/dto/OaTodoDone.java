package com.bytefinger.toutuo.biz.auditstep.dto;


import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class OaTodoDone {

    private String appName;
    private String modelName;
    private String modelId;
    private int optType;
    private String key;
    private String param1;
    private String param2;
    private int type;
    private Map<String, String> targets;
    private String others;

}
