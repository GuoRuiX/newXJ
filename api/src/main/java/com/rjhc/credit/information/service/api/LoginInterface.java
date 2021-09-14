package com.rjhc.credit.information.service.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.dto.UserDto;
import com.rjhc.credit.information.service.api.model.param.AuthUserParam;
import com.rjhc.credit.information.service.api.model.param.PoorHouseholdsInformationParam;
import com.rjhc.credit.information.service.api.model.param.UserParam;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LoginInterface
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/18
 * @Version V1.0
 **/
@RequestMapping("/login")
@Api(value = "登录", tags = {"登录"})
public interface LoginInterface {
    /**
     * 功能描述：
     * 〈用户登录〉
     * @Author: grx
     * @Date: 1:15 下午 2020/8/27
     * @param userParam
     * @param request
     * @param response
     * @param httpSession
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<com.rjhc.credit.information.service.api.model.dto.UserDto>
     */
    @ApiOperation(value = "登录", notes = "登录")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping("/logins")
    RestfulApiResponse<UserDto> queryAuditRecords(@RequestBody UserParam userParam, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession);



    @ApiOperation(value = "用户登陆", notes = "用户登陆")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping
    RestfulApiResponse<UserDto> login(@RequestBody AuthUserParam authUserParam) throws Exception;


    /**
     * 刷新token
     * @return
     */
    @ApiOperation(value = "刷新token", notes = "刷新token")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping("/refreshToken")
    RestfulApiResponse<UserDto> refreshToken();
    /**
     * 退出系统
     * @return
     */
    @ApiOperation(value = "退出登录", notes = "退出登录")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping("/loginOut")
    RestfulApiResponse loginOut();



}
