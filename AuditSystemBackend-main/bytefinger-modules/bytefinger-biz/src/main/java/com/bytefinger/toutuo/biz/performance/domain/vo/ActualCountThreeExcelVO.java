package com.bytefinger.toutuo.biz.performance.domain.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@ApiModel(value = "动态业绩统计")
@ContentRowHeight(20)
@ColumnWidth(25)
public class ActualCountThreeExcelVO {

    @ExcelProperty("费项")
    private String label;

    @ExcelProperty("统计")
    private String rate;

    @ExcelProperty("类型")
    private String type;

    @ExcelProperty("全年")
    private String value;

}
