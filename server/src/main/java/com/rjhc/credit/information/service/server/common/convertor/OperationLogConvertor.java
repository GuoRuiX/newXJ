package com.rjhc.credit.information.service.server.common.convertor;

import com.rjhc.credit.information.service.api.model.dto.OperationLogDto;
import com.rjhc.credit.information.service.api.model.param.OperationLogParam;
import com.rjhc.credit.information.service.server.dao.dataobject.OperationLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @ClassName OperationLogConvertor
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/26
 * @Version V1.0
 **/
@Mapper
public interface OperationLogConvertor {
    OperationLogConvertor INSTANCE = Mappers.getMapper(OperationLogConvertor.class);
    List<OperationLogDto> entityListToDtoList(List<OperationLog> list);
    OperationLogParam entityToParam(OperationLog poorHouseholdsInformation);
}
