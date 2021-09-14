package com.rjhc.credit.information.service.server.common.convertor;

import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.dto.UserDto;
import com.rjhc.credit.information.service.api.model.param.PoorHouseholdsInformationParam;
import com.rjhc.credit.information.service.api.model.param.UserParam;
import com.rjhc.credit.information.service.server.dao.dataobject.PoorHouseholdsInformation;
import com.rjhc.credit.information.service.server.dao.dataobject.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @ClassName UserConverTor
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/20
 * @Version V1.0
 **/
@Mapper
public interface UserConverTor {
    UserConverTor INSTANCE = Mappers.getMapper(UserConverTor.class);

    UserDto entityToDto(User user);

    User prarmToentity(UserParam userParam);

    UserParam entityToParam(User User);

    List<UserDto> entityListToDtoList(List<User> list);
}
