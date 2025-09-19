package com.bytefinger.toutuo.biz.performance.domain.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentLoopMerge;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.OnceAbsoluteMerge;
import com.bytefinger.common.security.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@ApiModel(value = "动态业绩统计")
public class ActualCountThreeVO {

    private String key;

    private String label;

    private String rate;

    private String type;

    private String value;

    private String isMerge;

}
