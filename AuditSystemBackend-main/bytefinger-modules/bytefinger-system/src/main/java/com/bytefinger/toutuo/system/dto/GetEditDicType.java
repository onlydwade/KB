package com.bytefinger.toutuo.system.dto;

import lombok.Data;

import java.util.Map;

/**
 * @Author Jone Ying
 * @Date 2023/2/21
 **/
@Data
public class GetEditDicType {

    private Long dictId;

    private String dictName;

    private String dictType;

    private String remark;
}
