package com.rjhc.credit.information.service.server.middleware.easyexcel;

import com.alibaba.excel.event.WriteHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @ClassName StyleExcelHandler
 * @Description: excel表格设置样式
 * @Author grx
 * @Date 2020/8/14
 * @Version V1.0
 **/
public class StyleExcelHandler implements WriteHandler {
    @Override
    public void sheet(int i, Sheet sheet) {
        sheet.setColumnWidth(0,100*80);
        sheet.setColumnWidth(1,100*80);
        sheet.setColumnWidth(2,100*80);
        sheet.setColumnWidth(3,100*80);

    }

    @Override
    public void row(int i, Row row) {
        /*row.setHeightInPoints(30);*/

    }

    @Override
    public void cell(int i, Cell cell) {
        /*Workbook workbook = cell.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setWrapText(false);*/

    }
}
