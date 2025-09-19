package com.bytefinger.toutuo.system.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author Jone Ying
 * @Date 2023/2/21
 **/
@Data
public class GetYeTaiDicData {

    private String value;

    private List<GetYeTaiDicData> children;

    private String label;
}
