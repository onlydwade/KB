package com.bytefinger.toutuo.biz.businessopportunity.domain.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author chengwei
 * @date 2023/12/19 14:13
 **/
@Data
public class BusinessOpportunityRequestParam  {
    //发布时间
    private List<Long> updateTime;
    private List<Integer> province;
    private List<Integer> city;
    private List<Integer> propertyType;
    private List<String> industryClass;
    private List<Integer> tenderFirstCategoryList;
    private List<Integer> tenderSecondCategoryList;
    private List<Integer> amount;
    private Integer startAmount;
    private Integer endAmount;
    private List<Integer> tagType;
    private List<Integer> proGid;
    private List<Integer> noticeSegmentType;
    private String keyWord;
//    存量参数
    private List<Long> expireTime;
    private List<String> expireDateTime;
    private List<Integer> bidingTypeList;


    private Integer pageSize;
    private Integer pageNo;
}
