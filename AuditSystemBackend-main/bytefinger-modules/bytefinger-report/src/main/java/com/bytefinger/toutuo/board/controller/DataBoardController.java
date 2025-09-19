package com.bytefinger.toutuo.board.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.exception.ServiceException;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.poi.ExcelUtil;
import com.bytefinger.common.security.utils.SysConfigUtils;
import com.bytefinger.toutuo.api.system.core.domain.SysDictType;
import com.bytefinger.toutuo.board.aop.GetDataRole;
import com.bytefinger.toutuo.board.domain.*;
import com.bytefinger.toutuo.board.domain.PerformanceTemplate;
import com.bytefinger.toutuo.board.domain.ProjectCity;
import com.bytefinger.toutuo.board.domain.SysDept;
import com.bytefinger.toutuo.board.mapper.DataBoardMapper;
import com.bytefinger.toutuo.board.mapper.SysDictTypeMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据看板 前端控制器
 * </p>
 *
 * @author lc
 * @since 2023-03-14
 */
@Slf4j
@Api(tags = "数据看板")
@AllArgsConstructor
@RestController
@RequestMapping("/dataBoard")
public class DataBoardController extends BaseController {

    private final DataBoardMapper dataBoardMapper;

    private final SysDictTypeMapper sysDictTypeMapper;


    @ApiOperation(value = "城市分布")
    @GetMapping("/getCity/{currLevel}/{recordId}/{currYear}/{zaiguan}/{code}")
    public AjaxResult getCity(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear, @PathVariable("zaiguan") Long zaiguan, @PathVariable("code") Long code) {
        List<ProjectCity> projectCounts = Lists.newArrayList();
        String areaCode = code.toString();
        switch (areaCode) {//查询省
            case "0":
                projectCounts = dataBoardMapper.getCityByProvince(currLevel, recordId, currYear,zaiguan, new GetDataRole().dataRole());
                break;
            default:
                projectCounts = dataBoardMapper.getCityByCity(currLevel, recordId, currYear,zaiguan, new GetDataRole().dataRole());
                for (ProjectCity projectCity : projectCounts) {
                    ProjectCity parent = dataBoardMapper.getParentNameByCode(projectCity.getProvinceCode());
                    projectCity.setParentName(parent.getParentName());
                }
                break;

        }
        return success(projectCounts);
    }

    @ApiOperation(value = "项目业态占比")
    @GetMapping("/getProjectYETAI/{currLevel}/{recordId}/{currYear}/{dictTypeName}/{zhaiguan}")
    public AjaxResult getProjectYETAI(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear, @PathVariable("dictTypeName") String dictTypeName,@PathVariable("zhaiguan") Integer zhaiguan) {
        //为空代表一级，有值则执行二级查询
        List<Map> projectYETAIAll = new ArrayList<>();
        if (StrUtil.isNotBlank(dictTypeName) && "XIANG_MU_YE_TAI".equals(dictTypeName)) {
            List<SysDictType> sysDictTypeList = sysDictTypeMapper.selectList(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictPtype, dictTypeName).eq(SysDictType::getStatus, 0));
            for (SysDictType sysDictType : sysDictTypeList) {
                Map<String, Object> projectYETAIParams = new HashMap<>();
                projectYETAIParams.put("label", sysDictType.getDictName());
                projectYETAIParams.put("value", sysDictType.getDictType());
                List<Map> projectYETAI = dataBoardMapper.getProjectYETAI(currLevel, recordId, currYear, zhaiguan ,new GetDataRole().dataRole(), sysDictType.getDictType());
                BigDecimal contractAmountSum = BigDecimal.valueOf(0);
                if (CollectionUtil.isNotEmpty(projectYETAI)){
                    for (Map map : projectYETAI) {
                        contractAmountSum = contractAmountSum.add(BigDecimal.valueOf(Double.valueOf(map.get("contractAmount").toString())));
                    }
                }
                projectYETAIParams.put("contractAmount", contractAmountSum);
                projectYETAIAll.add(projectYETAIParams);
            }
        }else {
            projectYETAIAll = dataBoardMapper.getProjectYETAI(currLevel, recordId, currYear, zhaiguan ,new GetDataRole().dataRole(), dictTypeName);
        }
        //如果传入一级业态数据，则走原来逻辑
        BigDecimal contractAmountSum = BigDecimal.valueOf(0);
        if (CollectionUtil.isNotEmpty(projectYETAIAll)){
            for (Map map : projectYETAIAll) {
                contractAmountSum = contractAmountSum.add(BigDecimal.valueOf(Double.valueOf(map.get("contractAmount").toString())));
            }
        }
        Map result = new HashMap();
        result.put("contractAmountSum", contractAmountSum);
        result.put("projectYETAI", projectYETAIAll);
        return success(result);
    }

    @ApiOperation(value = "拓展模式占比")
    @GetMapping("/getExpansionMode/{currLevel}/{recordId}/{currYear}/{type}")
    public AjaxResult getExpansionMode(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear, @PathVariable("type") String type) {


        List<Map> zhongMap = dataBoardMapper.getExpansionMode(currLevel, recordId, currYear,type, null, new GetDataRole().dataRole());

        return success(zhongMap);
    }

    /**
     * 获取模板信息
     *
     * @return
     */
    public List<PerformanceTemplate> getTemplate(String budgetKey) {
        // 创建年月数据不记录数据，返回前端渲染
        String cacheKey = SysConfigUtils.getConfigCache(budgetKey);
        if (StringUtils.isBlank(cacheKey)) {
            throw new ServiceException("数据尚未初始化，请联系管理员");
        }

        List<PerformanceTemplate> budgetTemplates = com.alibaba.fastjson2.JSON.parseArray(cacheKey, PerformanceTemplate.class);
        Collections.sort(budgetTemplates, (o1, o2) -> o1.getSorts().compareTo(o1.getSorts()));
        return budgetTemplates;
    }

    @ApiOperation(value = "业绩达成情况")
    @GetMapping("/getAchievementsThree/{currLevel}/{recordId}/{currYear}")
    public AjaxResult getAchievementsThree(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear) {
        List<Map> result = new ArrayList<>();

        //获取考核指标项
        String budgetKey = "BIZ_BUDGET_IN";
        List<PerformanceTemplate> performanceTemplates = this.getTemplate(budgetKey);

        performanceTemplates.forEach(performanceTemplate -> {
            //根据指标项获取权限列表
            List<Long> ids = dataBoardMapper.getIndicatorDisplayLevelIds(performanceTemplate.getFieldKey());

            if (ids.contains(recordId)) {
                Map data = new HashMap();
                data.put("key",performanceTemplate.getFieldKey());
                data.put("name",performanceTemplate.getFieldName());
                result.add(data);
            }
        });

        //目标
        List<Map> mbMap = dataBoardMapper.getBudget(currLevel, recordId, currYear, new GetDataRole().dataRole());

        //实际金额
        Map actual = dataBoardMapper.getActual(currLevel, recordId, currYear, new GetDataRole().dataRole());

        //实际有效信息提报量
        Integer actualValid = dataBoardMapper.getActualValid(currLevel, recordId, currYear, new GetDataRole().dataRole());

        //项目保留率
        String projectRetention = dataBoardMapper.selectProjectPerformanceBl(
                currYear,
                recordId
        );

        if(result.size() > 0){
            result.forEach(data->{
                mbMap.forEach(mb->{
                    if(data.get("key").equals(mb.get("fieldKey"))){
                        data.put("mb", mb.getOrDefault("fieldValue", 0.00));
                    }
                });
                String key = data.get("key").toString();
                PerformanceTemplate performanceTemplateToUnit = performanceTemplates.stream().filter(performanceTemplate -> performanceTemplate.getFieldKey().equals(key)).findFirst().orElse(null);
                String unit = "";
                if (null!=performanceTemplateToUnit&&StringUtils.isNotBlank(performanceTemplateToUnit.getFieldUnitName())){
                    unit = "（"+performanceTemplateToUnit.getFieldUnitName()+"）";
                }
                data.put("unit", unit);

                if ("HTZJE".equals(data.get("key"))) {
                    data.put("sj", actual.getOrDefault("contractAmount", 0.00));
                }
                if ("HTNDJE".equals(data.get("key"))) {
                    data.put("sj", actual.getOrDefault("contractAnnualAmount", 0.00));
                }
                if ("ZDTZHTJE".equals(data.get("key"))) {
                    data.put("sj", actual.getOrDefault("zdtzhtje", 0.00));
                }
                if ("YXXXTBL".equals(data.get("key"))) {
                    data.put("sj",actualValid);
                }
                if ("XMBLL".equals(data.get("key"))) {
                    data.put("sj",  projectRetention);
                }

            });
        }else{

        }

        return success(result);
    }

    @ApiOperation(value = "业绩达成情况")
    @GetMapping("/getAchievements/{currLevel}/{recordId}/{currYear}")
    public AjaxResult getAchievements(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear) {
        //预算目标
        List<Map> budget = dataBoardMapper.getBudget(currLevel, recordId, currYear, new GetDataRole().dataRole());
        //实际金额
        Map actual = dataBoardMapper.getActual(currLevel, recordId, currYear, new GetDataRole().dataRole());
        //实际有效信息提报量
        Integer actualValid = dataBoardMapper.getActualValid(currLevel, recordId, currYear, new GetDataRole().dataRole());
        Map result = new HashMap();
        if (budget.size() > 0) {
            for (Map map : budget) {
                String fieldKey = map.get("fieldKey").toString();
                if ("HTZJE".equals(fieldKey)) {
                    Map htzje = new HashMap();
                    htzje.put("buget", map.getOrDefault("fieldValue", 0.00));
                    htzje.put("actual", actual.getOrDefault("contractAmount", 0.00));
                    result.put("htzje", htzje);
                }
                if ("HTNDJE".equals(fieldKey)) {
                    Map htndje = new HashMap();
                    htndje.put("buget", map.getOrDefault("fieldValue", 0.00));
                    htndje.put("actual", actual.getOrDefault("contractAnnualAmount", 0.00));
                    result.put("htndje", htndje);
                }
                if ("YXXXTBL".equals(fieldKey)) {
                    Map yxxxtbl = new HashMap();
                    yxxxtbl.put("buget", map.getOrDefault("fieldValue", 0.00));
                    yxxxtbl.put("actual", actualValid);
                    result.put("yxxxtbl", yxxxtbl);
                }
            }
        } else {
            Map htzje = new HashMap();
            htzje.put("buget", 0.00);
            htzje.put("actual", actual.getOrDefault("contractAmount", 0.00));
            result.put("htzje", htzje);
            Map htndje = new HashMap();
            htndje.put("buget", 0.00);
            htndje.put("actual", actual.getOrDefault("contractAnnualAmount", 0.00));
            result.put("htndje", htndje);
            Map zdtzhtje = new HashMap();
            zdtzhtje.put("buget", 0.00);
            zdtzhtje.put("actual", actualValid);
            result.put("zdtzhtje", zdtzhtje);
            Map yxxxtbl = new HashMap();
            yxxxtbl.put("buget", 0.00);
            yxxxtbl.put("actual", actualValid);
            result.put("yxxxtbl", yxxxtbl);
            Map xmbll = new HashMap();
            xmbll.put("buget", 0.00);
            xmbll.put("actual", actualValid);
            result.put("xmbll", xmbll);
        }
        return success(result);
    }


    @ApiOperation(value = "业绩达成情况-详情")
    @PostMapping("/getAchievementsDetails")
    public AjaxResult getAchievementsDetails(@RequestBody JSONObject params) { //@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear
        Integer currLevel = params.getInteger("currLevel");
        Long recordId = params.getLong("recordId");
        Integer currYear = params.getInteger("currYear");
        params.put("tabKey",1);
        List<Map<String,Object>> dataList = new ArrayList<>();
        getChildAchievementsDetailsDetails(dataList,currLevel,recordId,currYear);
        return AjaxResult.success(dataList);
    }

    /**
     * 业绩完成情况详情*
     *
     * @param dataList
     * @param currLevel
     * @param recordId
     * @param currYear
     */
    public void getChildAchievementsDetailsDetails(List<Map<String, Object>> dataList, Integer currLevel, Long recordId, Integer currYear) {
        IPage<Map<String, Object>> page = new Page<>(1, Integer.MAX_VALUE);
        JSONObject params = new JSONObject();
        params.put("recordId", recordId);
        params.put("tabKey", 1);
        Page<Map<String, Object>> resultPage = dataBoardMapper.getDeptPageByParentId(page, params);
        List<Map<String, Object>> records = resultPage.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            for (int i = 0; i < records.size(); i++) {
                Map<String, Object> map = records.get(i);
                Integer childCurrLevel = (Integer) map.get("currLevel");
                Long childRecordId = (Long) map.get("recordId");
                Long parentId = (Long) map.get("parentId");
                String companyName = (String) map.get("companyName");
                String regionName = (String) map.get("regionName");
                AjaxResult achievementsResultTree = getAchievementsThree(childCurrLevel, childRecordId, currYear);
                if (achievementsResultTree.isSuccess() && achievementsResultTree.containsKey("data")) {
                    List<Map> data = (List<Map>) achievementsResultTree.get("data");
                    //重新封装
                    Map<String, Object> dataTwo = new HashMap();
                    dataTwo.put("regionName", regionName);
                    dataTwo.put("companyName", companyName);
                    dataTwo.put("recordId", childRecordId);
                    dataTwo.put("key", childRecordId);
                    dataTwo.put("parentId", parentId);
                    List<Map<String, Object>> childDataList = new ArrayList<>();
                    if (currLevel == 1 && childCurrLevel == 2) {
                        dataTwo.put("children", childDataList);
                    }
                    // 填充map
                    Map<String, Object> resultUpperCase = new HashMap<>();
                    for (Map datum : data) {
                        resultUpperCase.put(datum.get("key").toString(), datum.containsKey("sj")?datum.get("sj"):"0");
                    }
                    dataTwo.put("HTZJE", resultUpperCase.containsKey("HTZJE") ? resultUpperCase.getOrDefault("HTZJE", "0") : "0");
                    dataTwo.put("HTNDJE", resultUpperCase.containsKey("HTNDJE") ? resultUpperCase.getOrDefault("HTNDJE", "0") : "0");
                    dataTwo.put("YXXXTBL", resultUpperCase.containsKey("YXXXTBL") ? resultUpperCase.getOrDefault("YXXXTBL", "0") : "0");
                    dataTwo.put("ZDTZHTJE", resultUpperCase.containsKey("ZDTZHTJE") ? resultUpperCase.getOrDefault("ZDTZHTJE", "0") : "0");
                    dataTwo.put("XMBLL", resultUpperCase.containsKey("XMBLL") ? resultUpperCase.getOrDefault("XMBLL", "0") : "0");
                    dataTwo.putAll(resultUpperCase);
                    dataList.add(dataTwo);
                }
            }
        }


        //根据主导扩展量排序
        dataList.sort(((o1, o2) -> {
            if (o2.containsKey("ZDTZHTJE") && o1.containsKey("ZDTZHTJE")) {
                String dqzdl1 = o2.get("ZDTZHTJE").toString();
                String dqzdl2 = o1.get("ZDTZHTJE").toString();
                //从小到大
                return (int) (Double.parseDouble(dqzdl1) - Double.parseDouble(dqzdl2));//此处定义比较规则，o2.age-o1.age即为从大到小
            }
            return 0;
        }));
        ////初始化序号
        //Integer[] arr = {1};
        //dataList = dataList.stream().peek(e -> {
        //    e.put("num", arr[0]++);
        //}).collect(Collectors.toList());
    }


    @ApiOperation(value = "业绩达成情况-详情-导出")
    @DataFill
    @PostMapping("/achievementsDetailsExport")
    public void achievementsDetailsExport(@RequestBody JSONObject params) {
        Long recordId = params.getLong("recordId");
        Integer currLevel = params.getInteger("currLevel");
        Integer currYear = params.getInteger("currYear");
        params.put("tabKey", 3);
        //子单位
        IPage<Map<String, Object>> page = new Page<>(1, Integer.MAX_VALUE);
        Page<Map<String, Object>> resultPage = dataBoardMapper.getDeptPageByParentId(page, params);
        List<Map<String, Object>> records = resultPage.getRecords();
        ExcelUtil<AchievementsDetailsExport> excelUtil = new ExcelUtil<>(AchievementsDetailsExport.class);
        if (CollectionUtils.isEmpty(records)) {
            excelUtil.exportExcelNoHead(ServletUtils.getResponse(), null, "业绩达成情况", true);
            return;
        }
        List<AchievementsDetailsExport> achievementsDetailsExports = new ArrayList<>();

        //处理头部标题
        //顺序
        List<String> headSortList = new ArrayList<>();
        //名称
        List<String> headNameList = new ArrayList<>();
        AjaxResult achievementsHeadResultTree = getAchievementsThree(currLevel, recordId, currYear);
        if (achievementsHeadResultTree.isSuccess() && achievementsHeadResultTree.containsKey("data")) {
            List<String> radioStrList = com.google.common.collect.Lists.newArrayList("HTZJE", "HTNDJE", "ZDTZHTJE", "YXXXTBL", "XMBLL");
            List<Map> data = (List<Map>) achievementsHeadResultTree.get("data");
            //固定列
            headNameList.add("上级");
            headNameList.add("大区/单位");
            List<PerformanceTemplate> performanceTemplates = this.getTemplate("BIZ_BUDGET_IN");
            for (int i = 0; i < data.size(); i++) {
                Object key = data.get(i).get("key");
                if (radioStrList.contains(key.toString())) {
                    if (i <= 4) {
                        String name = data.get(i).get("name").toString();
                        PerformanceTemplate performanceTemplateToUnit = performanceTemplates.stream().filter(performanceTemplate -> performanceTemplate.getFieldKey().equals(key)).findFirst().orElse(null);
                        String unit = "";
                        if (null!=performanceTemplateToUnit&&StringUtils.isNotBlank(performanceTemplateToUnit.getFieldUnitName())){
                            unit = "（"+performanceTemplateToUnit.getFieldUnitName()+"）";
                        }
                        headNameList.add(name+unit);
                    }
                    headSortList.add(key.toString());
                }
            }
        }

        //处理数据
        if (CollectionUtil.isNotEmpty(records)) {
            for (Map<String, Object> record : records) {
                Integer childCurrLevel = (Integer) record.get("currLevel");
                Long childRecordId = (Long) record.get("recordId");
                Long parentId = (Long) record.get("parentId");
                String companyName = (String) record.get("companyName");
                String regionName = (String) record.get("regionName");
                AjaxResult achievementsResultTree = getAchievementsThree(childCurrLevel, childRecordId, currYear);
                if (achievementsResultTree.isSuccess() && achievementsResultTree.containsKey("data")&&childCurrLevel<=3) {
                    List<Map> data = (List<Map>) achievementsResultTree.get("data");
                    AchievementsDetailsExport achievementsDetailsExport = new AchievementsDetailsExport();
                    //重新封装
                    Map<String, Object> dataTwo = new HashMap();
                    dataTwo.put("regionName", regionName);
                    dataTwo.put("companyName", companyName);
                    dataTwo.put("recordId", childRecordId);
                    dataTwo.put("key", childRecordId);
                    dataTwo.put("parentId", parentId);
                    List<Map<String, Object>> childDataList = new ArrayList<>();
                    if (currLevel == 1 && childCurrLevel == 2) {
                        dataTwo.put("children", childDataList);
                    }
                    // 填充map
                    Map<String, Object> resultUpperCase = new HashMap<>();
                    for (Map datum : data) {
                        resultUpperCase.put(datum.get("key").toString(), datum.containsKey("sj")?datum.get("sj"):"0");
                    }
                    dataTwo.put("HTZJE", resultUpperCase.containsKey("HTZJE") ? resultUpperCase.getOrDefault("HTZJE", "0") : "0");
                    dataTwo.put("HTNDJE", resultUpperCase.containsKey("HTNDJE") ? resultUpperCase.getOrDefault("HTNDJE", "0") : "0");
                    dataTwo.put("YXXXTBL", resultUpperCase.containsKey("YXXXTBL") ? resultUpperCase.getOrDefault("YXXXTBL", "0") : "0");
                    dataTwo.put("ZDTZHTJE", resultUpperCase.containsKey("ZDTZHTJE") ? resultUpperCase.getOrDefault("ZDTZHTJE", "0") : "0");
                    dataTwo.put("XMBLL", resultUpperCase.containsKey("XMBLL") ? resultUpperCase.getOrDefault("XMBLL", "0") : "0");
                    dataTwo.putAll(resultUpperCase);
                    achievementsDetailsExport.setRecordId(childRecordId);
                    achievementsDetailsExport.setParentId(parentId);
                    achievementsDetailsExport.setLevel(childCurrLevel);
                    achievementsDetailsExport.setRegionName(dataTwo.get("regionName").toString());
                    achievementsDetailsExport.setCompanyName(dataTwo.get("companyName").toString());
                    for (int i = 0; i < headSortList.size(); i++) {
                        if (i==0){
                            achievementsDetailsExport.setHtzje(dataTwo.get(headSortList.get(0)).toString());
                        } else if (i==1){
                            achievementsDetailsExport.setHtndje(dataTwo.get(headSortList.get(1)).toString());
                        } else if (i==2){
                            achievementsDetailsExport.setYxxxtbl(dataTwo.get(headSortList.get(2)).toString());
                        } else if (i==3){
                            achievementsDetailsExport.setZdtzhtje(dataTwo.get(headSortList.get(3)).toString());
                        } else if (i==4){
                            achievementsDetailsExport.setXmbll(dataTwo.get(headSortList.get(4)).toString());
                        }
                    }
                    achievementsDetailsExports.add(achievementsDetailsExport);
                }
            }
        }

        List<AchievementsDetailsExport> achievementsDetailsExportsAll = new ArrayList<>();
        //再次根据大区层级排序
        if (currLevel==1){
            List<AchievementsDetailsExport> achievementsDetailsExportsTwo = achievementsDetailsExports.stream().filter(achievementsDetailsExport -> achievementsDetailsExport.getLevel() == 2).collect(Collectors.toList());
            //根据主导扩展量排序
            achievementsDetailsExportsTwo.sort(((o1, o2) -> {
                String dqzdl1 = null==o2.getZdtzhtje()?"0":o2.getZdtzhtje();
                String dqzdl2 = null==o1.getZdtzhtje()?"0":o1.getZdtzhtje();
                //从小到大
                return (int) (Double.parseDouble(dqzdl1) - Double.parseDouble(dqzdl2));//此处定义比较规则，o2.age-o1.age即为从大到小
            }));

            //子级
            for (AchievementsDetailsExport achievementsDetailsExport : achievementsDetailsExportsTwo) {
                achievementsDetailsExportsAll.add(achievementsDetailsExport);
                List<AchievementsDetailsExport> achievementsDetailsExportsThreeList = achievementsDetailsExports.stream().filter(achievementsDetailsExportThree -> achievementsDetailsExportThree.getParentId().equals(achievementsDetailsExport.getRecordId())).collect(Collectors.toList());
                //根据主导扩展量排序
                achievementsDetailsExportsThreeList.sort(((o1, o2) -> {
                    String dqzdl1 = null==o2.getZdtzhtje()?"0":o2.getZdtzhtje();
                    String dqzdl2 = null==o1.getZdtzhtje()?"0":o1.getZdtzhtje();
                    //从小到大
                    return (int) (Double.parseDouble(dqzdl1) - Double.parseDouble(dqzdl2));//此处定义比较规则，o2.age-o1.age即为从大到小
                }));
                achievementsDetailsExportsAll.addAll(achievementsDetailsExportsThreeList);
            }
        }else {
            //根据主导扩展量排序
            achievementsDetailsExports.sort(((o1, o2) -> {
                String dqzdl1 = null==o2.getZdtzhtje()?"0":o2.getZdtzhtje();
                String dqzdl2 = null==o1.getZdtzhtje()?"0":o1.getZdtzhtje();
                //从小到大
                return (int) (Double.parseDouble(dqzdl1) - Double.parseDouble(dqzdl2));//此处定义比较规则，o2.age-o1.age即为从大到小
            }));
            achievementsDetailsExportsAll.addAll(achievementsDetailsExports);
        }
        excelUtil.exportExcelCustomHead(ServletUtils.getResponse(),headNameList, achievementsDetailsExportsAll, "业绩达成情况");
    }

    @ApiOperation(value = "投标情况")
    @GetMapping("/getBidding/{currLevel}/{recordId}/{currYear}/{zgType}/{tbType}/{zbType}")
    public AjaxResult getBidding(
            @PathVariable("currLevel") Integer currLevel,
            @PathVariable("recordId") Long recordId,
            @PathVariable("currYear") Integer currYear,
            @PathVariable("zgType") Integer zgType,
            @PathVariable("tbType") Integer tbType,
            @PathVariable("zbType") Integer zbType
    ) {
        Map<String,Object>  result = new HashMap<>(3);

        int currentYear = Year.now().getValue();

        boolean isCurrentYear = false;
        if(currYear == currentYear){
            isCurrentYear = true;
        }

        Map biddingAll = dataBoardMapper.getBidding(currLevel, recordId, isCurrentYear,currYear, zgType,tbType,zbType, new GetDataRole().dataRole());

        result.put("total", biddingAll.get("total"));
        result.put("zhongbiao", biddingAll.get("zhongbiao"));
        result.put("contractAmount", biddingAll.get("contractAmount"));

        return success(result);
    }

    @ApiOperation(value = "漏斗分析")
    @GetMapping("/getFunnelAnalysis/{currLevel}/{recordId}/{currYear}")
    public AjaxResult getFunnelAnalysis(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear) {
        Map<String,Object> result = new HashMap<>(3);

        //单一和非单一
        Map<String,BigDecimal> projectTotal = dataBoardMapper.getProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());

        //股权合作
        //Map<String,BigDecimal> projectTotalGU = dataBoardMapper.getProjectTotalGU(currLevel, recordId, currYear, new GetDataRole().dataRole());

        Map<String,BigDecimal> data = new HashMap<>(3);
        data.put("xmxxzl", projectTotal.get("xmxxzl"));
        data.put("xmgjzl", projectTotal.get("xmgjzl"));
        data.put("cgxmzl", projectTotal.get("cgxmzl"));

        result.put("data",data);
        result.put("total",data.get("xmxxzl").add(data.get("xmgjzl")).add(data.get("cgxmzl")));
        return success(result);
    }

    @ApiOperation(value = "整体项目情况")
    @GetMapping("/getProjectSituation/{currLevel}/{recordId}/{currYear}")
    public AjaxResult getProjectSituation(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear) {
        Map result = new HashMap();
        Integer projectTotal = dataBoardMapper.getInTubeProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        String waiProjectTotal = dataBoardMapper.getWaiProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        Integer signProjectTotal = dataBoardMapper.getSignProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        String contractConversion = dataBoardMapper.getContractConversion(currLevel, recordId, currYear, new GetDataRole().dataRole());
        AjaxResult achievements = getAchievements(currLevel, recordId, currYear);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(achievements));
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject htzje = data.getJSONObject("htzje");
        JSONObject htndje = data.getJSONObject("htndje");
        result.put("projectTotal", projectTotal);//在管项目总数
        result.put("waiProjectTotal", waiProjectTotal);//在管项目面积总数
        result.put("signProjectTotal", signProjectTotal);//当年签约项目总数
        result.put("contractConversion",new BigDecimal(contractConversion).setScale(2, BigDecimal.ROUND_HALF_UP));//合同当年转化收入
        result.put("contractTarget", htzje.get("buget"));//目标合同总金额
        result.put("contractActual", htzje.get("actual"));//实际合同总金额
        result.put("contractTargetYear", null == htndje ? 0.00 : htndje.get("buget"));//目标年度合同总金额
        result.put("contractActualYear", null == htndje ? 0.00 : htndje.get("actual"));//实际年度合同总金额
        return success(result);
    }

    @ApiOperation(value = "整体项目情况")
    @GetMapping("/getProjectSituationThree/{currLevel}/{recordId}/{currYear}")
    public AjaxResult getProjectSituationThree(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear) {
        Map<String,Object> result = new HashMap();

        //在管项目总数
        Integer projectTotal = dataBoardMapper.getInTubeProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        result.put("projectTotal", projectTotal);

        //在管项目面积总数
        String waiProjectTotal = dataBoardMapper.getWaiProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        result.put("waiProjectTotal", waiProjectTotal);

        //当年新增面积总数
        String newWaiProjectTotal = dataBoardMapper.getNewWaiProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        result.put("newWaiProjectTotal", newWaiProjectTotal);

        //新签项目总数
        Integer signProjectTotal = dataBoardMapper.getSignProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        result.put("signProjectTotal", signProjectTotal);

        //续签项目总数
        Integer signRenewalProjectTotal = dataBoardMapper.getSignRenewalProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        result.put("signRenewalProjectTotal", signRenewalProjectTotal);

        //续签为是
        Map<String,String> xuQian = dataBoardMapper.getContractTotal("XU_QIAN",currLevel, recordId, currYear, new GetDataRole().dataRole());

        //续签为否
        Map<String,String> feiXuQian = dataBoardMapper.getContractTotal("FEI_XU_QIAN",currLevel, recordId, currYear, new GetDataRole().dataRole());

        //当年新增合同转化收入
        result.put("xzzhsr",feiXuQian.get("zzhsr"));

        //新增合同总金额
        result.put("xzzje",feiXuQian.get("zzje"));

        //新增合同年度金额
        result.put("xzndzje",feiXuQian.get("nndzje"));

        //续签合同总金额
        result.put("xqzje",xuQian.get("zzje"));

        //续签合同年度金额
        result.put("xqndzje",xuQian.get("nndzje"));

        return success(result);
    }

    @ApiOperation(value = "整体项目情况-详情")
    @PostMapping("/getProjectSituationThreeDetails")
    public AjaxResult getProjectSituationThreeDetails(@RequestBody JSONObject params) { //@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear
        Integer currYear = params.getInteger("currYear");
        Integer pageNo = params.getInteger("pageNo");
        Integer pageSize = params.getInteger("pageSize");
        IPage<Map<String,Object>> page = new Page<>(pageNo,pageSize);
        Page<Map<String,Object>> resultPage = dataBoardMapper.getDeptPageByParentId(page,params);
        List<Map<String, Object>> deptList = resultPage.getRecords();
        List<Map<String, Object>> deptListNew = new ArrayList<>();
        for (Map<String, Object> map : deptList) {
            Integer childCurrLevel = (Integer) map.get("currLevel");
            Long childRecordId =(Long) map.get("recordId");
            AjaxResult projectSituationThree = getProjectSituationThree(childCurrLevel, childRecordId, currYear);
            if (projectSituationThree.isError()||!projectSituationThree.containsKey("data")){
                continue;
            }
            Map<String,Object> data = (Map<String, Object>) projectSituationThree.get("data");
            map.putAll(data);
            deptListNew.add(map);
        }
        resultPage.setRecords(deptListNew);
        return success(resultPage);
    }

    @ApiOperation(value = "整体项目情况-详情-导出")
    @DataFill
    @PostMapping("/projectSituationThreeDetailsExport")
    public void projectSituationThreeDetailsExport(@RequestBody JSONObject params) {
        params.put("pageSize",Integer.MAX_VALUE);
        String tabKey = params.getString("tabKey");
        AjaxResult projectSituationThreeDetailResult = getProjectSituationThreeDetails(params);
        if (tabKey.equals("1")){//大区
            ExcelUtil<ProjectSituationThreeDetailsRegionExport> excelUtil = new ExcelUtil<>(ProjectSituationThreeDetailsRegionExport.class);
            List<ProjectSituationThreeDetailsRegionExport> projectSituationThreeDetailsExports = new ArrayList<>();
            if (projectSituationThreeDetailResult.isSuccess()&&projectSituationThreeDetailResult.containsKey("data")){
                Page pageResult = (Page) projectSituationThreeDetailResult.get("data");
                List<Map<String,Object>> data = pageResult.getRecords();
                for (Map<String, Object> dataMap : data) {
                    ProjectSituationThreeDetailsRegionExport projectSituationThreeDetailsRegionExport = new ProjectSituationThreeDetailsRegionExport();
                    projectSituationThreeDetailsRegionExport.setRegionName(dataMap.get("companyName").toString());
                    projectSituationThreeDetailsRegionExport.setProjectTotal(dataMap.get("projectTotal").toString());
                    projectSituationThreeDetailsRegionExport.setWaiProjectTotal(dataMap.get("waiProjectTotal").toString());
                    projectSituationThreeDetailsRegionExport.setSignProjectTotal(dataMap.get("signProjectTotal").toString());
                    projectSituationThreeDetailsRegionExport.setNewWaiProjectTotal(dataMap.get("newWaiProjectTotal").toString());
                    projectSituationThreeDetailsRegionExport.setXzzhsr(dataMap.get("xzzhsr").toString());
                    projectSituationThreeDetailsRegionExport.setXzzje(dataMap.get("xzzje").toString());
                    projectSituationThreeDetailsRegionExport.setXzndzje(dataMap.get("xzndzje").toString());
                    projectSituationThreeDetailsRegionExport.setSignRenewalProjectTotal(dataMap.get("signRenewalProjectTotal").toString());
                    projectSituationThreeDetailsRegionExport.setXqzje(dataMap.get("xqzje").toString());
                    projectSituationThreeDetailsRegionExport.setXqndzje(dataMap.get("xqndzje").toString());
                    projectSituationThreeDetailsExports.add(projectSituationThreeDetailsRegionExport);
                }
            }
            excelUtil.exportExcel(ServletUtils.getResponse(), projectSituationThreeDetailsExports, "整体项目情况");
        }else {//单位
            ExcelUtil<ProjectSituationThreeDetailsCompanyExport> excelUtil = new ExcelUtil<>(ProjectSituationThreeDetailsCompanyExport.class);
            List<ProjectSituationThreeDetailsCompanyExport> projectSituationThreeDetailsExports = new ArrayList<>();
            if (projectSituationThreeDetailResult.isSuccess()&&projectSituationThreeDetailResult.containsKey("data")){
                Page pageResult = (Page) projectSituationThreeDetailResult.get("data");
                List<Map<String,Object>> data = pageResult.getRecords();
                for (Map<String, Object> dataMap : data) {
                    ProjectSituationThreeDetailsCompanyExport projectSituationThreeDetailsCompanyExport = new ProjectSituationThreeDetailsCompanyExport();
                    projectSituationThreeDetailsCompanyExport.setRegionName(dataMap.get("regionName").toString());
                    projectSituationThreeDetailsCompanyExport.setCompanyName(dataMap.get("companyName").toString());
                    projectSituationThreeDetailsCompanyExport.setProjectTotal(dataMap.get("projectTotal").toString());
                    projectSituationThreeDetailsCompanyExport.setWaiProjectTotal(dataMap.get("waiProjectTotal").toString());
                    projectSituationThreeDetailsCompanyExport.setSignProjectTotal(dataMap.get("signProjectTotal").toString());
                    projectSituationThreeDetailsCompanyExport.setXzzhsr(dataMap.get("xzzhsr").toString());
                    projectSituationThreeDetailsCompanyExport.setXzzje(dataMap.get("xzzje").toString());
                    projectSituationThreeDetailsCompanyExport.setXzndzje(dataMap.get("xzndzje").toString());
                    projectSituationThreeDetailsCompanyExport.setSignRenewalProjectTotal(dataMap.get("signRenewalProjectTotal").toString());
                    projectSituationThreeDetailsCompanyExport.setXqzje(dataMap.get("xqzje").toString());
                    projectSituationThreeDetailsCompanyExport.setXqndzje(dataMap.get("xqndzje").toString());
                    projectSituationThreeDetailsExports.add(projectSituationThreeDetailsCompanyExport);
                }
            }
            excelUtil.exportExcel(ServletUtils.getResponse(), projectSituationThreeDetailsExports, "整体项目情况");
        }
    }


    @ApiOperation(value = "实际签约情况")
    @GetMapping("/getActualSigning/{currLevel}/{recordId}/{currYear}")
    public AjaxResult getActualSigning(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear) {
        List<Map> actualSigning = dataBoardMapper.getActualSigning(currLevel, recordId, currYear, new GetDataRole().dataRole());
        return success(actualSigning);
    }

    @ApiOperation(value = "签约趋势及排名")
    @GetMapping("/getSigning/{currLevel}/{recordId}/{currYear}")
    public AjaxResult getSigning(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear) {
        Date start = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, currYear + "-01-01 00:00:00");
        List<String> months = Lists.newArrayList();
        for (long i = 0; i < 12; i++) {
            Date d = DateUtils.addMonths(start, (int) i);
            String ym = DateUtils.parseDateToStr(DateUtils.YYYY_MM, d);
            months.add(ym);
        }
        Map salesVolume = dataBoardMapper.getSalesVolume(currLevel, recordId, months, new GetDataRole().dataRole());
        List<Map> salesVolumeList = new ArrayList<>();
        for (String month : months) {
            Map resultSales = new HashMap();
            resultSales.put("month", month);
            resultSales.put("value", null == salesVolume ? 0 : salesVolume.get(month));
            salesVolumeList.add(resultSales);
        }

        List<Map> cityRanking = dataBoardMapper.getCityRanking(currLevel, recordId, currYear, new GetDataRole().dataRole());
        Map resultMap = new HashMap();
        //销售额
        resultMap.put("salesVolume", salesVolumeList);
        //城市排名
        resultMap.put("cityRanking", cityRanking);
        return success(resultMap);
    }

    @ApiOperation(value = "整体项目情况")
    @GetMapping("/getProjectSituationDept/{currLevel}/{recordId}/{currYear}")
    public AjaxResult getProjectSituationDept(@PathVariable("currLevel") Integer currLevel, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear) {
        Map result = new HashMap();
        SysDept byId = dataBoardMapper.getOneById(recordId);
        // 迭代二需求：如果选的机构是部门的话，都要读取“宝石花总部”的数据
        if(ObjectUtil.equals(byId.getDeptType(),"BU_MEN")){
            recordId = byId.getParentId();
        }
        Integer projectTotal = dataBoardMapper.getInTubeProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        String waiProjectTotal = dataBoardMapper.getWaiProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        Integer signProjectTotal = dataBoardMapper.getSignProjectTotal(currLevel, recordId, currYear, new GetDataRole().dataRole());
        String contractConversion = dataBoardMapper.getContractConversion(currLevel, recordId, currYear, new GetDataRole().dataRole());
        AjaxResult achievements = getAchievements(currLevel, recordId, currYear);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(achievements));
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject htzje = data.getJSONObject("htzje");
        JSONObject htndje = data.getJSONObject("htndje");
        result.put("projectTotal", projectTotal);//在管项目总数
        result.put("waiProjectTotal", waiProjectTotal);//在管项目面积总数
        result.put("signProjectTotal", signProjectTotal);//当年签约项目总数
        result.put("contractConversion",new BigDecimal(contractConversion).setScale(2, BigDecimal.ROUND_HALF_UP));//合同当年转化收入

        BigDecimal bugetYuan = new BigDecimal(0);
        if (null==htzje){
            htzje = new JSONObject();
        }
        if (htzje.containsKey("buget")){
            bugetYuan =new BigDecimal(htzje.get("buget").toString());
        }
        bugetYuan = bugetYuan.divide(new BigDecimal("10000"));

        BigDecimal actualYuan = new BigDecimal(0);
        if (htzje.containsKey("actual")){
            actualYuan =new BigDecimal(htzje.get("actual").toString());
        }
        actualYuan = actualYuan.divide(new BigDecimal("10000"));

        result.put("contractTarget", bugetYuan);//目标合同总金额
        result.put("contractActual", actualYuan);//实际合同总金额

        BigDecimal htndjeBugetYuan = new BigDecimal(0);
        if (null==htndje){
            htndje = new JSONObject();
        }
        if (htndje.containsKey("buget")){
            htndjeBugetYuan =new BigDecimal(htndje.get("actual").toString());
        }
        htndjeBugetYuan = htndjeBugetYuan.divide(new BigDecimal("10000"));

        BigDecimal htndjeActualYuan = new BigDecimal(0);
        if (htndje.containsKey("buget")){
            htndjeActualYuan =new BigDecimal(htndje.get("actual").toString());
        }
        htndjeActualYuan = htndjeActualYuan.divide(new BigDecimal("10000"));

        result.put("contractTargetYear", htndjeBugetYuan);//目标年度合同总金额
        result.put("contractActualYear", htndjeActualYuan);//实际年度合同总金额
        return success(result);
    }
}
