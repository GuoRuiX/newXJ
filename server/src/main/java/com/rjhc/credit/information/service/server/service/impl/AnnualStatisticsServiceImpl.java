package com.rjhc.credit.information.service.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rjhc.credit.information.service.api.model.dto.AnnualStatisticsDto;
import com.rjhc.credit.information.service.server.common.convertor.AnnualStatisticsConvertor;
import com.rjhc.credit.information.service.server.dao.dataobject.AnnualStatistics;
import com.rjhc.credit.information.service.server.dao.mapper.AnnualStatisticsMapper;
import com.rjhc.credit.information.service.server.dao.mapper.StatisticalAnalysisMapper;
import com.rjhc.credit.information.service.server.service.AnnualStatisticsService;
import com.rjhc.matrix.framework.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @ClassName AnnualStatisticsServiceImpl
 * @Description: TODO
 * @Author grx
 * @Date 2020/10/10
 * @Version V1.0
 **/
@Service
public class AnnualStatisticsServiceImpl implements AnnualStatisticsService {
    @Resource
    private AnnualStatisticsMapper annualStatisticsMapper;
    @Resource
    private StatisticalAnalysisMapper statisticalAnalysisMapper;
    @Autowired
    private AysAnnualStatistics aysAnnualStatistics;
    @Override
    public List<AnnualStatisticsDto> queryAll() throws ExecutionException, InterruptedException {
        Calendar date = Calendar.getInstance();
        String s = String.valueOf(date.get(Calendar.YEAR));
        String tableName="agricultural_organizations";
        //查询当前年度信息
        Map<String, Object> agricultural_organizations = statisticalAnalysisMapper.countAll("agricultural_organizations");
        LambdaQueryWrapper<AnnualStatistics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AnnualStatistics::getSynchronizationTime);
        List<AnnualStatistics> annualStatistics = annualStatisticsMapper.selectList(queryWrapper);
        AnnualStatistics annualStatistics1 = new AnnualStatistics();
        annualStatistics1.setYears(s);
        annualStatistics1.setId(RandomUtil.UUID36());
        if(agricultural_organizations.get("loanamount") != null){
            annualStatistics1.setTotalCredit(new BigDecimal(agricultural_organizations.get("loanamount").toString()));
        }
        if(agricultural_organizations.get("loanbalance") != null){
            annualStatistics1.setCreditBalance(new BigDecimal(agricultural_organizations.get("loanbalance").toString()));
        }
        annualStatistics.add(annualStatistics1);
        List<AnnualStatisticsDto> annualStatisticsDtos = AnnualStatisticsConvertor.INSTANCE.entityListToDtoList(annualStatistics);
        for (AnnualStatisticsDto annualStatistic : annualStatisticsDtos) {
            //根据年份查询当前数据总数
            if(annualStatistic.getYears().equals(s)){
                Future<Long> l = aysAnnualStatistics.getcount(tableName);
                annualStatistic.setCounts(l.get());
            }else {
                Future<Long> l = aysAnnualStatistics.getcount(tableName + "_" + annualStatistic.getYears());
                annualStatistic.setCounts(l.get());
            }

        }
        return annualStatisticsDtos;
    }

    @Override
    public List<String> selYear() {
        List<String> strList = annualStatisticsMapper.selYear();
        Calendar date = Calendar.getInstance();
        String s = String.valueOf(date.get(Calendar.YEAR));
        strList.add(s);
        return strList;
    }
}
