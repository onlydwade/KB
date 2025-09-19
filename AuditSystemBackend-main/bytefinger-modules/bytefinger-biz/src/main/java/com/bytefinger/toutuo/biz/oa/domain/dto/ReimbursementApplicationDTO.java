package com.bytefinger.toutuo.biz.oa.domain.dto;

import lombok.Data;

import java.util.Map;

/**
 * 费用报销系统申请
 *
 * @author chengwei
 */
@Data
public class ReimbursementApplicationDTO {
    /**
     * 项目编号
     */
    private String ttCode;
    /**
     * 项目名称
     */
    private String ttName;
    /**
     * 所属大区
     */
    private String ssdq;
    /**
     * 归属单位
     */
    private String ssgs;
    /**
     * 是否有效(YES/NO)
     */
    private String sfyx;
    /**
     * 状态(默认 01:拓展中)
     */
    private String zt;
    /**
     * 参标单位
     */
    private String cbdw;
    /**
     * 招标编号
     */
    private String zbbh;
    /**
     * 投标截止时间
     */
    private String tbjzsj;
    /**
     * 招标编号
     */
    private String kbsj;
    /**
     * 开标时间
     */
    private String zbdw;
    /**
     * 招标单位联系方式
     */
    private String zbdwlx;
    /**
     * 招标代理机构
     */
    private String zbdljg;
    /**
     * 招标代理机构联系方式
     */
    private String dljglx;
    /**
     * 归属单位
     */
    private String gsdw;
    /**
     * 参标单位
     */
    private String cbdwtext;
    /**
     * 来源系统(直接默认传递04)
     */
    private String lyxt;
}
