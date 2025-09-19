package com.bytefinger.toutuo.system.dto;

import lombok.Data;

/**
 * @Author Jone Ying
 * @Date 2023/2/21
 **/
@Data
public class GetEditDicData {

    private Long dictCode;

    private Integer dictSort;

    private String dictLabel;

    private String dictValue;

    private String dictType;
}
