package com.rjhc.credit.information.service.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.param.CountyParam;
import com.rjhc.credit.information.service.api.model.param.PoorHouseholdsInformationParam;
import com.rjhc.credit.information.service.api.model.param.ReportParam;
import com.rjhc.credit.information.service.api.model.param.StatisticalAnalysisParam;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName ReportStatistics
 * @Description: TODO
 * @Author grx
 * @Date 2020/10/10
 * @Version V1.0
 **/
@RequestMapping("/reportStatistics")
@Api(value = "报表统计分析", tags = {"报表统计分析"})
public interface ReportStatisticsInterface {


    @ApiOperation(value = "根据年份查询报表信息", notes = "根据年份查询报表信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping
    RestfulApiResponse<List<Map<String,String>>> queryReportStatistics(@RequestBody ReportParam reportParam) throws Exception;



    @ApiOperation(value = "数据同步按钮", notes = "数据同步按钮")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/updateTableName")
    void  updateTableName() throws Exception;

    @ApiOperation(value = "根据年份查询地图信息", notes = "根据年份查询地图信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "years", value = "年份", required = true, paramType = "path", dataType = "string")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/{years}/{type}")
    RestfulApiResponse<List<Map<String,String>>> selstaAndPoorByBankAddress(@PathVariable("years") String years,@PathVariable("type")String type);

    @ApiOperation(value = "根据上传人员机构分组查询等级不为空的信贷数量", notes = "根据上传人员机构分组查询等级不为空的信贷数量")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/selStaCountByLevel/{year}")
    RestfulApiResponse<List<Map<String,String>>> selStaCountByLevel(@PathVariable("years") String years);

    @ApiOperation(value = "根据时间统计当前信贷笔数", notes = "根据时间统计当前信贷笔数")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping(value = "/selectByDate")
    RestfulApiResponse<Long> selectByDate(@RequestBody StatisticalAnalysisParam statisticalAnalysisParam);

    @ApiOperation(value = "贫困县菜单展示数据", notes = "贫困县菜单展示数据")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping(value = "/slecounty")
    RestfulApiResponse<List<Map<String,String>>> slecounty(@RequestBody CountyParam countyParam) throws Exception;


}
