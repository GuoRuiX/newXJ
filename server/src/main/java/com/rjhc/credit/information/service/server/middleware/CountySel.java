package com.rjhc.credit.information.service.server.middleware;

import com.rjhc.credit.information.service.api.model.dto.CountyDto;
import com.rjhc.credit.information.service.api.model.param.CountyParam;
import com.rjhc.credit.information.service.server.dao.mapper.StatisticalAnalysisMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName CountySel
 * @Description: TODO
 * @Author grx
 * @Date 2021/1/14
 * @Version V1.0
 **/
@Slf4j
@Component
public class CountySel {
    @Resource
    private StatisticalAnalysisMapper statisticalAnalysisMapper;
    @Async
    public Future<List<CountyDto>> selCountry(CountyParam countyParam){
        List<CountyDto> countyDtos = new ArrayList<CountyDto>();
        try {
             countyDtos = statisticalAnalysisMapper.selecountyByPoorNoTest(countyParam);
        }catch (Exception e){
            log.error(countyParam.getTableName()+"不存在");
        }
        return new AsyncResult<>(countyDtos);
    }
}
