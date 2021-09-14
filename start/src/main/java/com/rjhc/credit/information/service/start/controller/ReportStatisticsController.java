package com.rjhc.credit.information.service.start.controller;

import com.rjhc.credit.information.service.api.ReportStatisticsInterface;
import com.rjhc.credit.information.service.api.model.param.CountyParam;
import com.rjhc.credit.information.service.api.model.param.ReportParam;
import com.rjhc.credit.information.service.api.model.param.StatisticalAnalysisParam;
import com.rjhc.credit.information.service.server.middleware.MysqlScheduled;
import com.rjhc.credit.information.service.server.middleware.log.OptionalLog;
import com.rjhc.credit.information.service.server.service.StatisticalAnalysisService;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName ReportStatisticsController
 * @Description: TODO
 * @Author grx
 * @Date 2020/10/10
 * @Version V1.0
 **/
@RestController
@Slf4j
public class ReportStatisticsController implements ReportStatisticsInterface {
    @Autowired
    private StatisticalAnalysisService statisticalAnalysisService;
    @Autowired
    private MysqlScheduled mysqlScheduled;
    @OptionalLog(module = "报表统计",operations = "各项指标数据")
    @Override
    public RestfulApiResponse<List<Map<String,String>>> queryReportStatistics(ReportParam reportParam) throws Exception {
        List<Map<String,String>> stringStringMap = statisticalAnalysisService.queryReportStatistics(reportParam);
        return RestfulApiResponse.success(stringStringMap);
    }

    @OptionalLog(module = "数据同步",operations = "数据同步")
    @Override
    public void updateTableName() throws Exception {
        mysqlScheduled.createTable();
    }


    @OptionalLog(module = "报表统计",operations = "地图信息显示")
    @Override
    public RestfulApiResponse<List<Map<String, String>>> selstaAndPoorByBankAddress(String years, String type) {
        return RestfulApiResponse.success(statisticalAnalysisService.selstaAndPoorByBankAddress(type,years,type));
    }


    @OptionalLog(module = "报表统计",operations = "根据金融机构查询信贷数量")
    @Override
    public RestfulApiResponse<List<Map<String, String>>> selStaCountByLevel(String years) {
        return RestfulApiResponse.success(statisticalAnalysisService.selStaCountByLevel(years));
    }
    @OptionalLog(module = "报表统计",operations = "根据时间统计信贷笔数")
    @Override
    public RestfulApiResponse<Long> selectByDate(StatisticalAnalysisParam statisticalAnalysisParam) {
        return RestfulApiResponse.success(statisticalAnalysisService.selectByDate(statisticalAnalysisParam));
    }
    @OptionalLog(module = "贫困县",operations = "贫困县信息统计")
    @Override
    public RestfulApiResponse<List<Map<String, String>>> slecounty(CountyParam countyParam) throws Exception {
        return RestfulApiResponse.success(statisticalAnalysisService.slecounty(countyParam));
    }


}
