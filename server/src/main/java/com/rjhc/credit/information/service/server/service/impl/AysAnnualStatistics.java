package com.rjhc.credit.information.service.server.service.impl;

import com.rjhc.credit.information.service.server.dao.mapper.AnnualStatisticsMapper;
import com.rjhc.credit.information.service.server.dao.mapper.StatisticalAnalysisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @ClassName AysAnnualStatistics
 * @Description: TODO
 * @Author grx
 * @Date 2021/7/8
 * @Version V1.0
 **/
@Component
public class AysAnnualStatistics {
    @Autowired
    private StatisticalAnalysisMapper statisticalAnalysisMapper;
    @Async
    public Future<Long> getcount(String tableName){
        long l = statisticalAnalysisMapper.selCount(tableName);
        return new AsyncResult<>(l);
    }
}
