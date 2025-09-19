package com.bytefinger.toutuo.biz.performance.domain.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CustomMergeStrategy extends AbstractMergeStrategy  {

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer integer) {
        int rowIndex = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();

        // A列（索引为0）和B列（索引为1）的合并逻辑
        if (columnIndex == 0 || columnIndex == 1) {
            if (rowIndex >= 2 && rowIndex <= 11 && rowIndex % 2 == 0) {
                // 合并上一行和当前行的相同列
                int firstRow = rowIndex - 1;
                int lastRow = rowIndex;
                CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, columnIndex, columnIndex);
                sheet.addMergedRegion(region);
            }
        }
    }
}
