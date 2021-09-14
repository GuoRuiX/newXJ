package com.rjhc.credit.information.service.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.UserDto;
import com.rjhc.credit.information.service.api.model.param.UserParam;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName UserManagerIntterface
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/27
 * @Version V1.0
 **/
@RequestMapping("/userMana")
@Api(value = "用户管理", tags = {"用户管理"})
public interface UserManagerIntterface {
    /**
     * 功能描述：
     * 〈用户列表展示〉
     * @Author: grx
     * @Date: 1:41 下午 2020/8/27
     * @param userParam
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.rjhc.credit.information.service.api.model.dto.UserDto>>
     */
    @ApiOperation(value = "用户管理列表展示", notes = "用户管理列表展示")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping
    RestfulApiResponse<Page<UserDto>> queryAuditRecords(@RequestBody UserParam userParam);
}
