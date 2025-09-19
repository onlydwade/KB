package com.bytefinger.toutuo.board.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.board.domain.ProjectCity;
import com.bytefinger.toutuo.board.domain.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface DataBoardMapper {

    List<ProjectCity> getCityByProvince (@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId, @Param("currYear") Integer currYear,@Param("zaiguan") Long zaiguan,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    List<ProjectCity> getCityByCity (@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,  @Param("currYear") Integer currYear,@Param("zaiguan") Long zaiguan,@Param(Constants.COLUMN_MAP) Map<String, Object> params);
    ProjectCity getParentNameByCode (@Param("code")Long code);

    List getProjectYETAI(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId, @Param("currYear") Integer currYear, @Param("zaiguan") Integer zaiguan,@Param(Constants.COLUMN_MAP) Map<String, Object> params,@Param("dictTypeName")String dictTypeName);

    Map getSalesVolume(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("months") List<String> months,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    List<Map> getCityRanking(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    List<Map> getActualSigning(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    List<Map> getBudget(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Map getActual(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Integer getActualValid(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    List<Map> getExpansionMode(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId, @Param("currYear") Integer currYear,@Param("type") String type,@Param("expansionMode") List<String> expansionMode, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Map getBidding(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("isCurrentYear") Boolean isCurrentYear,@Param("currYear") Integer currYear,@Param("zgType") Integer zgType,@Param("tbType") Integer tbType,@Param("zbType") Integer zbType,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Integer getInTubeProjectTotal(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    String getWaiProjectTotal(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);;

    Integer getSignProjectTotal(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);;

    String getContractConversion(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);;

    Map<String, BigDecimal> getProjectTotal(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId, @Param("currYear") Integer currYear, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Map<String, BigDecimal> getProjectTotalGU(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Integer getSuccessTotal(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear, @Param("projectType") List<String> projectTypeDan, @Param("stepMenuCode")List<String> stepMenuCodeDan,@Param(Constants.COLUMN_MAP) Map<String, Object> dataRole);


    Integer getSuccessTotalGU(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear, @Param("projectType") List<String> projectTypeDan, @Param("stepMenuCode")List<String> stepMenuCodeDan,@Param(Constants.COLUMN_MAP) Map<String, Object> dataRole);

    SysDept getOneById(@Param("id") Long id);

    String getNewWaiProjectTotal(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Integer getSignRenewalProjectTotal(@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);;

    Map<String,String> getContractTotal(@Param("type") String type,@Param("currLevel") Integer currLevel, @Param("recordId")Long recordId,@Param("currYear") Integer currYear,@Param(Constants.COLUMN_MAP) Map<String, Object> params);;

    List<Long> getIndicatorDisplayLevelIds(String fieldKey);

    String selectProjectPerformanceBl(@Param("currYear") Integer currYear, @Param("recordId")Long recordId);

    Page<Map<String, Object>> getDeptPageByParentId(IPage<Map<String, Object>> page, @Param("params") JSONObject params);


}
