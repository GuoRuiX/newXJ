package com.rjhc.credit.information.service.server.common.convertor;

import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.param.PoorHouseholdsInformationParam;
import com.rjhc.credit.information.service.server.dao.dataobject.PoorHouseholdsInformation;
import com.rjhc.credit.information.service.server.dao.dataobject.WaringPoor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @ClassName PoorHouseholdsInformationConvertor
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/14
 * @Version V1.0
 **/
@Mapper
public interface PoorHouseholdsInformationConvertor {
    PoorHouseholdsInformationConvertor INSTANCE = Mappers.getMapper(PoorHouseholdsInformationConvertor.class);

    PoorHouseholdsInformationDto entityToDto(PoorHouseholdsInformation poorHouseholdsInformation);

    PoorHouseholdsInformation prarmToentity(PoorHouseholdsInformationParam poorHouseholdsInformationParam);

    PoorHouseholdsInformationParam entityToParam(PoorHouseholdsInformation poorHouseholdsInformation);

    List<PoorHouseholdsInformationDto> entityListToDtoList(List<PoorHouseholdsInformation> list);
    WaringPoor entityToentity(PoorHouseholdsInformationDto poorHouseholdsInformation);
}
