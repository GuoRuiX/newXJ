package com.rjhc.credit.information.service.start.controller;

import com.rjhc.credit.information.service.api.AnnualStatisticsInterface;
import com.rjhc.credit.information.service.api.model.dto.AnnualStatisticsDto;
import com.rjhc.credit.information.service.api.model.param.CountyParam;
import com.rjhc.credit.information.service.server.dao.dataobject.AnnualStatistics;
import com.rjhc.credit.information.service.server.middleware.CountySel;
import com.rjhc.credit.information.service.server.middleware.log.OptionalLog;
import com.rjhc.credit.information.service.server.service.AnnualStatisticsService;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName AnnualStatisticsController
 * @Description: TODO
 * @Author grx
 * @Date 2020/10/10
 * @Version V1.0
 **/
@RestController
@Slf4j
public class AnnualStatisticsController implements AnnualStatisticsInterface {
    @Autowired
    private AnnualStatisticsService annualStatisticsService;
    @Autowired
    private CountySel countySel;
    @OptionalLog(module = "报表统计",operations = "综合年度统计")
    @Override
    public RestfulApiResponse<List<AnnualStatisticsDto>> queryReportStatistics() throws ExecutionException, InterruptedException {
        List<AnnualStatisticsDto> annualStatistics = annualStatisticsService.queryAll();
        return RestfulApiResponse.success(annualStatistics);
    }

    @Override
    public RestfulApiResponse<List<String>> selYear() {
        List<String> strings = annualStatisticsService.selYear();
        return RestfulApiResponse.success(strings);
    }

    @Override
    public RestfulApiResponse<List<String>> test11() {
        CountyParam countyParam = new CountyParam();
        countySel.selCountry(countyParam);
        return null;
    }
}
