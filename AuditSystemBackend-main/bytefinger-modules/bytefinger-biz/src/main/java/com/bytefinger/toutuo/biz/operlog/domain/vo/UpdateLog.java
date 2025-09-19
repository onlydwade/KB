package com.bytefinger.toutuo.biz.operlog.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author pat
 * @date 2022/10/12 14:13
 **/
@Data
@Builder
public class UpdateLog {

    private String fieldName;
    private String valueBefore;
    private String valueAfter;

}
