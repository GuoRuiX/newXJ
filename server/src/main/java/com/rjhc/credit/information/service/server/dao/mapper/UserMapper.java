package com.rjhc.credit.information.service.server.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjhc.credit.information.service.api.model.dto.UserDto;
import com.rjhc.credit.information.service.server.dao.dataobject.User;

/**
 * @ClassName UserMapper
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/20
 * @Version V1.0
 **/
public interface UserMapper extends BaseMapper<User> {

    UserDto selUserNameAndPsd(User user);
}
