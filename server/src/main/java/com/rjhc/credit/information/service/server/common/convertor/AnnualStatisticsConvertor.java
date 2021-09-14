package com.rjhc.credit.information.service.server.common.convertor;

import com.rjhc.credit.information.service.api.model.dto.AnnualStatisticsDto;
import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.credit.information.service.server.dao.dataobject.AnnualStatistics;
import com.rjhc.credit.information.service.server.dao.dataobject.StatisticalAnalysis;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @ClassName AnnualStatisticsConvertor
 * @Description: TODO
 * @Author grx
 * @Date 2021/7/8
 * @Version V1.0
 **/
@Mapper
public interface AnnualStatisticsConvertor {
    AnnualStatisticsConvertor INSTANCE = Mappers.getMapper(AnnualStatisticsConvertor.class);
    List<AnnualStatisticsDto> entityListToDtoList(List<AnnualStatistics> list);
}
