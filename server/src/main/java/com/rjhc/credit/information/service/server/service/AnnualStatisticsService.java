package com.rjhc.credit.information.service.server.service;

import com.rjhc.credit.information.service.api.model.dto.AnnualStatisticsDto;
import com.rjhc.credit.information.service.server.dao.dataobject.AnnualStatistics;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName AnnualStatisticsService
 * @Description: TODO
 * @Author grx
 * @Date 2020/10/10
 * @Version V1.0
 **/
public interface AnnualStatisticsService {

    List<AnnualStatisticsDto> queryAll() throws ExecutionException, InterruptedException;
    List<String> selYear();
}
