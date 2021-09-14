package com.rjhc.credit.information.service.server.common.convertor;

import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.credit.information.service.api.model.param.StatisticalAnalysisParam;
import com.rjhc.credit.information.service.server.dao.dataobject.StatisticalAnalysis;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @ClassName StatisticalAnalysisConvertor
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/
@Mapper
public interface StatisticalAnalysisConvertor {
    StatisticalAnalysisConvertor INSTANCE = Mappers.getMapper(StatisticalAnalysisConvertor.class);

    StatisticalAnalysisDto entityToDto(StatisticalAnalysis statisticalAnalysis);
    StatisticalAnalysis dtoToentity(StatisticalAnalysisDto statisticalAnalysis);

    StatisticalAnalysis prarmToentity(StatisticalAnalysisParam statisticalAnalysisParam);

    StatisticalAnalysisParam entityToParam(StatisticalAnalysis statisticalAnalysis);

    List<StatisticalAnalysisDto> entityListToDtoList(List<StatisticalAnalysis> list);
}
