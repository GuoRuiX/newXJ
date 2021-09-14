package com.rjhc.credit.information.service.start.controller;

import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.OperationLogInterface;
import com.rjhc.credit.information.service.api.model.dto.OperationLogDto;
import com.rjhc.credit.information.service.api.model.param.OperationLogParam;
import com.rjhc.credit.information.service.server.common.convertor.OperationLogConvertor;
import com.rjhc.credit.information.service.server.dao.dataobject.OperationLog;
import com.rjhc.credit.information.service.server.middleware.easyexcel.EasyExcelUtils;
import com.rjhc.credit.information.service.server.middleware.log.OptionalLog;
import com.rjhc.credit.information.service.server.service.OperationLogService;
import com.rjhc.credit.information.service.server.service.impl.OperationLogServiceImpl;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import com.rjhc.matrix.framework.core.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OperationLogController
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/25
 * @Version V1.0
 **/
@RestController
@Slf4j
public class OperationLogController implements OperationLogInterface {
    @Autowired
    private OperationLogService operationLogService;
    @Override
    @OptionalLog(module = "操作日志",operations = "查看")
    public RestfulApiResponse<Page<OperationLogDto>> selectLogs(OperationLogParam operationLogParam) {
        Page<OperationLog> operationLogPage = operationLogService.selectLogsByPage(operationLogParam);
        return RestfulApiResponse.success(operationLogPage);
    }
    @OptionalLog(module = "日志管理",operations = "日志导出")
    @Override
    public void exporExcel(String userName, String modelName, String operationName, String operationDate, HttpServletResponse response, HttpServletRequest request) throws Exception{
        OperationLog logs = new OperationLog();
        if(!"-".equals(userName)){
            logs.setUserName(userName);
        }
        if(!"-".equals(operationName)){
            logs.setOperationName(operationName);
        }
        if(!"-".equals(operationDate)){
            //格式化日期
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            logs.setOperationDate(format.parse(operationDate));
        }
        List<OperationLogDto> operationLogDtos = operationLogService.selectLogs(OperationLogConvertor.INSTANCE.entityToParam(logs));
        OutputStream outputStream =  response.getOutputStream();
        try {
            //添加响应头信息
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//固定格式
            response.setCharacterEncoding("utf-8");//固定格式
            String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            //数据导出
            Map<String, List<? extends BaseRowModel>> map = new HashMap<>();
            map.put("日志管理",operationLogDtos);
            EasyExcelUtils.createExcelStreamMutilByEaysExcel(response,map, ExcelTypeEnum.XLSX,"日志管理"+date,request);
        }catch (Exception e){
            log.error("日志导出失败");
        }



    }

    @Override
    public RestfulApiResponse<String> testDate() {
        operationLogService.testData();
        return null;
    }

}
