package com.rjhc.credit.information.service.start.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.UserManagerIntterface;
import com.rjhc.credit.information.service.api.model.dto.UserDto;
import com.rjhc.credit.information.service.api.model.param.UserParam;
import com.rjhc.credit.information.service.server.dao.dataobject.User;
import com.rjhc.credit.information.service.server.middleware.log.OptionalLog;
import com.rjhc.credit.information.service.server.service.UserService;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserManagerController
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/27
 * @Version V1.0
 **/
@RestController
@Slf4j
public class UserManagerController implements UserManagerIntterface {
    @Autowired
    private UserService userService;

    @OptionalLog(module = "用户管理",operations = "列表展示")
    @Override
    public RestfulApiResponse<Page<UserDto>> queryAuditRecords(UserParam userParam) {
        Page<User> userPage = userService.selectByPage(userParam);
        return RestfulApiResponse.success(userPage);
    }
}
