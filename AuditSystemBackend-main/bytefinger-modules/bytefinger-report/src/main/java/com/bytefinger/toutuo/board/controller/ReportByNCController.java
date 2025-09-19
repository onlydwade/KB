package com.bytefinger.toutuo.board.controller;

import cn.hutool.core.convert.Convert;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.security.utils.poi.ExcelUtil;
import com.bytefinger.toutuo.board.domain.ReportData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * NC系统报表管理控制器*
 */
@Slf4j
@Api(tags = "NC系统报表管理")
@RestController
@RequestMapping("/reportByNc")
public class ReportByNCController extends BaseController {

    @ApiOperation(value = "NC系统指标报表坐标导出")
    @PostMapping("/export")
    public void reportData(@RequestBody Map<String, Object> params) throws Exception {
        String reportcode = Convert.toStr(params.get("reportcode"));
        String period = Convert.toStr(params.get("period"));
        String organizationcode = Convert.toStr(params.get("organizationcode"));
        //这块是采用调用接口方式，目前未提供则伪造数据
        List<ReportData> reportDataList = new ArrayList<>();
        ReportData h29 = new ReportData("H29", "技术投入比率(%)本月实际值", "0.0");
        ReportData g29 = new ReportData("G29", "技术投入比率(%)较差值", "2.0");
        reportDataList.add(h29);
        reportDataList.add(g29);
        ExcelUtil<ReportData> excelUtil = new ExcelUtil<>(ReportData.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), reportDataList, "NC系统报表","","NC系统指标报表坐标导出");
    }
}
