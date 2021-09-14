package com.rjhc.credit.information.service.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.OperationLogDto;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.param.OperationLogParam;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @ClassName operationLog
 * @Description: 日志管理
 * @Author grx
 * @Date 2020/8/25
 * @Version V1.0
 **/
@RequestMapping("/logs")
@Api(value = "日志管理", tags = {"日志管理"})
public interface OperationLogInterface {
    /**
     * 功能描述：
     * 〈日志管理列表展示以及模糊查询〉
     * @Author: grx
     * @Date: 3:06 下午 2020/8/25
     * @param operationLogParam
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.rjhc.credit.information.service.api.model.dto.OperationLogDto>>
     */
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping
    RestfulApiResponse<Page<OperationLogDto>> selectLogs(@RequestBody OperationLogParam operationLogParam);
    /**
     * 功能描述：
     * 〈日志导出〉
     * @Author: grx
     * @Date: 6:34 下午 2020/8/26
     * @param userName
     * @param modelName
     * @param operationName
     * @param operationDate
     * @param response
     * @param request
     * @return: void
     */
    @ApiOperation(value = "日志导出",produces="application/octet-stream")
    @GetMapping("/exporExcel/{userName}/{modelName}/{operationName}/{operationDate}")
    void exporExcel(@PathVariable("userName")String userName, @PathVariable("modelName")String modelName, @PathVariable("operationName")String operationName, @PathVariable("operationDate")String operationDate, HttpServletResponse response, HttpServletRequest request) throws Exception;



    @ApiOperation(value = "测试")
    @GetMapping("/testDate")
    RestfulApiResponse<String> testDate();
}
