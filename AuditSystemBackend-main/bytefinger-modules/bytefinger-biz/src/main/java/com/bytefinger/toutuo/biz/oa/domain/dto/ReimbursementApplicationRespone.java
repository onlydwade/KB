package com.bytefinger.toutuo.biz.oa.domain.dto;
import java.util.*;
import lombok.Data;

/**
 * 费用报销系统申请
 *
 * @author chengwei
 */
@Data
public class ReimbursementApplicationRespone {
    /**
     * 描述信息
     */
    private String msg;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 数据详情
     */
    private List<ReimbursementApplicationRespone> list;

    /**
     * 编码唯一值
     */
    private String ttCode;
}
