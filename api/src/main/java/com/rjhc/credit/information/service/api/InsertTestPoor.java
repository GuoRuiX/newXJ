package com.rjhc.credit.information.service.api;

import com.rjhc.credit.information.service.api.model.dto.AnnualStatisticsDto;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName InsertTestPoor
 * @Description: TODO
 * @Author grx
 * @Date 2021/3/5
 * @Version V1.0
 **/
@RequestMapping("/insertTestPoor")
@Api(value = "测试新增贫困户接口", tags = {"测试新增贫困户接口"})
public interface InsertTestPoor {

    @ApiOperation(value = "测试新增贫困户接口", notes = "测试新增贫困户接口")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/queryReportStatistics")
    RestfulApiResponse savePoor() throws Exception;
}
