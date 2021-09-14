package com.rjhc.credit.information.service.api;

import com.rjhc.credit.information.service.api.model.dto.AnnualStatisticsDto;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName AnnualStatisticsInterface
 * @Description: TODO
 * @Author grx
 * @Date 2020/10/10
 * @Version V1.0
 **/
@RequestMapping("/annualStatistics")
@Api(value = "报表统计分析年份折线图", tags = {"报表统计分析年份折线图"})
public interface AnnualStatisticsInterface {

    @ApiOperation(value = "根据年份查询报表信息", notes = "根据年份查询报表信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/queryReportStatistics")
    RestfulApiResponse<List<AnnualStatisticsDto>> queryReportStatistics() throws ExecutionException, InterruptedException;


    @ApiOperation(value = "获取年份", notes = "获取年份")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/selYear")
    RestfulApiResponse<List<String>> selYear();

    @ApiOperation(value = "测试借口", notes = "测试接口")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping
    RestfulApiResponse<List<String>> test11();

}
