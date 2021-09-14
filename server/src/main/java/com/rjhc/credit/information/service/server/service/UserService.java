package com.rjhc.credit.information.service.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.UserDto;
import com.rjhc.credit.information.service.api.model.param.UserParam;
import com.rjhc.credit.information.service.server.dao.dataobject.User;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserService
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/20
 * @Version V1.0
 **/
public interface UserService {
    /**
     * 功能描述：
     * 〈用户登录操作〉
     * @Author: grx
     * @Date: 1:17 下午 2020/8/27
     * @param userParam
     * @return: com.rjhc.credit.information.service.api.model.dto.UserDto
     */
    UserDto selUserNameAndPsd(UserParam userParam);
    /**
     * 功能描述：
     * 〈用户列表展示〉
     * @Author: grx
     * @Date: 1:17 下午 2020/8/27
     * @param userParam
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.rjhc.credit.information.service.server.dao.dataobject.User>
     */
    Page<User> selectByPage(UserParam userParam);
}
