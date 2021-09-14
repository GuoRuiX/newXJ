package com.rjhc.credit.information.service.server.middleware;

import com.rjhc.credit.information.service.api.model.dto.CountyDto;
import com.rjhc.credit.information.service.api.model.param.CountyParam;
import com.rjhc.credit.information.service.server.dao.mapper.StatisticalAnalysisMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @ClassName staaysn
 * @Description: TODO
 * @Author grx
 * @Date 2020/12/23
 * @Version V1.0
 **/
@Slf4j
@Component
public class Staaysn {
    @Resource
    private StatisticalAnalysisMapper statisticalAnalysisMapper;
    @Async
    public Future<List<CountyDto>> slecountyYear(CountyParam countyParam){
        List<CountyDto> slecounty = new ArrayList<>();

        try {
            slecounty = statisticalAnalysisMapper.slecounty(countyParam);
        }catch (Exception e){
            log.error(countyParam.getTableName()+"当前不存在此年份数据");
        }
        return new AsyncResult<>(slecounty);

    }


}
