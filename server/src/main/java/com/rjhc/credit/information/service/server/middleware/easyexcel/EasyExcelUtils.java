package com.rjhc.credit.information.service.server.middleware.easyexcel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EasyExcelUtils
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/14
 * @Version V1.0
 **/
@Slf4j
public class EasyExcelUtils {
    /**
     * 功能描述：
     * 〈〉
     * @Author: grx
     * @Date: 10:42 上午 2020/8/14
     * @param response
     * @param SheetNameAndDateList sheetName和每个sheet的数据
     * @param type 要导出的excel的类型 有ExcelTypeEnum.xls 和有ExcelTypeEnum.xlsx
     * @return: void
     */
    public static void createExcelStreamMutilByEaysExcel(HttpServletResponse response, Map<String, List<? extends BaseRowModel>> SheetNameAndDateList, ExcelTypeEnum type, String fileName, HttpServletRequest request) throws UnsupportedEncodingException {
        if (checkParam(SheetNameAndDateList, type)) return;
        try {

            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
/*            response.setHeader("Content-disposition",
                    String.format("attachment; filename=\"%s\"", fileName+type.getValue()));*/
//            response.setHeader( "Content-Disposition", "attachment;filename=" + new String( fileName.getBytes("gb2312"), "ISO8859-1" )+type.getValue() );
            response.setHeader("Content-disposition", "attachment;filename=" +  fileName+ type.getValue());
            ServletOutputStream out = response.getOutputStream();
            StyleExcelHandler styleExcelHandler = new StyleExcelHandler();
            ExcelWriter writer = new ExcelWriter(null,out, type, true,styleExcelHandler);
            setSheet(SheetNameAndDateList, writer);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 功能描述：
     * 〈〉
     * @Author: grx
     * @Date: 10:43 上午 2020/8/14
     * @param SheetNameAndDateList setSheet数据
     * @param writer
     * @return: void
     */
    private static void setSheet(Map<String, List<? extends BaseRowModel>> SheetNameAndDateList, ExcelWriter writer) {
        int sheetNum = 1;
        for (Map.Entry<String, List<? extends BaseRowModel>> stringListEntry : SheetNameAndDateList.entrySet()) {
            Sheet sheet = new Sheet(sheetNum, 0, stringListEntry.getValue().get(0).getClass());
            sheet.setSheetName(stringListEntry.getKey());

            writer.write(stringListEntry.getValue(), sheet);
            sheetNum++;

        }
    }

    /**
     * 功能描述：
     * 〈参数校验〉
     * @Author: grx
     * @Date: 10:43 上午 2020/8/14
     * @param SheetNameAndDateList
     * @param type
     * @return: boolean
     */
    private static boolean checkParam(Map<String, List<? extends BaseRowModel>> SheetNameAndDateList, ExcelTypeEnum type) {
        if (CollectionUtils.isEmpty(SheetNameAndDateList)) {
            log.error("SheetNameAndDateList不能为空");
            return true;
        } else if (type == null) {
            log.error("导出的excel类型不能为空");
            return true;
        }
        return false;
    }
}
