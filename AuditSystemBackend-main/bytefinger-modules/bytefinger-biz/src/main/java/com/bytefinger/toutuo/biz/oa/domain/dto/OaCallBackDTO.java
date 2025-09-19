package com.bytefinger.toutuo.biz.oa.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 用户登录对象
 *
 * @author patrick
 */
@Data
public class OaCallBackDTO {

    /**
     * 审批编号
     */
    @NotEmpty(message = "审批编号不能为空")
    private String approvalNo;

    /***
     * 审批状态
     * 0  未发起审批 1:审批中  2:审批通过 3:审批驳回 4:作废 8:线下审批通过 9:无需审批
     **/
    @NotEmpty(message = "审批状态不能为空")
    private Integer approvalStatus;

    /**
     * OA审批结果内容
     */
    @NotEmpty(message = "OA审批结果内容不能为空")
    private String approvalResult;

    /**
     * oa对接审批状态
     */
    @NotEmpty(message = "oa对接审批状态不能为空")
    private Long oaStatus;

    /**
     * 审批时间
     */
    @NotEmpty(message = "审批时间不能为空")
    private Date approvalTime;

    /**
     * OA审批记录url
     */
    private String approvalUrl;

    @Override
    public String toString() {
        return "OaCallBackDTO{" +
                "approvalNo='" + approvalNo + '\'' +
                ", approvalStatus=" + approvalStatus +
                ", approvalResult='" + approvalResult + '\'' +
                ", oaStatus=" + oaStatus +
                ", approvalTime=" + approvalTime +
                ", approvalUrl='" + approvalUrl + '\'' +
                ", detailLog='" + detailLog + '\'' +
                '}';
    }

    /**
     * 审批数据
     */
//    @NotEmpty(message = "审批数据不能为空")
//    private String approvalData;

    private String detailLog;

    private String remark;
}
